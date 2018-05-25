/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.visual.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import eml.studio.server.constant.Constants;
import eml.studio.server.util.DataParser;
import eml.studio.server.util.HDFSIO;
import eml.studio.server.visual.statics.ThreadDataParser;
import eml.studio.server.visual.statics.ThreadUtils;
import eml.studio.shared.util.DataFeature;
import eml.studio.shared.util.DataTools;
import eml.studio.shared.util.DatasetType;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Specific methods in data feature statics and analysis service
 */
public class DataAnalysisService{

	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(DataAnalysisService.class.getName());

	/**
	 * Extract data list common feature
	 * 
	 * @param dataPath data path on hdfs
	 * @param dataType  data type
	 * @param columns all data columns
	 * @param columnName  select data column
	 * @return
	 */
	public DataFeature extDataFeature(String filePath,String dataType, List<String> columns, String columnName) {
		// TODO Auto-generated method stub

		// Read data from HDFS
		long start = System.currentTimeMillis();
		List<String> results = readAllData(filePath,true);// Read part data

		// If the data include column data, should remove it
		if(!columns.get(0).contains("Col"))
			results = results.subList(1, results.size());
		logger.info("Read data time = "+String.valueOf(System.currentTimeMillis()-start));

		start = System.currentTimeMillis();

		//Prepare selected column list and init result list
		List<String> selectedCols = new ArrayList<String>();
		selectedCols.add(columnName);
		List<List<String>>  selectedColsDatas = new ArrayList<List<String>>();
		for(String selectedCol : selectedCols)
			selectedColsDatas.add(new ArrayList<String>());
		
		// Use multi-thread to data analysis and get selected column data
		ExecutorService exec = Executors.newCachedThreadPool();
		int threadNum = ThreadUtils.THREAD_NUM;
		int count = (int)Math.ceil(results.size() / threadNum);
		for(int i=0; i<threadNum; i++){ 
			int startInx = i*count;
			int endInx = startInx + count;
			if(i == threadNum-1)
				endInx = results.size();
			exec.execute(new ThreadDataParser(results.subList(startInx, endInx),selectedCols,columns,dataType,selectedColsDatas));
		} 
		exec.shutdown();
		// wait until sum-process is over 
		try {
			exec.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.info("exec.awaitTermination(5, TimeUnit.MINUTES) occurs error : " + e);
		}
		
		List<String> datas =selectedColsDatas.get(0) ;
		logger.info("Parse data column size = " + datas.size()+"; time = "+String.valueOf(System.currentTimeMillis()-start));
		
		//Get data number type（Numberic or String）
		start = System.currentTimeMillis();
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
		boolean numeric = DataTools.isNumeric(verifyTypeData);

		//Data feature extraction
		DataFeature dataFeature = new DataFeature();
		int missingNum =0;
		Set<String> uniqueDatas = new HashSet<String>();
		if(!numeric)
		{
			for(String data : datas)
			{
				if(data == null || "".equals(data.trim()))
					missingNum = missingNum +1;
				else
					uniqueDatas.add(data);
			}
			dataFeature = new DataFeature(uniqueDatas.size(),missingNum,numeric);
		}else{
			double sum = 0;
			List<Double> effictDatas = new ArrayList<Double>();
			for(String data : datas)
			{
				if(data == null || "".equals(data.trim()))
					missingNum = missingNum +1;
				else
				{
					uniqueDatas.add(data);
					effictDatas.add(Double.valueOf(data));
					sum = sum + Double.valueOf(data);
				}
			}
			int effSize = effictDatas.size();
			Collections.sort(effictDatas);

			double mean = sum/effSize;
			double min = effictDatas.get(0);
			double max =effictDatas.get(effSize-1);

			double s = 0;
			for(double d : effictDatas)
			{
				s = s + (d-mean)*(d-mean);
			}
			double standDeviation=(s/effSize);

			double median = effictDatas.get(0);
			if(effictDatas.size()%2 == 0)
			{
				double left = effictDatas.get(effSize/2-1);
				double right = effictDatas.get(effSize/2);
				median = (left+right)/2;
			}
			else
				median = effictDatas.get((effSize+1)/2);
			dataFeature = new DataFeature(m3(mean),  median,  min ,  max, m3(standDeviation), uniqueDatas.size(),missingNum,numeric);
		}
		logger.info("Unique data = "+uniqueDatas.size()+"Missing num="+missingNum +"Numberic="+numeric);
		logger.info("Feature parse time = "+String.valueOf(System.currentTimeMillis()-start));
		return dataFeature;
	}

