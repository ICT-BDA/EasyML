/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("chartservice")
public interface ChartService extends RemoteService {
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
	String barChartServer(String filePath, String dataType, List<String> columns, String columnName,int binSize, boolean logScale) throws IllegalArgumentException;

	/**
	 * Get bar chart data option string to draw the chart
	 * 
	 * @param name  if the x_axis and y_axis should be reverse, false is reverse
	 * @param labels  x_axis
	 * @param datas  y_axis
	 * @return
	 * @throws IllegalArgumentException
	 */
	String barChartServer(Boolean name,String[] labels, double[] datas) throws IllegalArgumentException;
}
