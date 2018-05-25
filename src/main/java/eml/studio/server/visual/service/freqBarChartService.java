/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.visual.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import eml.studio.server.visual.charts.BarChart;
import eml.studio.server.visual.statics.ThreadDataParser;
import eml.studio.server.visual.statics.ThreadUtils;
import eml.studio.shared.util.DataTools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *  Service for generate frequency bar chart in statistics module
 */
public class freqBarChartService extends ChartService {
	
	private static final Logger logger = Logger.getLogger(ChartService.class.getName());
	
	/**
	 * Get bar chart data option string to draw the chart
	 * 
	 * @param filePath   data file path
	 * @param dataType data type
	 * @param columns  data all columns
	 * @param columnName select data column
	 * @param binSize    top-k data frequence to show
	 * @param logScale  if the value should be logged (log(value))
	 * @return
	 * @throws IllegalArgumentException
	 */
	public BarChart freqBarChartGen(String filePath, String dataType, List<String> columns, String columnName, int binSize, boolean logScale)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		//Read data from hdfs
		List<String> results = readAllData(filePath,true); //Only read limited line data(if the data length over max length)
		
		//Prepare selected column list and init result list
		List<String> selectedCols = new ArrayList<String>();
		selectedCols.add(columnName);
		List<List<String>>  selectedColsDatas = new ArrayList<List<String>>();
		initiaColResults(selectedColsDatas,selectedCols);

		//Use Multithread to parse specific column data 

