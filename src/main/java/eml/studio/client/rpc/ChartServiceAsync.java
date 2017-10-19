/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ChartServiceAsync {

	void barChartServer(Boolean input, String[] labels, double[] datas, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void barChartServer(String filePath, String dataType, List<String> columns, String columnName, int binSize, boolean logScale, AsyncCallback<String> callback)
			throws IllegalArgumentException;

}
