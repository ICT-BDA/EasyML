/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import java.util.LinkedHashMap;
import java.util.List;

import eml.studio.shared.util.DataFeature;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataAnalysisServiceAsync {
	void extDataFeature(String dataPath,String dataType, List<String> columns, String columnName, AsyncCallback<DataFeature> callback)
			throws IllegalArgumentException;

	void  pageData(String dataPath, String dataType,List<String>columns, int start, int size,  AsyncCallback<List<String>> callback);

	void  getDataColumn(String dataPath, String dataType, AsyncCallback<LinkedHashMap<String,Integer>> callback);

	void getDataRows(String dataPath, String dataType, AsyncCallback<Integer> callback);
}
