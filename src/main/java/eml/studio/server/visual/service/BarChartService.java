/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.visual.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import eml.studio.server.visual.charts.BarChart;
import eml.studio.server.visual.statics.ThreadDataParser;
import eml.studio.server.visual.statics.ThreadUtils;
import eml.studio.shared.util.DataTools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Service for generate bar chart
 */
public class BarChartService extends ChartService {

	private static final Logger logger = Logger.getLogger(ChartService.class.getName());
	private static final int MAX_GROUP_SIZE = 16;

	/**
	 * Get bar chart data option string to draw the chart
	 * 
	 * @param filePath   data file path
	 * @param dataType data type
	 * @param columns  data all columns
	 * @param gByCol group by column
	 * @param valCol  value column
	 * @param aggCol aggregation column
	 * @param logScale  value column log scale
	 * @return
	 * @throws IllegalArgumentException
	 */
	public BarChart barChartGen(String filePath, String dataType, List<String> columns, String gByCol, String valCol, String aggCol,boolean logScale)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub

		//Read data from hdfs
		List<String> results = readAllData(filePath,true); //Only read limited line data(if the data length over max length)

		//Prepare selected column list and init result list
		List<String> selectedCols = new ArrayList<String>();
		selectedCols.add(gByCol);
		selectedCols.add(valCol);
		List<List<String>>  selectedColsDatas = new ArrayList<List<String>>();
		initiaColResults(selectedColsDatas,selectedCols);

		//Use Multithread to parse specific column data 
		ExecutorService exec = Executors.newCachedThreadPool();
		int threadNum = ThreadUtils.THREAD_NUM;
		int countNum = (int)Math.ceil(results.size() / threadNum);
		for(int i=0; i<threadNum; i++) { 
			int startInx = i*countNum;
			int endInx = startInx + countNum;
			if(i == threadNum-1)
				endInx = results.size();
			exec.execute(new ThreadDataParser(results.subList(startInx, endInx),selectedCols,columns,dataType,selectedColsDatas));
		} 
		exec.shutdown();
		// wait until sum-process is over 
		try {
			exec.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.info("exec.awaitTermination(1, TimeUnit.HOURS) occurs error : " + e);
		}

		List<String> gByDatas = selectedColsDatas.get(0);
		List<String> valDatas = selectedColsDatas.get(1);

		logger.info("【BarChart】Parse  group column="+gByCol+",  size = " + gByDatas.size());
		logger.info("【BarChart】Parse  value column="+valCol+",  size = " + valDatas.size());

		//Use group column to group value column data
		HashMap<String,List<String>>  groupDatas = new HashMap<String,List<String>>();
		for(int k=0;k<gByDatas.size();k++)
		{
			String key = gByDatas.get(k);
			String value = valDatas.get(k);
			if(groupDatas.containsKey(key))
				groupDatas.get(key).add(value);
			else
			{
				List<String> values = new ArrayList<String>();
				values.add(value);
				groupDatas.put(key, values);
			}
		}

