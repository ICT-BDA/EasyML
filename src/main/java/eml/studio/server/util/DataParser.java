/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.util;

import java.util.ArrayList;
import java.util.List;

import eml.studio.shared.util.DataTools;
import eml.studio.shared.util.DatasetType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Data format parser, to parse json or csv or tsv data to json 
 */
public class DataParser {
	/**
	 * Parse json string data to json object
	 * 
	 * @param datas  json string data list
	 * @return
	 */
	public static List<JSONObject>  parseJsonToJson(List<String> datas)
	{
		List<JSONObject> results = new ArrayList<JSONObject>();
		for(String data : datas)
		{
			if(data == null || data.equals(""))
			{
				results.add(null);
				continue;
			}
			JSONObject obj = JSON.parseObject(data);
			results.add(obj);
		}
		return results;
	}

	/**
	 * Parse csv data to json object
	 * 
	 * @param datas  csv data list
	 * @param columns   csv data column
	 * @return
	 */
	public static List<JSONObject> parseCsvToJson(List<String> datas,List<String> columns)
	{
		int startIndex = 0;
		List<JSONObject> results = new ArrayList<JSONObject>();
		for(int i = startIndex ; i<datas.size(); i++)
		{
			if(datas.get(i) == null || datas.get(i).equals(""))
			{
				results.add(null);
				continue;
			}
			String[] colDatas = datas.get(i).split(",");
			if(columns.get(0).equals(colDatas[0]))
				continue;
			JSONObject obj = new JSONObject();
			for(int k=0;k<colDatas.length;k++)
			{
				String colName = columns.get(k);
				String colValue = colDatas[k];
				obj.put(colName, colValue);
			}
			results.add(obj);
		}
		return results;
	}

	/**
	 * Parse tsv data to json object
	 * 
	 * @param datas  tsv data list
	 * @param columns   tsv data column
	 * @return
	 */
	public static List<JSONObject> parseTsvToJson(List<String> datas,List<String> columns)
	{
		int startIndex = 0;
		List<JSONObject> results = new ArrayList<JSONObject>();
		for(int i = startIndex ; i<datas.size(); i++)
		{
			if(datas.get(i) == null || datas.get(i).equals(""))
			{
				results.add(null);
				continue;
			}
			String[] colDatas = datas.get(i).split("\t");
			JSONObject obj = new JSONObject();
			for(int k=0;k<colDatas.length;k++)
			{
				String colName = columns.get(k);
				String colValue = colDatas[k];
				obj.put(colName, colValue);
			}
			results.add(obj);
		}
		return results;
	}

	/**
	 * Data format conversion
	 * 
	 * @param datas   Source data
	 * @param dataType   Source data type
	 * @param columns Source data columns
	 * @return
	 */
	public static List<JSONObject>  parse(List<String> datas, String dataType,List<String>columns)
	{
		if(dataType.equals(DatasetType.JSON.toString()))
			return parseJsonToJson(datas);
		else if(dataType.equals(DatasetType.CSV.toString()))
			return parseCsvToJson(datas,columns);
		else if(dataType.equals(DatasetType.TSV.toString()))
			return parseTsvToJson(datas,columns);
		else
			return null;
	}


	/**
	 * Determine wheather the csv or tsv file have columns（If the first line is not numberic, then we consider the it as the data column name ）
	 * 
	 * @param datas
	 * @param dataType
	 * @return
	 */
	public static boolean haveColumn(List<String> datas,String dataType)
	{
		String data = datas.get(0);
		String[] columns = null;
		if(dataType.equals(DatasetType.CSV.toString()))
			columns = data.split(",");
		else if(dataType.equals(DatasetType.TSV.toString()))
			columns = data.split("\t");
		if(columns == null)
			return false;
		else
		{
			boolean flag = true;
			for(String column : columns)
			{
				if(DataTools.isNumeric(column))
				{
					flag = false;
					break;
				}
			}
			return flag;
		}
	}
}
