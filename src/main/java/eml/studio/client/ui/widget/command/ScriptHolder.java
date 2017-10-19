/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.command;

import java.util.ArrayList;
import java.util.List;

public class ScriptHolder {

	private int index;
	private String name;
	private String scriptName;


	private String value = "";
	private List<String> inFilePaths = new ArrayList<String>();
	private List<String> outFilePaths = new ArrayList<String>();

	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addInFilePath(String path) {
		inFilePaths.add(path);
	}

	public void addOutFilePath(String path) {
		outFilePaths.add(path);
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public List<String> getInFilePaths(){
		return inFilePaths;
	}


	public List<String> getOutFilePaths(){
		return outFilePaths;
	}

}
