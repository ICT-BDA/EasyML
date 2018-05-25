/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.visual.charts;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Line Chart Object 
 */
public class LineChart implements IsSerializable{
	private int[] categories; //x-axis
	private double[] values;  //y-axis
	
	public LineChart(){};
	public LineChart(int[] categories,double[] values)
	{
		this.setCategories(categories); 
		this.values = values;
	}

	public double[] getValues() {
		return values;
	}
	public void setValues(double[] values) {
		this.values = values;
	}
	public int[] getCategories() {
		return categories;
	}
	public void setCategories(int[] categories) {
		this.categories = categories;
	}

}
