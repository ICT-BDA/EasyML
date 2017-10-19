/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import eml.studio.client.rpc.CategoryService;
import eml.studio.client.rpc.CategoryServiceAsync;
import eml.studio.shared.model.Category;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.logging.Logger;
import java.util.List;

/**
 * Load all existing programs from database into Program Tree Menu
 */
public class ProgramCateTreeLoader {
	protected static CategoryServiceAsync categorySrv = GWT.create(CategoryService.class);
	protected static Logger logger = Logger.getLogger(ProgramCateTreeLoader.class.getName());


	/**
	 * TODO: query the programs by users
	 * @param userEmail used to filter myPrograms
	 */
	public static ProgramCategoryTree load() {
		final ProgramCategoryTree tree = new ProgramCategoryTree();
		Category category = new Category();
		category.setType("prog");
		categorySrv.getCategory(category, " and level != 1 order by name", new AsyncCallback<List<Category>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				logger.info("Loader component failed");
			}

			@Override
			public void onSuccess(List<Category> result) {
				// TODO Auto-generated method stub
				for(Category c : result){
					addCategoryLeaf(tree,c);
				}
			}

		});
		return tree;
	}

	/** Add a program into the menu */
	public static void addCategoryLeaf(final ProgramCategoryTree tree, Category category) {
		if(category.getPath().equals(null)){
			tree.findTreeItem(category.getName());
		}else
			tree.findTreeItem(category.getPath());
	}
}
