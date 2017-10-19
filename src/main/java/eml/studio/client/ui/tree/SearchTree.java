/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import com.google.gwt.user.client.ui.TreeItem;
import java.util.logging.Logger;

/**
 * Search result tree
 */
public class SearchTree extends BaseTree {
	private static Logger logger = Logger.getLogger(SearchTree.class.getName());
	private TreeItem programRT = null;
	private TreeItem datasetRT = null;
	private TreeItem jobRT = null;
	public SearchTree(){

	}

	public void addProgramRT(TreeItem leaf) {
		this.programRT.addItem(leaf);
		this.programRT.setState(true);
	}
	public void addDatasetRT(TreeItem leaf) {
		this.datasetRT.addItem(leaf);
		this.datasetRT.setState(true);
	}
	public void addJobRT(TreeItem leaf) {
		this.jobRT.addItem(leaf);
		this.jobRT.setState(true);
	}
	public void clearTree(){
		this.clear();
		programRT = this.addTextItem("Program");
		datasetRT = this.addTextItem("Dataset");
		jobRT = this.addTextItem("Job");
	}
	public TreeItem getProgramRT() {
		return programRT;
	}

	public void setProgramRT(TreeItem programRT) {
		this.programRT = programRT;
	}

	public TreeItem getDatasetRT() {
		return datasetRT;
	}

	public void setDatasetRT(TreeItem datasetRT) {
		this.datasetRT = datasetRT;
	}

	public TreeItem getJobRT() {
		return jobRT;
	}

	public void setJobRT(TreeItem jobRT) {
		this.jobRT = jobRT;
	}
}
