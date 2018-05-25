/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.visual.charts;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Scatter Chart Object 
 */
public class ScatterChart implements IsSerializable{
	private String[] values; //series
	private String[] colors;
	private double min;
	private double max;
	public ScatterChart(){};
	public ScatterChart(String[] colors, String[] values) //color column is String 
	{
		this.setColor(colors);
		this.setValues(values);
	};
	public ScatterChart(double min,double max, String[] values) //color column is numberic
	{
		this.setMin(min);
		this.setMax(max);
		this.setValues(values); 
	}

	
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}
	
	public String[] getColor() {
		return colors;
	}
	public void setColor(String[] colors) {
		this.colors = colors;
	}
	
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}

}
