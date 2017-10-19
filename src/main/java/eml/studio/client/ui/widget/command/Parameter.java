/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.command;

/** 
 * Program Parameter in String format 
 */
public class Parameter {

	private int index;
	private String paramName;
	private String paramType;

	private String minValue;
	private String maxValue;
	private String paramValue;

	public Parameter(int id, String name, String type, String value) {
		setIndex(id);
		setParamName(name);
		setParamType(type);
		setParamValue(value);
	}

	public Parameter(){

	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int id) {
		this.index = id;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String min) {
		this.minValue = min;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String max) {
		this.maxValue = max;
	}

	public String getParaName() {
		return paramName;
	}

	public void setParamName(String paraName) {
		this.paramName = paraName;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String value) {
		this.paramValue = value;
	}

}