		// If the group over MAX_GROUP_SIZE, then return barChart with null category, tell user this column can not group data
		if(groupDatas.keySet().size()>MAX_GROUP_SIZE) 
		{
			double[] datas = new double[groupDatas.keySet().size()];
			BarChart barChart = new BarChart(null,datas);
			return barChart;
		}else{
			HashMap<String,Double> statRets = new HashMap<String,Double>();
			switch(aggCol)
			{
			case  "average":  computAvg(groupDatas,statRets); break;
			case  "stand_deviation":  computStand(groupDatas,statRets); break;
			case  "count":  computCount(groupDatas,statRets); break;
			case  "minimum":  computMin(groupDatas,statRets); break;
			case  "maximum":  computMax(groupDatas,statRets); break;
			}

			if(statRets.size() == 0)  //If the data is not numerical, the return barChart with null values, tell user this column is not numerical
			{
				String[] categories = new String[MAX_GROUP_SIZE];
				return new BarChart(categories,null);
			}

			String[] categories = new String[statRets.keySet().size()];
			double[] values = new double[statRets.keySet().size()];
			Iterator<Entry<String,Double>>  dataIter = statRets.entrySet().iterator();
			int k =0;
			while(dataIter.hasNext())
			{
				Entry<String,Double> entry = dataIter.next();
				categories[k] = entry.getKey();
				if(logScale)
					values[k] = m3(Math.log(entry.getValue()));
				else
					values[k] = entry.getValue();
				k++;
			}
			BarChart barChart = new BarChart(categories,values);
			return barChart;
		}
	}

	/**
	 * Compute  data average
	 * 
	 * @param groupDatas  group list datas
	 * @param statRets  group average values
	 */
	private  void computAvg(HashMap<String,List<String>> groupDatas,HashMap<String,Double> statRets)
	{
		Iterator<Entry<String,List<String>>>  dataIter = groupDatas.entrySet().iterator();
		while(dataIter.hasNext())
		{
			Entry<String,List<String>> entry = dataIter.next();
			String key = entry.getKey();
			List<String> datas = entry.getValue();
			double sum = 0;
			int count = 0;
			for(String data : datas)
			{
				if(data.equals(""))
					continue;
				if(DataTools.isNumeric(data))
				{
					count = count +1;
					sum = sum + Double.valueOf(data);
				}else{ // As long as one is not numeric, it returns null
					statRets = new HashMap<String,Double>();
					return;
				}
			}

			if(count != 0)
				statRets.put(key, sum/count);
		}
	}

	/**
	 * Compute standard deviation
	 * 
	 * @param groupDatas group list datas
	 * @param statRets  group standard deviation values
	 */
	private  void computStand(HashMap<String,List<String>> groupDatas,HashMap<String,Double> statRets)
	{
		Iterator<Entry<String,List<String>>>  dataIter = groupDatas.entrySet().iterator();
		while(dataIter.hasNext())
		{
			Entry<String,List<String>> entry = dataIter.next();
			String key = entry.getKey();
			List<String> datas = entry.getValue();
			double sum = 0;
			int count = 0;
			for(String data : datas)
			{
				if(DataTools.isNumeric(data))
				{
					count = count +1;
					sum = sum + Double.valueOf(data);
				}else{ // As long as one is not numeric, it returns null
					statRets = new HashMap<String,Double>();
					return;
				}
			}

			if(count != 0)
			{	
				double avg = sum/count;
				double stdSum = 0;
				for(String data: datas)
				{
					if(DataTools.isNumeric(data))
					{
						double numData = Double.valueOf(data);
						stdSum = stdSum +Math.pow(numData-avg, 2);
					}
				}
				statRets.put(key, Math.sqrt(stdSum/count));
			}
		}
	}

	/**
	 * Compute data count
	 * 
	 * @param groupDatas group list datas
	 * @param statRets  group count values
	 */
	private  void computCount(HashMap<String,List<String>> groupDatas,HashMap<String,Double> statRets)
	{
		Iterator<Entry<String,List<String>>>  dataIter = groupDatas.entrySet().iterator();
		while(dataIter.hasNext())
		{
			Entry<String,List<String>> entry = dataIter.next();
			String key = entry.getKey();
			List<String> datas = entry.getValue();
			int count = 0;
			for(String data : datas)
			{
				if(DataTools.isNumeric(data))
				{
					count = count +1;
				}else{ // As long as one is not numeric, it returns null
					statRets = new HashMap<String,Double>();
					return;
				}
			}

			if(count != 0)
				statRets.put(key, Double.valueOf(count));
		}
	}

	/**
	 * Compute minimum value
	 * 
	 * @param groupDatas  group list datas
	 * @param statRets  group minimum values
	 */
	private  void computMin(HashMap<String,List<String>> groupDatas,HashMap<String,Double> statRets)
	{
		Iterator<Entry<String,List<String>>>  dataIter = groupDatas.entrySet().iterator();
		while(dataIter.hasNext())
		{
			Entry<String,List<String>> entry = dataIter.next();
			String key = entry.getKey();
			List<String> datas = entry.getValue();
			double min = Double.MAX_VALUE ;
			int count = 0;
			for(String data : datas)
			{
				if(DataTools.isNumeric(data))
				{
					if(Double.valueOf(data) < min)
						min = Double.valueOf(data);
					count = count +1;
				}else{ // As long as one is not numeric, it returns null
					statRets = new HashMap<String,Double>();
					return;
				}
			}

			if(count != 0)
				statRets.put(key, min);
		}
	}

	/**
	 * Compute maximal value
	 * 
	 * @param groupDatas group list datas
	 * @param statRets  group maximal values
	 */
	private  void computMax(HashMap<String,List<String>> groupDatas,HashMap<String,Double> statRets)
	{
		Iterator<Entry<String,List<String>>>  dataIter = groupDatas.entrySet().iterator();
		while(dataIter.hasNext())
		{
			Entry<String,List<String>> entry = dataIter.next();
			String key = entry.getKey();
			List<String> datas = entry.getValue();
			double max = Double.MIN_VALUE ;
			int count = 0;
			for(String data : datas)
			{
				if(DataTools.isNumeric(data))
				{
					if(Double.valueOf(data) > max)
						max = Double.valueOf(data);
					count = count +1;
				}else{ // As long as one is not numeric, it returns null
					statRets = new HashMap<String,Double>();
					return;
				}
			}

			if(count != 0)
				statRets.put(key, max);
		}
	}

}