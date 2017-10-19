/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel.Grid;

import eml.studio.client.ui.tree.*;
import eml.studio.shared.model.BdaJob;
import eml.studio.shared.model.Dataset;
import eml.studio.shared.model.Program;

import com.google.gwt.user.client.ui.*;

import java.util.logging.Logger;

/**
 * Search Grid
 */
public class SearchGrid extends Grid {
	protected static Logger logger = Logger.getLogger(SearchGrid.class.getName());
	private TextBox sbox = new TextBox();
	private String keyword;
	private ProgramTree programTree;
	private DatasetTree datasetTree;
	private JobTree jobTree;
	private SearchTree resultTree;

	private void init()
	{
		this.setWidget(0,0,new Label("name: "));
		sbox.setEnabled(true);
		sbox.setStyleName("gwt-SuggestBox");
		this.setWidget(1, 0, sbox);
		SearchListener sl = new SearchListener();
		sbox.addKeyboardListener(sl);
		setResultWidget();
		//this.setWidget(3, 0, programTree);
	}

	public SearchGrid(ProgramTree programTree, DatasetTree datasetTree, JobTree jobTree, SearchTree resultTree){
		super(4, 2);
		this.programTree = programTree;
		this.datasetTree = datasetTree;
		this.jobTree = jobTree;
		this.resultTree = resultTree;

		init();
	}

	private class SearchListener extends KeyboardListenerAdapter {
		@Override
		public void onKeyDown(Widget sender, char key, int mods) {
			if (KeyboardListener.KEY_ENTER == key)
			{
				keyword = sbox.getText();
				resultTree.clearTree();
				getProgramResultree();
				getDatasetResultree();
				getJobResultree();

			}

		}
	}
	private void getProgramResultree(){
		//programTree
		if(programTree.getItemCount()==0) {
		}else{
			for(int i= 0;i<programTree.getItemCount();i++)
			{
				getProgramTreeLeaf(programTree.getItem(i));
			}
		}

	}
	private void getProgramTreeLeaf(TreeItem leaf){
		if(leaf.getChildCount()==0) {
			ProgramLeaf pLeaf = (ProgramLeaf)leaf;
			Program program = pLeaf.getModule();
			logger.info(""+program.getName());
			if(program.getName().contains(keyword)&&(!keyword.equals(""))) {
				resultTree.addProgramRT(new ProgramLeaf(program));
			}
		}else{
			for(int i= 0;i<leaf.getChildCount();i++)
			{
				getProgramTreeLeaf(leaf.getChild(i));
			}
		}
	}
	private void getDatasetResultree(){
		//datasettree
		if(datasetTree.getItemCount()==0) {
		}else{
			for(int i= 0;i<datasetTree.getItemCount();i++)
			{
				getDatasetTreeLeaf(datasetTree.getItem(i));
			}
		}

	}
	private void getDatasetTreeLeaf(TreeItem leaf){
		if(leaf.getChildCount()==0) {
			DatasetLeaf pLeaf = (DatasetLeaf)leaf;
			Dataset dataset = pLeaf.getModule();
			logger.info(""+dataset.getName());
			if(dataset.getName().contains(keyword)&&(!keyword.equals(""))) {
				resultTree.addDatasetRT(new DatasetLeaf(dataset));
			}
		}else{
			for(int i= 0;i<leaf.getChildCount();i++)
			{
				getDatasetTreeLeaf(leaf.getChild(i));
			}
		}
	}
	private void getJobResultree(){
		//JobTree
		if(jobTree.getItemCount()==0) {
		}else{
			for(int i= 0;i<jobTree.getItemCount();i++)
			{
				getJobTreeLeaf(jobTree.getItem(i));
			}
		}
	}
	private void getJobTreeLeaf(TreeItem leaf){
		if(leaf.getChildCount()==0) {
			if(leaf instanceof JobLeaf)
			{
				JobLeaf pLeaf = (JobLeaf)leaf;
				BdaJob job = pLeaf.getModule();
				logger.info(""+job.getJobName());
				if(job.getJobName().contains(keyword)&&(!keyword.equals(""))) {
					resultTree.addJobRT(new JobLeaf(job));
				}
			}
		}else{
			for(int i= 0;i<leaf.getChildCount();i++)
			{
				getJobTreeLeaf(leaf.getChild(i));
			}
		}
	}
	private void setResultWidget(){
		this.setWidget(2,0,resultTree);

	}


}