	/**
	 * Get page json  string data from data path, a string str is a json string
	 * 
	 * @param dataPath   hdfs data path
	 * @param dataType data type
	 * @param start  start index
	 * @param columns  all data columns
	 * @param size page size
	 * @return
	 */
	public ArrayList<String> pageData(String dataPath,String dataType, List<String> columns,int start, int size)  {
		// TODO Auto-generated method stub
		List<String> datas = readPageData(dataPath,start,size);

		//Remove header line
		if(!columns.get(0).contains("Col") && start == 0)
			datas = datas.subList(1, datas.size());

		List<JSONObject> obj = DataParser.parse(datas,dataType,columns);

		ArrayList<String> jsonStrs = new ArrayList<String>();
		for(int i=0;i<obj.size();i++)
		{
			jsonStrs.add(obj.get(i).toString());
		}
		logger.info("data page, start = "+start+", size = "+size);
		return jsonStrs;
	}

	/**
	 * Get json data column
	 * 
	 * @param dataPath data hdfs path
	 * @return
	 */
	public LinkedHashMap<String, Integer> getDataColumn(String dataPath,String dataType) {
		// TODO Auto-generated method stub
		List<String> datas = readPageData(dataPath,0,2);
		LinkedHashMap<String,Integer> columns = new LinkedHashMap<String,Integer>();

		//Get data column name
		List<String> colNames = new ArrayList<String>();
		List<JSONObject> obj = null;
		if(dataType.equals(DatasetType.JSON.toString()))
		{
			obj = DataParser.parse(datas,dataType,null);
			colNames = new ArrayList<String>(obj.get(0).keySet());
		}
		else
		{
			boolean flag = DataParser.haveColumn(datas,dataType);
			if(flag)
			{
				if(dataType.equals(DatasetType.TSV.toString()))
					colNames = Arrays.asList(datas.get(0).split("\t"));
				else
					colNames = Arrays.asList(datas.get(0).split(","));
				obj = DataParser.parse(datas.subList(1, datas.size()),dataType,colNames);
			}
			else
			{
				int colSize = 0;
				if(dataType.equals(DatasetType.TSV.toString()))
					colSize = datas.get(0).split("\t").length;
				else
					colSize = datas.get(0).split(",").length;
				for(int i = 1 ; i<= colSize;i++)
					colNames.add("Col"+i);
				obj = DataParser.parse(datas,dataType,colNames);
			}
		}
		StringBuffer buf = new StringBuffer();
		buf.append("Column:");
		for(String colName : colNames)
		{
			String value = DataTools.getJSONValue(obj.get(0).get(colName).toString());
			if(DataTools.isNumeric(value))
				columns.put(colName, 1);
			else
				columns.put(colName, 0);
			buf.append(colName+","+columns.get(colName));
		}
		logger.info(buf.toString());
		return columns;
	}
	/**
	 * Get json data size
	 * 
	 * @param dataPath data hdfs path
	 * @return
	 */
	public int getDataRows(String dataPath,String dataType) {
		// TODO Auto-generated method stub
		List<String> datas = readAllData(dataPath,false); // read all data
		if(DataParser.haveColumn(datas, dataType))
			return datas.size()-1;
		else
			return datas.size();
	}
	/**
	 * Read data by page from hdfs
	 * 
	 * @param path  data path 
	 * @param start  start position
	 * @param count
	 * @return
	 * @throws Exception
	 */
	private List<String>  readPageData(String path,int start, int count) 
	{
		/**
		 * If the src_uri is existed，read the file directly. If not, visit DATASET_PATH directory corresponding uuid file actually. This is the default uuid file saved address
		 */
		try {
			if (HDFSIO.exist(path))
				return HDFSIO.readDataForPage(path, start, count);
			String uuid = path.replaceAll(".*/", "");
			return HDFSIO
					.readDataForPage(Constants.DATASET_PATH + "/" + uuid + "/" + uuid, start,count);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Read all data by page
	 * 
	 * @param path  data path 
	 * @param limit  if limit line
	 * @return
	 * @throws Exception
	 */
	private List<String>  readAllData(String path,boolean limit) 
	{
		/**
		 * If the src_uri is existed，read the file directly. If not, visit DATASET_PATH directory corresponding uuid file actually. This is the default uuid file saved address
		 */
		try {
			if (HDFSIO.exist(path))
				return HDFSIO.readAllData(path,limit);
			String uuid = path.replaceAll(".*/", "");
			return HDFSIO
					.readAllData(Constants.DATASET_PATH + "/" + uuid + "/" + uuid,limit);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Keep three digit number
	 * 
	 * @param value
	 * @return
	 */
	private  double m3(double value)
	{
		DecimalFormat df = new DecimalFormat("#.000");
		return Double.valueOf(df.format(value));
	}

}
