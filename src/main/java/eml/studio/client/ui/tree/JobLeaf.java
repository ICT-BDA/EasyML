/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import eml.studio.client.mvp.AppController;
import eml.studio.client.ui.menu.JobAddExampleMenu;
import eml.studio.client.ui.menu.JobDeleteMenu;
import eml.studio.shared.model.BdaJob;

/**
 * TreeMenu Leaf for Jobs
 */
public class JobLeaf extends Leaf<BdaJob> {

	public JobLeaf(BdaJob job) {
		super(job.getCurOozJob().getAppName(), job);
		initStyle(job);
		if(AppController.power.equals("111")){
			this.addMenuItem(JobDeleteMenu.create(this));
		}

	}

	public JobLeaf(BdaJob job,String power) {
		super(job.getCurOozJob().getAppName(), job);
		this.addMenuItem(JobDeleteMenu.create(this));
		if(power.equals("111")){
			this.addMenuItem(JobAddExampleMenu.create(this));
		}
		initStyle(job);
	}
	/** Set the tyle of jobEntity leaf node */
	private void initStyle(BdaJob job) {
		if ("SUCCEEDED".equals(job.getCurOozJob().getStatus())) {
			this.label.setStyleName("bda-treeleaf-finish");
		} else if ("KILLED".equals(job.getCurOozJob().getStatus())) {
			this.label.setStyleName("bda-treeleaf-fail");
		} else if ("FAILED".equals(job.getCurOozJob().getStatus())) {
			this.label.setStyleName("bda-treeleaf-fail");
		} else {
			this.label.setStyleName("bda-treeleaf-running");
		}
	}
}
