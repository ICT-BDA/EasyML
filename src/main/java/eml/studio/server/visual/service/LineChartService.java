/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.visual.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import eml.studio.server.visual.charts.LineChart;
import eml.studio.server.visual.statics.ThreadSortedDataParser;
import eml.studio.server.visual.statics.ThreadUtils;
import eml.studio.shared.util.DataTools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *  Service for generate line chart
 */
public class LineChartService extends ChartService {

	private static final Logger logger = Logger.getLogger(ChartService.class.getName());

	/**
	 * Get line chart data option string to draw the chart
	 * 
	 * @param filePath   data file path
	 * @param dataType data type
	 * @param columns  data all columns
	 * @param columnNames select data columns( list data)
	 * @return
	 * @throws IllegalArgumentException
	 */
	public List<LineChart> LineChartGen(String filePath, String dataType, List<String> columns, List<String> columnNames)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		//Read data from hdfs
		List<String> results = readAllData(filePath,true); //Only read limited line data(if the data length over max length)

		List<LineChart> lineCharts = new ArrayList<LineChart>();
		for(String  selectedCol : columnNames)
		{
			LineChart lineChart =  singleLineGen(results, dataType, columns, selectedCol);
			if(lineChart == null)
				continue;
			else
				lineCharts.add(lineChart);
		}

		return lineCharts;
	}

	/**
	 * Generate single line chart
	 * 
	 * @param totalDatas  total datas
	 * @param dataType    data type
	 * @param columns    data columns
	 * @param selectedCol   one of user select column
	 * @return
	 */
	private LineChart  singleLineGen(List<String> totalDatas, String dataType, List<String> columns, String selectedCol)
	{

		//Prepare selected column list and init result list
		List<String> selectedCols = new ArrayList<String>();
		selectedCols.add(selectedCol);
		TreeMap<Integer,List<List<String>>> selectedColsDatas = new TreeMap<Integer,List<List<String>>>();
		
		//Use Multithread to parse specific column data 
		ExecutorService exec = Executors.newCachedThreadPool();
		int threadNum = ThreadUtils.THREAD_NUM;
		int countNum = (int)Math.ceil(totalDatas.size() / threadNum);
		for(int i=0; i<threadNum; i++){ 
			int startInx = i*countNum;
			int endInx = startInx + countNum;
			if(i == threadNum-1)
				endInx = totalDatas.size();
			exec.execute(new ThreadSortedDataParser(totalDatas.subList(startInx, endInx),selectedCols,columns,dataType,selectedColsDatas,i));
		} 
		exec.shutdown();
		// wait until sum-process is over 
		try {
			exec.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.info("exec.awaitTermination(1, TimeUnit.HOURS) occurs error : " + e);
		}
		//Re group the data to its normal order
		List<List<String>> reGroupDatas = new ArrayList<List<String>>();
		initiaColResults(reGroupDatas,selectedCols);
		for(Integer key:selectedColsDatas.keySet())
		{
			List<List<String>> localColDatas  = selectedColsDatas.get(key);
			for(int i= 0 ;i<selectedCols.size();i++)
			{
				reGroupDatas.get(i).addAll(localColDatas.get(i));
			}
		}
		List<String> datas = reGroupDatas.get(0) ;
		logger.info("【Line Chart】Parse data column="+selectedCol+", size = " + datas.size());

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
		Map<Integer,Double> linelabels = new TreeMap<Integer,Double>();
		boolean numeric = DataTools.isNumeric(verifyTypeData);

		if(numeric) {
			int index = 0;
			for(String data : datas) {
				if(data == null || "".equals(data.trim()))
					continue;
				double value = Double.valueOf(data);
				linelabels.put(index, value);
				index++;
			}
		} else {
			LineChart n_lineChart = new LineChart(null,null);
			return n_lineChart;
		}
		
		List<Map.Entry<Integer,Double>> mapList = new ArrayList<Map.Entry<Integer,Double>>(linelabels.entrySet());
		int[] labels = new int[datas.size()];
		double[] values = new double[datas.size()];
		for(int i = 0; i<datas.size();i++) {
			if(mapList.get(i).getKey() != null) {
				labels[i] = mapList.get(i).getKey();
				values[i] = mapList.get(i).getValue();
			}
		}
		LineChart lineChart = new LineChart(labels,values);
		return lineChart;
	}
}