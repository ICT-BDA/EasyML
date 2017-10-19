/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import eml.studio.client.mvp.AppController;
import eml.studio.client.rpc.CategoryService;
import eml.studio.client.rpc.CategoryServiceAsync;
import eml.studio.client.rpc.ProgramService;
import eml.studio.client.rpc.ProgramServiceAsync;
import eml.studio.client.ui.menu.*;
import eml.studio.shared.model.Category;
import eml.studio.shared.model.Module;
import eml.studio.shared.model.Program;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.logging.Logger;
import java.util.List;

/**
 * Load all existing programs from database into Program Tree Menu
 */
public class ProgramTreeLoader {
	protected static CategoryServiceAsync categorySrv = GWT.create(CategoryService.class);
	protected static Logger logger = Logger.getLogger(ProgramTreeLoader.class.getName());

	/**
	 * TODO: query the programs by users
	 * @param userEmail used to filter myPrograms
	 */
	public static ProgramTree load(final String userEmail) {
		final ProgramTree tree = new ProgramTree();
		ProgramServiceAsync svc = GWT.create(ProgramService.class);
		svc.load(new AsyncCallback<List<Program>>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.info("Load programs failed");
			}

			@Override
			public void onSuccess(List<Program> result) {
				for (Program m : result) {
					if (!m.getDeprecated()) {
						ProgramLeaf node = new ProgramLeaf(m);
						addProgramLeaf(tree, node, userEmail);
						addContextMenu(tree,node);
					}
				}
			}
		});

		return tree;
	}

	/** Add a program into the menu */
	public static void addProgramLeaf(final ProgramTree tree,
			final ProgramLeaf node,
			String userEmail) {
		Module m = node.getModule();
		String category = m.getCategory();
		if ("0A0F402F-670F-4696-9D9C-42F0E0D665A1".equals(category) 
				|| "shared program".equals(category.toLowerCase()) || "共享程序".equals(category)) {
			// add to shared programs
			tree.getSharedTree().addItem(node);
		}
		else if ("0A0F402F-670F-4696-9D9C-42F0E0D665A2".equals(category) 
				|| "my program".equals(category.toLowerCase()) || "我的程序".equals(category)) {
			// add to my programs
			if (m.getOwner().equals(userEmail)) {
				tree.getMyTree().addItem(node);
				node.addMenuItem(ProgramDeleteMenu.create(node));
			}
		}else if("0A0F402F-670F-4696-9D9C-42F0E0D665A0".equals(category) 
				|| "system program".equals(category.toLowerCase()) || "系统程序".equals(category)){
			tree.getSysTree().addItem(node);
			node.addMenuItem(ProgramDeleteMenu.create(node));
		}else if(category.contains(">")){			
			TreeItem treeItem = tree.findTreeItem(category);
			treeItem.addItem(node);
		}else {
			// add to system programs
			Category query = new Category();
			query.setId(category);
			query.setType("prog");
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
	public static void addContextMenu(ProgramTree programTree,ProgramLeaf node) {

		if(!AppController.email.equals("guest")){
			node.addMenuItem(ProgramEditMenu.create(node,programTree));
			node.addMenuItem(ProgramUpdateMenu.create(node,programTree));
			node.addMenuItem(ProgramDeprecateMenu.create(node));
		}
		node.addMenuItem(ProgramDownloadMenu.create(node));

	}
}
