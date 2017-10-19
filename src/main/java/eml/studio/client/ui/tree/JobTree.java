/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import java.util.logging.Logger;

import eml.studio.client.mvp.AppController;
import eml.studio.client.util.Constants;
import eml.studio.shared.model.BdaJob;

import com.google.gwt.user.client.ui.TreeItem;


/**
 * Job Menus for each user
 */
public class JobTree extends BaseTree {
	private static Logger logger = Logger.getLogger(JobTree.class.getName());
	// show example jobs
	TreeItem exampleRoot = null;

	// show my jobs
	TreeItem myRoot = null;
	TreeItem myFinishRoot = null;
	TreeItem myRunningRoot = null;

	// other jobs
	TreeItem otherRoot = null;
	TreeItem otherFinishRoot = null;
	TreeItem otherRunningRoot = null;

	public JobTree() {
		exampleRoot = this.addTextItem(Constants.studioUIMsg.examples());
		myRoot = this.addTextItem(Constants.studioUIMsg.myJobs());
		otherRoot = this.addTextItem(Constants.studioUIMsg.otherJobs());

		exampleRoot.setState(true);
		myRoot.setState(true);
		otherRoot.setState(true);
		this.setSelectedItem(myRoot);

		myFinishRoot = myRoot.addTextItem(Constants.studioUIMsg.finished());
		myRunningRoot = myRoot.addTextItem(Constants.studioUIMsg.running());

		otherFinishRoot = otherRoot.addTextItem(Constants.studioUIMsg.finished());
		otherRunningRoot = otherRoot.addTextItem(Constants.studioUIMsg.running());
	}

	/** Return the menus for the example jobs */
	public TreeItem getExampleTree() {
		return exampleRoot;
	}

	/** Return the menus for private jobs */
	public TreeItem getMyTree() {
		return myRoot;
	}

	/** Return the menus for other jobs */
	public TreeItem getOtherTree() {
		return otherRoot;
	}

	/** Add a example job to the menu */
	public void addExampleJob(BdaJob job) {
		JobLeaf node = new JobLeaf(job);
		exampleRoot.addItem(node);
		exampleRoot.setState(true);
	}

	/** Add a example job to the menu */
	public void addMyJob(BdaJob job) {
		JobLeaf node = new JobLeaf(job, AppController.power);
		if (job.getCurOozJob().getStatus().equals("RUNNING")) {
			myRunningRoot.addItem(node);
			myRunningRoot.setState(true);
		} else {
			myFinishRoot.addItem(node);
			myFinishRoot.setState(true);
		}
	}

	/** Add a other job to the menu */
	public void addOtherJob(BdaJob job) {
		if( job.getCurOozJob() == null ){
			return ;
		}
		JobLeaf node = new JobLeaf(job,AppController.power);
		if (job.getCurOozJob().getStatus().equals("RUNNING")) {
			otherRunningRoot.addItem(node);
			otherRunningRoot.setState(true);
		} else {
			otherFinishRoot.addItem(node);
			otherFinishRoot.setState(true);
		}
	}



	/** Return the menus for private running jobs */
	public TreeItem getMyRunningTree() {
		return myRunningRoot;
	}

	/** Return the menus for private finished jobs */
	public TreeItem getMyFinishTree() {
		return myFinishRoot;
	}

	/** Return the menus for other running jobs */
	public TreeItem getOtherRunningTree() {
		return otherRunningRoot;
	}

	/** Return the menus for other finished jobs */
	public TreeItem getOtherFinishTree() {
		return otherFinishRoot;
	}

}