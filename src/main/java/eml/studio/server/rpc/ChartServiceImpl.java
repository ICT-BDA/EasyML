/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eml.studio.client.rpc.ChartService;
import eml.studio.server.constant.Constants;
import eml.studio.server.statics.ThreadDataParser;
import eml.studio.server.statics.ThreadUtils;
import eml.studio.server.util.HDFSIO;
import eml.studio.shared.util.DataTools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Specific methods in Draw data chart related RemoteServiceServlet
 */
public class ChartServiceImpl extends RemoteServiceServlet implements ChartService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ChartServiceImpl.class.getName());
	private static final int SPACE = 500;

	@Override
	public String barChartServer(Boolean name,String[] labels, double[] datas) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String color = "rgb(186,73,46)";
		String title = "";

		GsonOption option = new GsonOption();
		option.title(title); 
		option.toolbox().show(true).feature(
				Tool.dataView,
				new MagicType(Magic.line, Magic.bar),
				Tool.restore
				);
		option.tooltip().show(true).formatter("{a} <br/>{b} : {c}");
		option.legend(title);

		Bar bar = new Bar(title);
		CategoryAxis category = new CategoryAxis();// Axis classification
		category.data(labels);

		for (int i = 0; i < labels.length; i++) {
			double data = datas[i];
			Map<String, Object> map = new HashMap<String, Object>(2);
			map.put("value", data);
			map.put("itemStyle", new ItemStyle().normal(new Normal().color(color)));
			bar.data(map);
		}

		if (name) {
			option.xAxis(category);
			option.yAxis(new ValueAxis());
		} else {
			option.xAxis(new ValueAxis());
			option.yAxis(category);
		}

		option.series(bar); 
		return option.toString();
	}

	@Override
	public String barChartServer(String filePath, String dataType, List<String> columns, String columnName, int binSize, boolean logScale)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		List<String> results = readAllData(filePath,true); //only read part data

		// If the data include column data, should remove it
		if(!columns.get(0).contains("Col"))
			results = results.subList(1, results.size());

		// Use multi-thread to data analysis and get selected column data
		List<String> datas = new ArrayList<String>();
		ExecutorService exec = Executors.newCachedThreadPool();
		int threadNum = ThreadUtils.THREAD_NUM;
		int countNum = (int)Math.ceil(results.size() / threadNum);
		for(int i=0; i<threadNum; i++){ 
			int startInx = i*countNum;
			int endInx = startInx + countNum;
			if(i == threadNum-1)
				endInx = results.size();
			exec.execute(new ThreadDataParser(results.subList(startInx, endInx),columnName,columns,dataType,datas));
		} 
		exec.shutdown();

		// wait until sum-process is over 
		try {
			exec.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.info("exec.awaitTermination(1, TimeUnit.HOURS) occurs error : " + e);
		}
		logger.info("【Chart】Parse data column size = " + datas.size());

		//Get data number type（Numberic or String）
		String verifyTypeData = null;
		for(int i =0;i<datas.size();i++)
		{
			if(datas.get(i)==null || datas.get(i).trim().equals(""))
				continue;
			else
			{
				verifyTypeData = datas.get(i);
				break;
			}
		}
		Map<String,Integer> labelCounts = new HashMap<String,Integer>();
		boolean numeric = DataTools.isNumeric(verifyTypeData);

		if(numeric)
		{
			logger.info("Numeric data chart service");
			double max = Double.MIN_VALUE;
			double min = Double.MAX_VALUE;
			for(String data : datas)
			{
				if(data == null || "".equals(data.trim()))
					continue;
				double value = Double.valueOf(data);
				if(value > max)
					max = value;
				if(value < min)
					min = value;
			}
			int range = (int) Math.ceil(((max - min)/(binSize*2)));
			int k = 2;
			while(range>SPACE)  // If interval over 500, increase the number of boxes
			{
				k = k+1;
				range = (int) Math.ceil(((max - min)/(binSize*k)));
			}
			if(range>1)
			{
				logger.info("Range segment, range = "+range);
				int[] bags = new int[binSize*2];
				bags[0] = (int)min + range;
				//Build a drawer for data statistics
				for(int i=1;i<bags.length;i++)
				{
					bags[i] = bags[i-1]+range;
				}
				logger.info("min="+min+"; max="+max+"; data bags=");
				for(String data : datas)
				{
					if(data == null || "".equals(data.trim()))
						continue;
					else
					{
						int bottom = (int) Math.floor(min);   // Lower bound
						int upper = (int) Math.ceil(max); // Upper bound
						for(int i=0;i<bags.length;i++)
						{
							double dataT = Double.valueOf(data);
							if(i == 0)
							{
								bottom = (int) Math.floor(min);
								upper = bags[i];
							}else{
								bottom = bags[i-1];
								upper = bags[i];
							}
							if(i == bags.length - 1)  //The last drawer is closed for left and right
							{
								if(dataT >=bottom && dataT<=upper)
								{
									String key = "["+bottom+","+upper+"]";
									if(labelCounts.containsKey(key))
										labelCounts.put(key, labelCounts.get(key)+1);
									else
										labelCounts.put(key, 1);
									break;
								}
							}else{ // The middle drawer is closed for left and opened for right
								if(dataT >=bottom && dataT<upper)
								{
									String key = "["+bottom+","+upper+")";
									if(labelCounts.containsKey(key))
										labelCounts.put(key, labelCounts.get(key)+1);
									else
										labelCounts.put(key, 1);
									break;
								}
							}
						}
					}
				}

			}
			else //If the value is small, process as discrete
			{
				logger.info("Numeric data dispersed chart service");
				for(String data : datas)
				{
					if(data == null || "".equals(data.trim()))
						continue;
					else
					{
						if(labelCounts.containsKey(data))
						{
							int count = labelCounts.get(data)+1;
							labelCounts.put(data, count);
						}else{
							labelCounts.put(data, 1);
						}
					}
				}
			}

		}else{  //String process
			logger.info("String data chart service");
			for(String data : datas)
			{
				if(data == null || "".equals(data.trim()))
					continue;
				else
				{
					if(labelCounts.containsKey(data))
					{
						int count = labelCounts.get(data)+1;
						labelCounts.put(data, count);
					}else{
						labelCounts.put(data, 1);
					}
				}
			}
		}

		//Sort the label frequency in descending order
		List<Map.Entry<String,Integer>> mapList = new ArrayList<Map.Entry<String,Integer>>(labelCounts.entrySet());
		Collections.sort(mapList,new Comparator<Map.Entry<String,Integer>>() {
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				return o2.getValue()- o1.getValue();
			}

		});

		//Get the top binSize value by frequency
		if(mapList.size()<binSize)
			binSize = mapList.size();

		String[] labels = new String[binSize];
		double[] values = new double[binSize];
		for(int i = 0; i<binSize;i++)
		{
			labels[i] = mapList.get(i).getKey();
			if(logScale)
				values[i] = Math.log(Double.valueOf(mapList.get(i).getValue()));
			else
				values[i] = mapList.get(i).getValue();
		}

		StringBuffer labelBuf = new StringBuffer();
		StringBuffer valueBuf = new StringBuffer();
		for(int i=0;i<binSize; i++)
		{
			labelBuf.append(labels[i]+"  ");
			valueBuf.append(values[i]+"  ");
		}
		logger.info("x_axis:"+labelBuf.toString());
		logger.info("y_axis:"+valueBuf.toString());
		return barChartServer(true,labels,values);
	}

	/**
	 * Read all data by page
	 * 
	 * @param path  data path 
	 * @return
	 * @throws Exception
	 */
	private List<String>  readAllData(String path, boolean limit) 
	{
		/**
		 * If the src_uri is existed，read the file directly. If not, visit DATASET_PATH directory corresponding uuid file actually. This is the default uuid file saved address
		 */
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
}