		ExecutorService exec = Executors.newCachedThreadPool();
		int threadNum = ThreadUtils.THREAD_NUM;
		int countNum = (int)Math.ceil(results.size() / threadNum);
		for(int i=0; i<threadNum; i++){ 
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
		
		List<String> datas = selectedColsDatas.get(0);
		logger.info("【Frequent Bar Chart】Parse data column="+columnName+", size = " + datas.size());

		//Get data type（Numerical or String）
		String verifyTypeData = null;
		for(int i =0;i<datas.size();i++)
		{
			if(datas.get(i)==null || datas.get(i).trim().equals(""))
				continue;
			else
			{
				verifyTypeData = datas.get(i);
				break;
			}
		}
		Map<String,Integer> labelCounts = new TreeMap<String,Integer>();
		boolean numeric = DataTools.isNumeric(verifyTypeData);
		int range_p = 0;
		
		//Numerical processing
		if(numeric)
		{
			logger.info("Numeric data chart service");
			double max = Double.MIN_VALUE;
			double min = Double.MAX_VALUE;
			for(String data : datas)
			{
				if(data == null || "".equals(data.trim()))
					continue;
				double value = Double.valueOf(data);
				if(value > max)
					max = value;
				if(value < min)
					min = value;
			}
			int range = (int) Math.ceil(((max - min)/binSize));
			range_p = range;

			
			boolean floatFlag = false;
			if(max%1!=0 || min%1!=0)
				floatFlag = true;

			if(range>1)
			{
				logger.info("Range segment, range = "+range);
				int[] bags = new int[binSize];
				bags[0] = (int)min + range;
				//Build a drawer for data statistics
				for(int i=1;i<bags.length;i++)
				{
					bags[i] = bags[i-1]+range;
				}
				logger.info("min="+min+"; max="+max+"; data bags="+ bags.length);
				int a = (int) Math.floor(min);   //Lower bound
				int b = (int) Math.ceil(max); //Upper bound
				for(int i=0;i<bags.length;i++)
				{
					if(i == 0)
					{
						a = (int) Math.floor(min);
						b = bags[i];
					}else{
						a = bags[i-1];
						b = bags[i];
					}
					if(i == bags.length - 1)  //The last drawer is closed for left and right
					{
						String key = "["+a+","+b+"]";
						labelCounts.put(key, 0);
					}else{ //The middle drawer is closed for left and opened for right
						String key = "["+a+","+b+")";
						labelCounts.put(key, 0);
					}
				}
				for(String data : datas)
				{
					if(data == null || "".equals(data.trim()))
						continue;
					else
					{
						for(int i=0;i<bags.length;i++)
						{
							double dataT = Double.valueOf(data);
							if(i == 0)
							{
								a = (int) Math.floor(min);
								b = bags[i];
							}else{
								a = bags[i-1];
								b = bags[i];
							}
							if(i == bags.length - 1)  //The last drawer is closed for left and right
							{
								if(dataT >=a && dataT<=b)
								{
									String key = "["+a+","+b+"]";
									if(labelCounts.containsKey(key))
										labelCounts.put(key, labelCounts.get(key)+1);
									else
										labelCounts.put(key, 1);
									break;
								}
							}else{ //The middle drawer is closed for left and opened for right
								if(dataT >=a && dataT<b)
								{
									String key = "["+a+","+b+")";
									if(labelCounts.containsKey(key))
										labelCounts.put(key, labelCounts.get(key)+1);
									else
										labelCounts.put(key, 1);
									break;
								}
							}
						}
					}
				}
			}else if(floatFlag){
				logger.info("Float data, new range segment, range = "+range);
				double newRange = m3(Math.ceil(((max - min)/(binSize))));
				double[] bags = new double[binSize];
				bags[0] = m3(min + newRange);
				
				for(int i=1;i<bags.length;i++)
				{
					bags[i] = m3(bags[i-1]+range);
				}
				logger.info("min="+min+"; max="+max+"; data bags="+ bags.length);
				for(String data : datas)
				{
					if(data == null || "".equals(data.trim()))
						continue;
					else
					{
						double a = m3(Math.floor(min));   
						double b = m3(Math.ceil(max)); 
						for(int i=0;i<bags.length;i++)
						{
							double dataT = Double.valueOf(data);
							if(i == 0)
							{
								a = m3(Math.floor(min));
								b = bags[i];
							}else{
								a = bags[i-1];
								b = bags[i];
							}
							if(i == bags.length - 1)  
							{
								if(dataT >=a && dataT<=b)
								{
									String key = "["+a+","+b+"]";
									if(labelCounts.containsKey(key))
										labelCounts.put(key, labelCounts.get(key)+1);
									else
										labelCounts.put(key, 1);
									break;
								}
							}else{ 
								if(dataT >=a && dataT<b)
								{
									String key = "["+a+","+b+")";
									if(labelCounts.containsKey(key))
										labelCounts.put(key, labelCounts.get(key)+1);
									else
										labelCounts.put(key, 1);
									break;
								}
							}
						}
					}
				}
			}
			else //If the value is small, process as discrete
			{
				logger.info("Numeric data dispersed chart service");
				for(String data : datas)
				{
					if(data == null || "".equals(data.trim()))
						continue;
					else
					{
						if(labelCounts.containsKey(data))
						{
							int count = labelCounts.get(data)+1;
							labelCounts.put(data, count);
						}else{
							labelCounts.put(data, 1);
						}
					}
				}
			}

		}else{  //String process
			logger.info("String data chart service");
			for(String data : datas)
			{
				if(data == null || "".equals(data.trim()))
					continue;
				else
				{
					if(labelCounts.containsKey(data))
					{
						int count = labelCounts.get(data)+1;
						labelCounts.put(data, count);
					}else{
						labelCounts.put(data, 1);
					}
				}
			}
		}

		
		List<Map.Entry<String,Integer>> mapList = new ArrayList<Map.Entry<String,Integer>>(labelCounts.entrySet());
	
		//If is numeric and the range>1，sort the X axis in ascending order, otherwise, sort automatically according to TreeMap
		if(numeric)
		{
			if(range_p > 1){
			Collections.sort(mapList,new Comparator<Map.Entry<String,Integer>>() {
				public int compare(Entry<String, Integer> o1,
						Entry<String, Integer> o2) {
					String o1_key = o1.getKey();
					String o2_key = o2.getKey();
					int o1_numkey = Integer.parseInt(o1_key.substring(1, o1_key.indexOf(',')));
					int o2_numkey = Integer.parseInt(o2_key.substring(1, o2_key.indexOf(',')));
					return o1_numkey- o2_numkey;
				}

			});
			}
		}

		//Get the top binSize value by frequency
		if(mapList.size()<binSize)
			binSize = mapList.size();

		String[] labels = new String[binSize];
		double[] values = new double[binSize];
		for(int i = 0; i<binSize;i++)
		{
			labels[i] = mapList.get(i).getKey();
			if(logScale)
			{
				values[i] = Math.log(Double.valueOf(mapList.get(i).getValue()));
				if (values[i] == Double.NEGATIVE_INFINITY){
					values[i] = 0;
				}
			}else
				values[i] = mapList.get(i).getValue();
		}

		StringBuffer labelBuf = new StringBuffer();
		StringBuffer valueBuf = new StringBuffer();
		for(int i=0;i<binSize; i++)
		{
			labelBuf.append(labels[i]+"  ");
			valueBuf.append(values[i]+"  ");
		}
		logger.info("x_axis:"+labelBuf.toString());
		logger.info("y_axis:"+valueBuf.toString());
		BarChart freqBarChart = new BarChart(labels,values);
		return freqBarChart;
	}
}