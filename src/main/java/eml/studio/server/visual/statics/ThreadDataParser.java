/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.visual.statics;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSONObject;

import eml.studio.server.util.DataParser;

/**
 * Get columns data from hdfs source data which only support json,tsv,csv data
 * Firstly transfer data to json data, then get the selected column data
 * The class is used for data which not require normal order.
 *
 */
public class ThreadDataParser implements Runnable {

	static int ThreadNum = 0;
	private List<List<String>> totalColDatas;   // Selected columns datas list
	private List<List<String>> localColDatas ;
	private List<String> srcDatas;  //Every thread process data, data type is the same as hdfs
	private List<String> columns;  // All data column names
	private List<String> selectedCols;  // Compute data column name
	private String dataType; //Data type

	/**
	 * Constructor
	 * 
	 * @param localDatas  local process data（hdfs source data)
	 * @param column  Selected data column
	 * @param dataType  Data type
	 * @param globalDatas  selected  col data（for all threads, selected col json data）
	 */
	public ThreadDataParser(List<String> srcDatas,  List<String>  selectedCols,  List<String> columns,String dataType, List<List<String>> totalColDatas) {
		this.totalColDatas = totalColDatas;
		this.srcDatas = srcDatas;
		this.selectedCols = selectedCols;
		this.dataType = dataType;
		this.columns = columns;

		localColDatas = new ArrayList<List<String>>();
		for(String col : selectedCols)
		{
			List<String> colData = new ArrayList<String>();
			localColDatas.add(colData);
		}
	}

	public void run() {
		List<JSONObject> objs = DataParser.parse(srcDatas,dataType,columns);

		for(JSONObject obj : objs)
		{
			for(int k =0 ; k < selectedCols.size(); k++)
			{
				if(obj == null)
					localColDatas.get(k).add(null);
				else
				{
					if(obj.get(selectedCols.get(k))!=null)
						localColDatas.get(k).add((obj.get(selectedCols.get(k)).toString()));
					else
						localColDatas.get(k).add(null);
				}
			}

		}
		synchronized(totalColDatas){

			for(int k=0; k <selectedCols.size(); k++)
			{
				totalColDatas.get(k).addAll(localColDatas.get(k));
			}
		}
	}
}
