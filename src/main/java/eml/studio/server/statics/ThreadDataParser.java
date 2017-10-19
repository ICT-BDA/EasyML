/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.statics;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.alibaba.fastjson.JSONObject;
import eml.studio.server.util.DataParser;

/**
 * Multithreading Data Parser, use multi thread to parse big dataset format
 */
public class ThreadDataParser implements Runnable {

	private static final Logger logger = Logger.getLogger(ThreadDataParser.class.getName());

	static int ThreadNum = 0;

	private List<String> globalDatas;   //Total data
	private List<String> localDatas;  //Every thread process data
	private List<String> columns;  // All data column names
	private String column;  // Compute data column name
	private String dataType; //Data type
	private int ThreadCurrent;  // Current thread id

	/**
	 * Constructor
	 * 
	 * @param localDatas  Thread current process data（for single thread）
	 * @param column  Selected data column
	 * @param dataType  Data type
	 * @param globalDatas  Total data （for all threads）
	 */
	public ThreadDataParser(List<String> localDatas,  String column,  List<String> columns,String dataType, List<String> globalDatas) {
		this.globalDatas = globalDatas;
		this.localDatas = localDatas;
		this.column = column;
		this.dataType = dataType;
		this.columns = columns;
		this.ThreadCurrent = ++ThreadNum;
	}

	public void run() {
		List<JSONObject> objs = DataParser.parse(localDatas,dataType,columns);
		List<String> results = new ArrayList<String>();
		for(JSONObject obj : objs)
		{
			if(obj == null)
				results.add(null);
			else
				results.add(obj.get(column).toString());
		}
		ThreadNum--;	
		synchronized(globalDatas){
			globalDatas.addAll(results);
		}
	}
}
