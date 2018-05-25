/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.visual.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import eml.studio.server.constant.Constants;
import eml.studio.server.util.HDFSIO;

/**
 *  Base chart  service implementation
 */
public class ChartService {
	
	/**
	 * Read all data by page
	 * 
	 * @param path  data path 
	 * @return
	 * @throws Exception
	 */
	protected List<String>  readAllData(String path, boolean limit) 
	{
		try {
			if (HDFSIO.exist(path))
				return HDFSIO.readAllData(path,limit);
			String uuid = path.replaceAll(".*/", "");
			return HDFSIO
					.readAllData(Constants.DATASET_PATH + "/" + uuid + "/" + uuid,limit);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Keep three digit number
	 * 
	 * @param value
	 * @return
	 */
	protected  double m3(double value)
	{
		DecimalFormat df = new DecimalFormat("#.000");
		return Double.valueOf(df.format(value));
	}

	/**
	 * Get chart params templete
	 * 
	 * @param chartType
	 * @return
	 */
	public String getChartParams(String chartType)
	{
		String filePath = "/chartparams/"+chartType.toLowerCase()+".json";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		StringBuffer jsonBuf = new StringBuffer();
		BufferedReader reader=null;
        try{
            InputStreamReader in = new InputStreamReader(classLoader.getResourceAsStream(filePath),"UTF-8");
            reader=new BufferedReader(in);
            String tmpStr=null;
            while((tmpStr=reader.readLine())!=null){
                jsonBuf.append(tmpStr);
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader!=null){
                try{
                    reader.close();
                }catch(IOException el){
                }  
            }  
        }
        return jsonBuf.toString();
	}
	
	/**
	 * Initial column data list 
	 * 
	 * @param selectedColsDatas   selected column datas list
	 * @param selectedCols  selected column list
	 */
	public void initiaColResults(List<List<String>> selectedColsDatas, List<String> selectedCols)
	{
		for(String selectedCol : selectedCols)
			selectedColsDatas.add(new ArrayList<String>());
	}
}
