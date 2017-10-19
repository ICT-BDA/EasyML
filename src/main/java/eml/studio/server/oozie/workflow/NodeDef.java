/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.oozie.workflow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

/**
 * A NodeDef is a finest element of Workflow DAG
 */
public abstract class NodeDef {
	/**
	 * The set of in-going nodes, which is determined by the input files dependencies
	 */
	protected Set<NodeDef> inNodes;
	/**
	 * The set of out-going nodes, which is determined by the output files dependencies
	 */
	protected Set<NodeDef> outNodes;

	/**
	 * Specifies the input file dependencies
	 */
	protected Map<Integer, String> inputFiles;

	/**
	 * Specifies the output file dependencies
	 */
	protected Map<Integer, String> outputFiles;
	private String name;

	protected NodeDef(String name) {
		this.inNodes = new HashSet<NodeDef>();
		this.outNodes = new HashSet<NodeDef>();
		this.inputFiles = new HashMap<Integer, String>();
		this.outputFiles = new HashMap<Integer,String>();
		this.name = name;
	}

	public void addInNode(NodeDef node) {
		inNodes.add(node);
	}

	public void addOutNode(NodeDef node) {
		outNodes.add(node);
	}

	public int getInDegree() {
		return inNodes.size();
	}

	public int getOutDegree() {
		return outNodes.size();
	}

	public Set<NodeDef> getInNodes() {
		return inNodes;
	}

	public Set<NodeDef> getOutNodes() {
		return outNodes;
	}

	public void delOutNode(NodeDef node) {
		outNodes.remove(node);
	}

	public void delInNode(NodeDef node) {
		inNodes.remove(node);
	}

	public void clearInNodes() {
		inNodes.clear();
	}

	public void clearOutNodes() {
		outNodes.clear();
	}

	public abstract void append2XML(Element root);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void putInputFile(int port, String file){
		this.inputFiles.put(port, file);
	}

	public void putOutputFile(int port, String file){
		this.outputFiles.put(port, file);
	}
}
