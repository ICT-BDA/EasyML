/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.visual.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import eml.studio.server.visual.charts.ScatterChart;
import eml.studio.server.visual.statics.ThreadDataParser;
import eml.studio.server.visual.statics.ThreadUtils;
import eml.studio.shared.util.DataTools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *  Service for generate scatter chart
 */
public class ScatterChartService extends ChartService {

	private static final Logger logger = Logger.getLogger(ChartService.class.getName());

	/**
	 * Get scatter chart data option string to draw the chart
	 * 
	 * @param filePath   data file path
	 * @param dataType data type
	 * @param columns  data all columns
	 * @param col_x selected x column
	 * @param logScale_x  log scale for x column
	 * @param col_y  selected y column
	 * @param logScale_y log scale for y column
	 * @param col_c  selected column column
	 * @return
	 * @throws IllegalArgumentException
	 */
	public ScatterChart ScatterChartGen(String filePath, String dataType, List<String> columns, String col_x, boolean logScale_x, String col_y, boolean logScale_y, String col_c)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub

		//Read data from hdfs
		List<String> results = readAllData(filePath,true); //Only read limited line data(if the data length over max length)

		//Prepare selected column list and init result list
		List<String> selectedCols = new ArrayList<String>();
		selectedCols.add(col_x);
		selectedCols.add(col_y);
		selectedCols.add(col_c);
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

		List<String> datas_x = selectedColsDatas.get(0);
		List<String> datas_y = selectedColsDatas.get(1);
		List<String> datas_c = selectedColsDatas.get(2);
		logger.info("【Scatter Chart】Parse data column="+col_x+", size = " + datas_x.size());
		logger.info("【Scatter Chart】Parse data column="+col_y+", size = " + datas_y.size());
		logger.info("【Scatter Chart】Parse data column="+col_c+", size = " + datas_c.size());

		//Get data type（Numerical Or String）
		String verifyTypeData_x = null;
		String verifyTypeData_y = null;
		String verifyTypeData_c = null;
		for(int i =0;i<datas_x.size();i++)
		{
			if(datas_x.get(i)==null || datas_x.get(i).trim().equals(""))
				continue;
			else if(datas_y.get(i)==null || datas_y.get(i).trim().equals(""))
				continue;
			else if(datas_c.get(i)==null || datas_c.get(i).trim().equals(""))
				continue;
			else {
				verifyTypeData_x = datas_x.get(i);
				verifyTypeData_y = datas_y.get(i);
				verifyTypeData_c = datas_c.get(i);
				break;
			}	
		}
		boolean numeric_x = DataTools.isNumeric(verifyTypeData_x);
		boolean numeric_y = DataTools.isNumeric(verifyTypeData_y);
		boolean numeric_c = DataTools.isNumeric(verifyTypeData_c);


		Map<Integer,String> scatterlabels = new TreeMap<Integer,String>();

		Set<String> colors = new LinkedHashSet<String>();  

		if(numeric_x && numeric_y) {
			if(numeric_c) {
				double max = Double.MIN_VALUE;
				double min = Double.MAX_VALUE;
				for(String data_c : datas_c)
				{
					if(data_c == null || "".equals(data_c.trim()))
						continue;
					double value = Double.valueOf(data_c);
					if(value > max)
						max = value;
					if(value < min)
						min = value;
				}
				int index = 0;
				for(int i =0;i<datas_x.size();i++) {
					String data_x = datas_x.get(i);
					if(logScale_x)
					{
						if(Double.valueOf(data_x)>0)
							data_x = logData(data_x);
						else
							continue;
					}
					String data_y = datas_y.get(i);
					if(logScale_y)
					{
						if(Double.valueOf(data_y)>0)
							data_y = logData(data_y);
						else
							continue;
					}

					String value = "[" + data_x + "," + data_y + "," + datas_c.get(i) + "]";
					scatterlabels.put(index, value);
					index++;
				}
				List<Map.Entry<Integer,String>> mapList = new ArrayList<Map.Entry<Integer,String>>(scatterlabels.entrySet());
				String[] values = new String[scatterlabels.size()];
				for(int i = 0; i<scatterlabels.size();i++) {
					if(mapList.get(i).getKey() != null) {
						values[i] = mapList.get(i).getValue();
					}
				}
				ScatterChart scatterChart = new ScatterChart(min,max,values);
				return scatterChart;
			}else {
				int index = 0;
				for(int i =0;i<datas_x.size();i++) {
					String data_x = datas_x.get(i);
					if(logScale_x)
					{
						if(Double.valueOf(data_x)>0)
							data_x = logData(data_x);
						else
							continue;
					}
					String data_y = datas_y.get(i);
					if(logScale_y)
					{
						if(Double.valueOf(data_y)>0)
							data_y = logData(data_y);
						else
							continue;
					}

					String value = "[" + data_x + "," + data_y + "," + "'" + datas_c.get(i) + "'" + "]";
					colors.add(datas_c.get(i));
					scatterlabels.put(index, value);
					index++;
				}
			}
		}else {
			ScatterChart n_scatterChart = new ScatterChart(null,null);
			return n_scatterChart;
		}

		List<Map.Entry<Integer,String>> mapList = new ArrayList<Map.Entry<Integer,String>>(scatterlabels.entrySet());

		String[] values = new String[scatterlabels.size()];
		for(int i = 0; i<scatterlabels.size();i++) {
			if(mapList.get(i).getKey() != null) {
				values[i] = mapList.get(i).getValue();
			}
		}

		String[] finalColors = (String[]) colors.toArray(new String[colors.size()]);
		ScatterChart scatterChart = new ScatterChart(finalColors,values);
		return scatterChart;
	}
	/**
	 * Compute log data value
	 * 
	 * @param data
	 * @return
	 */
	private String logData(String data)
	{
		double result = Math.log(Double.valueOf(data));
		return String.valueOf(m3(result));
	}
}