/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.visual.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.alibaba.fastjson.JSONObject;

import eml.studio.server.visual.service.DataAnalysisService;
import eml.studio.shared.util.DataFeature;

/**
 * BDA visualization for statistics restful api service
 *
 */
@Path("statics")
public class StaticsResource {
	
	@POST
	@Path("/getAllCols")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getAllCols(@FormParam("filePath") String path, @FormParam("dataType") String type) throws Exception{
		DataAnalysisService dataAnalysis = new DataAnalysisService();
		HashMap<String,Integer> dataColMap = dataAnalysis.getDataColumn(path, type);
		List<String> dataCols = new ArrayList<String>();
		dataCols.addAll(dataColMap.keySet());
		return dataCols;
	}

	@POST
	@Path("/getColFeature")
	@Produces(MediaType.APPLICATION_JSON)
	public DataFeature getColFeature(@FormParam("filePath") String path, @FormParam("dataType") String type, @FormParam("columns") List<String> columns, @FormParam("selectedCol") String selCol) throws Exception{
		DataAnalysisService dataAnalysis = new DataAnalysisService();
		DataFeature feature = dataAnalysis.extDataFeature(path, type, columns, selCol);
		return feature;
	}

	@POST
	@Path("/getTotalSize")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer getTotalRows(@FormParam("filePath") String path, @FormParam("dataType") String type, @FormParam("columns") List<String> columns, @FormParam("selectedCol") String selCol) throws Exception{
		DataAnalysisService dataAnalysis = new DataAnalysisService();
		int rows = dataAnalysis.getDataRows(path, type);
		return rows;
	}

	@GET
	@Path("/getDataTable")
	@Produces(MediaType.APPLICATION_JSON)
	public String getOnePageData(
			@Context HttpServletRequest request) throws Exception {
		//User params
		String path = request.getParameter("filePath");		
		String type = request.getParameter("dataType");
		int totalSize = Integer.valueOf(request.getParameter("totalSize"));

		//Datatable default params for page data
		int draw = Integer.valueOf(request.getParameter("draw"));
		int start = Integer.valueOf(request.getParameter("start"));
		int length = Integer.valueOf(request.getParameter("length"));

		//Get page data, the data result is a json string list
		DataAnalysisService dataAnalysis = new DataAnalysisService();
		HashMap<String,Integer> dataColMap = dataAnalysis.getDataColumn(path, type);
		List<String> dataCols = new ArrayList<String>();
		dataCols.addAll(dataColMap.keySet());
		ArrayList<String> pageData = dataAnalysis.pageData(path, type, dataCols, start, length);

		//Transfer to datatable response data
		int recordsTotal = totalSize;
		int recordsFiltered = totalSize;
		JSONObject result= new JSONObject();  
		result.put("draw", draw);
		result.put("recordsTotal", recordsTotal);
		result.put("recordsFiltered", recordsFiltered);

		int pageRow = pageData.size();
		int colSize= dataCols.size();

		String[][] finalData = new String[pageRow][colSize];
		for(int i =0; i<pageRow; i++)
		{
			for(int j = 0;j<colSize; j++)
			{
				JSONObject  json = JSONObject.parseObject(pageData.get(i));
				finalData[i][j]=json.getString(dataCols.get(j)); //Get column data by column name
			}
		}
		
		result.put("data", finalData);
		return result.toJSONString();
	}
}
