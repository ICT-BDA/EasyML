/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.visual.restful;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import eml.studio.server.visual.charts.BarChart;
import eml.studio.server.visual.charts.LineChart;
import eml.studio.server.visual.charts.ScatterChart;
import eml.studio.server.visual.service.BarChartService;
import eml.studio.server.visual.service.ChartService;
import eml.studio.server.visual.service.LineChartService;
import eml.studio.server.visual.service.ScatterChartService;
import eml.studio.server.visual.service.freqBarChartService;
/**
 * EML visualization for charts restful api service
 */
@Path("charts")
public class ChartsResource {
	@POST
	@Path("/drawfreqBar")
	@Produces(MediaType.APPLICATION_JSON)
	public BarChart drawBar(@FormParam("filePath") String path, @FormParam("dataType") String type,@FormParam("columns") List<String> columns, @FormParam("selectedCol") String selCol,@FormParam("bins") int binSize,@FormParam("logScale") boolean logScale) throws Exception{
		freqBarChartService freqbarchartService = new freqBarChartService();
		BarChart freqBarChart = freqbarchartService.freqBarChartGen(path, type, columns, selCol, binSize, logScale);
		return freqBarChart;
	}
	
	@POST
	@Path("/drawLine")
	@Produces(MediaType.APPLICATION_JSON)
	public List<LineChart> drawLine(@FormParam("filePath") String path, @FormParam("dataType") String type,@FormParam("columns") List<String> columns, @FormParam("selectedCol") List<String> selCols) throws Exception{
		LineChartService linechartService = new LineChartService();
		List<LineChart> lineCharts = linechartService.LineChartGen(path, type, columns, selCols);
		return lineCharts;
	}
	
	@POST
	@Path("/drawScatter")
	@Produces(MediaType.APPLICATION_JSON)
	public ScatterChart drawScatter(@FormParam("filePath") String path, @FormParam("dataType") String type,@FormParam("columns") List<String> columns, @FormParam("selectedCol_x") String selCol_x, @FormParam("logScale_x") boolean logScale_x, @FormParam("selectedCol_y") String selCol_y, @FormParam("logScale_y") boolean logScale_y, @FormParam("selectedCol_c") String selCol_c) throws Exception{
		ScatterChartService scatterchartService = new ScatterChartService();
		ScatterChart scatterChart = scatterchartService.ScatterChartGen(path, type, columns, selCol_x, logScale_x, selCol_y, logScale_y, selCol_c);
		return scatterChart;
	}
	
	@POST
	@Path("/drawBar")
	@Produces(MediaType.APPLICATION_JSON)
	public BarChart drawBar(@FormParam("filePath") String path, @FormParam("dataType") String type,@FormParam("columns") List<String> columns, @FormParam("groupByCol") String gByCol, @FormParam("valueCol") String valCol, @FormParam("aggCol") String aggCol,@FormParam("logScale") boolean logScale) throws Exception{
		BarChartService barchartService = new BarChartService();
		BarChart barChart = barchartService.barChartGen(path, type, columns, gByCol, valCol, aggCol,logScale);
		return barChart;
	}
	
	@POST
	@Path("/getChartParams")
	@Produces(MediaType.APPLICATION_JSON)
	public String getChartParams(@FormParam("filePath") String path, @FormParam("dataType") String type,@FormParam("columns") List<String> columns,@FormParam("chartType")String chartType) throws Exception{
		ChartService chartService = new ChartService();
		String paramsStr = chartService.getChartParams(chartType);
		JSONObject paramObj = JSON.parseObject(paramsStr);
		JSONArray controlArr = paramObj.getJSONArray("controls");
		System.out.println(controlArr.toJSONString());
		for(int i=0;i<controlArr.size();i++)
		{
			JSONObject conObj = controlArr.getJSONObject(i);
			String contType = conObj.getString("control_type");
			if(contType.equals("select"))
			{
				String value = conObj.getString("value");
				if(value.equals(""))
				{
					StringBuffer buf = new StringBuffer();
					for(int j=0;j<columns.size();j++)
					{
						if(j == columns.size()-1)
							buf.append(columns.get(j));
						else
							buf.append(columns.get(j)+",");
					}
					conObj.put("value", buf.toString());
					controlArr.remove(i);
					controlArr.add(i, conObj);
				}
			}
		}
		paramObj.put("controls", controlArr);
		System.out.println(paramObj.toJSONString());
		return paramObj.toJSONString();
	}
}
