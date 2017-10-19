/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.script;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Run script object class
 */
public class Script implements IsSerializable{
	String path;
	String startShellPath;
	String value;
	Integer inputCount;
	Integer outputCount;
	public String getPath() {
		return path;
	}
	public String getValue() {
		return value;
	}
	public Integer getInputCount() {
		return inputCount;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setInputCount(Integer inputCount) {
		this.inputCount = inputCount;
	}
	public Integer getOutputCount() {
		return outputCount;
	}
	public void setOutputCount(Integer outputCount) {
		this.outputCount = outputCount;
	}
	public String getStartShellPath() {
		return startShellPath;
	}
	public void setStartShellPath(String startShellPath) {
		this.startShellPath = startShellPath;
	}
}
