/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import eml.studio.client.mvp.AppController;
import eml.studio.client.rpc.CategoryService;
import eml.studio.client.rpc.CategoryServiceAsync;
import eml.studio.client.rpc.DatasetService;
import eml.studio.client.rpc.DatasetServiceAsync;
import eml.studio.client.ui.menu.*;
import eml.studio.shared.model.Category;
import eml.studio.shared.model.Dataset;
import eml.studio.shared.model.Module;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.List;
import java.util.logging.Logger;

/**
 * Load all existing programs from database into Dataset Tree Menu
 */
public class DatasetTreeLoader {
	protected static CategoryServiceAsync categorySrv = GWT.create(CategoryService.class);
	private static Logger logger = Logger.getLogger(DatasetTreeLoader.class.getName());

	/**
	 * TODO: query the programs by users
	 *
	 * @param userEmail used to filter myDatasets
	 */
	public static DatasetTree load(final String userEmail) {
		final DatasetTree tree = new DatasetTree();
		DatasetServiceAsync svc = GWT.create(DatasetService.class);
		svc.load(new AsyncCallback<List<Dataset>>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.info("Failed to load data component:" + caught.getMessage());
			}

			@Override
			public void onSuccess(List<Dataset> result) {
				for (Dataset m : result) {
					if (!m.getDeprecated()) {
						DatasetLeaf node = new DatasetLeaf(m);
						addDatasetLeaf(tree, node, userEmail);
						addContextMenu(tree,node);
					}
				}
			}
		});
		return tree;
	}

	/** Add a program into the menu */
	public static void addDatasetLeaf(final DatasetTree tree,
			final DatasetLeaf node,
			String userEmail) {
		Module m = node.getModule();
		String category = m.getCategory();
		if ("0A0F402F-670F-4696-9D9C-42F0E0D665A11".equals(category) 
				|| "shared data".equals(category.toLowerCase()) || "共享数据".equals(category)) {
			// add to shared programs
			tree.getSharedTree().addItem(node);
		}
		else if ("0A0F402F-670F-4696-9D9C-42F0E0D665A22".equals(category) 
				|| "my data".equals(category.toLowerCase()) || "我的数据".equals(category)) {
			// add to my programs
			if (m.getOwner().equals(userEmail)) {
				tree.getMyTree().addItem(node);
				node.addMenuItem(DatasetDeleteMenu.create(node));
			}
		}else if("0A0F402F-670F-4696-9D9C-42F0E0D665A00".equals(category) 
				|| "system data".equals(category.toLowerCase()) || "系统数据".equals(category)){
			tree.getSysTree().addItem(node);
			node.addMenuItem(DatasetDeleteMenu.create(node));
		}else if(category.contains(">")){			
			TreeItem treeItem = tree.findTreeItem(category);
			treeItem.addItem(node);
		}else {
			// add to system programs
			Category query = new Category();
			query.setId(category);
			query.setType("data");
			categorySrv.getCategory(query, "", new AsyncCallback<List<Category>>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess(List<Category> result) {
					// TODO Auto-generated method stub
					if(result != null){
						TreeItem treeItem;
						if(result.get(0).getPath().equals(null)){
							treeItem = tree.findTreeItem(result.get(0).getName());
						}else
							treeItem = tree.findTreeItem(result.get(0).getPath());
						treeItem.addItem(node);
					}
				}

			});
		}
	}

	/** Add context menus for the leaf node */
	public static void addContextMenu(DatasetTree datasetTree,DatasetLeaf node) {
		if(!AppController.email.equals("guest")){
			node.addMenuItem(DatasetUpdateMenu.create(node,datasetTree));
			node.addMenuItem(DatasetEditMenu.create(node,datasetTree));
			node.addMenuItem(DatasetDeprecateMenu.create(node));
		}
		node.addMenuItem(DatasetDownloadMenu.create(node));
	}
}
