/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.visual.charts;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Bar Chart Object 
 *
 */
public class BarChart implements IsSerializable{
	private String[] categories; //x-axis
	private double[] values;  //y-axis
	
	public BarChart(){};
	public BarChart(String[] categories,double[] values)
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
	public String[] getCategories() {
		return categories;
	}
	public void setCategories(String[] categories) {
		this.categories = categories;
	}

}
