/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import java.util.List;
import java.util.logging.Logger;

import eml.studio.client.rpc.DatasetService;
import eml.studio.client.rpc.DatasetServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Preview the results tree in the pop-up tree
 */
public class PopupRetDirTreeLoad {
	protected static Logger logger = Logger.getLogger(PopupRetDirTreeLoad.class.getName());
	protected static DatasetServiceAsync datasetSrv = GWT.create(DatasetService.class);

	/**
	 * Load the results from HDFS to preview the directory tree
	 * 
	 * @param path   The current result previews id
	 * @return   dir tree
	 */
	public static PopupRetDirTree load(final String path)
	{
		logger.info("Popup result directory tree begin");
		PopupRetDirTree retDirTree = new PopupRetDirTree(path);
		return retDirTree;
	}
	/**
	 * Expand the child node of a node in the tree
	 * 
	 * @param cur Need to expand the nodes of the tree
	 */
	public static void wrapTreeNode(final PopupRetDirLeaf cur)
	{
		final String curPath = cur.getPath();
		if(cur.getChildCount()==0) //If the current node has no children, expand
		{
			datasetSrv.isDirectory(curPath, new AsyncCallback<Boolean>() { 
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Expand the directory tree, access the Hdfs results directory failed:"+curPath);
				}

				@Override
				public void onSuccess(Boolean isDir ) {
					if(isDir)//If the current file is a directory, get the file directory
					{
						logger.info("Path:"+curPath+" is directory");
						datasetSrv.getDirFilesPath(curPath, new AsyncCallback<List<String>>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Expand the directory tree, access the list of files in the Hdfs directory："+curPath);
							}

							@Override
							public void onSuccess(List<String> fileList) {
								// TODO Auto-generated method stub
								logger.info("Directory has "+fileList.size()+" files");
								for(String file : fileList)
								{
									final String filePath = curPath+"/"+file;
									final PopupRetDirLeaf leaf = new PopupRetDirLeaf(filePath);
									leaf.setText(file);
									datasetSrv.isDirectory(filePath, new AsyncCallback<Boolean>() {
										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Expand the directory tree and access the Hdfs results directory failed："+filePath);
										}

										@Override
										public void onSuccess(Boolean result) {
											// TODO Auto-generated method stub
											if(!result)
											     leaf.getLabel().addStyleName("bda-treeleaf-popup");
										}
									});
									cur.addItem(leaf);
								}
								cur.setState(cur.getState());
							}
						});
					}
				}
			});
		}
	}

	/**
	 * Rebuild the directory tree
	 * @param tree  directory tree
	 * @param rootPath  root path
	 */
	public static void reBuild(PopupRetDirTree tree,String rootPath)
	{
		logger.info("Popup result directory tree rebuild begin");
		PopupRetDirLeaf root = new PopupRetDirLeaf(rootPath);
		tree.setRoot(root);
		tree.addItem(root);
	}
}
