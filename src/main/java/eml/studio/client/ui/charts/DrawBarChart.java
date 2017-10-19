/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.charts;

/**
 *  Draw a histogram by js implementation, use java native method to generate javascript code
 */
public class DrawBarChart{

	/**
	 * Draw a histogram by echart
	 * 
	 * @param chartId   chart id 
	 * @param optionStr  chart option string(use echart option to describe a chart)
	 */
	public static native void drawBarChart(String chartId, String optionStr)/*-{
		var myChart = $wnd.echarts.init($doc.getElementById(chartId));
		$wnd.onresize = myChart.resize;
		option = eval('(' + optionStr + ')');

		var magicTypes = new $wnd.Array();
		var types = option.toolbox.feature.magicType.type;
		for (var i = 0; i < types.length; i++) {
			magicTypes[i] = types[i];
		}
		option.toolbox.feature.magicType.type = magicTypes;

		var xAxisArray = new $wnd.Array();
		for (var i = 0; i < option.xAxis.length; i++) {
			var xAxisObj = option.xAxis[i];
			var xAxisDatas = new $wnd.Array();
			for (var j = 0; j < xAxisObj.data.length; j++) {
				xAxisDatas[j] = xAxisObj.data[j];
			}
			xAxisObj.data = xAxisDatas;
			xAxisArray[i] = xAxisObj;
		}
		option.xAxis = xAxisArray;

		var yAxisArray = new $wnd.Array();
		for (var i = 0; i < option.yAxis.length; i++) {
			yAxisArray[i] = option.yAxis[i];
		}
		option.yAxis = yAxisArray;

		//seris
		var serisArray = new $wnd.Array();
		for (var i = 0; i < option.series.length; i++) {
			var serisObj = option.series[i];
			//series data
			var dataArray = new $wnd.Array();
			for (var j = 0; j < serisObj.data.length; j++) {
				dataArray[j] = serisObj.data[j];
			}
			serisObj.data = dataArray;

			serisArray[i] = serisObj;
		}
		option.series = serisArray;
		console.log(option);
		myChart.setOption(option, true);
	}-*/;

}
