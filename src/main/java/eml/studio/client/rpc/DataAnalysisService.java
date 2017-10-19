/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import java.util.LinkedHashMap;
import java.util.List;

import eml.studio.shared.util.DataFeature;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("analysisservice")
public interface DataAnalysisService extends RemoteService {
	/**
	 * Extract data list common feature
	 * 
	 * @param dataPath data path on hdfs
	 * @param dataType  data type
	 * @param columns all data columns
	 * @param columnName  select data column
	 * @return
	 */
	DataFeature extDataFeature(String dataPath,String dataType, List<String>columns, String columnName);

	/**
	 * Get page json  string data from data path, a string str is a json string
	 * 
	 * @param dataPath   hdfs data path
	 * @param dataType  data type
	 * @param columns  all data columns
	 * @param start  start index
	 * @param size page size
	 * @return
	 */
	List<String>  pageData(String dataPath,String dataType, List<String>columns, int start, int size);

	/**
	 * Get json data column(key: data column ,  value : data type(String or numberic))
	 * 
	 * @param dataPath data hdfs path
	 * @param dataType  data type
	 * @return
	 */
	LinkedHashMap<String,Integer>  getDataColumn(String dataPath, String dataType);

	/**
	 * Get json data size
	 * 
	 * @param dataPath data hdfs path
	 * @param dataType  data type
	 * @return
	 */
	int  getDataRows(String dataPath,String dataType);

}
