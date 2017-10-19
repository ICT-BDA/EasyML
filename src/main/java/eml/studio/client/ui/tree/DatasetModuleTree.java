/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import eml.studio.client.rpc.CategoryService;
import eml.studio.client.rpc.CategoryServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Tree Menu for modules like program and data
 */
public class DatasetModuleTree extends BaseTree {
	protected static CategoryServiceAsync categorySrv = GWT.create(CategoryService.class);
	protected static Logger logger = Logger.getLogger(DatasetModuleTree.class.getName());

	/** Tree for System modules */
	protected TreeItem sysTree = null;

	/** Tree for Private modules */
	protected TreeItem myTree = null;

	/** Tree for shared modules */
	protected TreeItem sharedTree = null;

	/** Map categories to TreeItems */
	protected Map<String, TreeItem> sysTreeMap = new HashMap<String, TreeItem>();
	protected Map<String, TreeItem> shrTreeMap = new HashMap<String, TreeItem>();
	protected Map<String, TreeItem> myTreeMap = new HashMap<String, TreeItem>();
	protected Map<TreeItem, String> sysTreeCateMap = new HashMap<TreeItem, String>();
	protected Map<TreeItem, String> shrTreeCateMap = new HashMap<TreeItem, String>();
	protected Map<TreeItem, String> myTreeCateMap = new HashMap<TreeItem, String>();

	/**
	 * Initialize the top-level tree items
	 *
	 * @param sysText    text for sysTree
	 * @param sharedText text for sharedTree
	 * @param myText     text for myTree
	 */
	public DatasetModuleTree(String sysText,
			String myText,
			String sharedText) {
		sysTree = this.addTextItem(sysText);
		sharedTree = this.addTextItem(sharedText);
		myTree = this.addTextItem(myText);
		sysTree.setState(true);
		sharedTree.setState(true);
		myTree.setState(true);
	}

	/**
	 * Find the TreeItem for a category. If it not exists, create it.
	 * TODO: reconstruct
	 * @param category category of a moduleId
	 * @return TreeItem of the moduleId corresponding to the category
	 */
	public TreeItem findTreeItem(String category) {
		// default return the system Tree
		if(category.equals(""))
			return getSysTree();

		if(category.startsWith("我的数据") || category.toLowerCase().startsWith("my data")){
			if(category.equals("我的数据") || category.toLowerCase().equals("my data")){
				return getMyTree();
			}else{
				TreeItem treeItem = myTreeMap.get(category);
				if (treeItem == null) {
					// Category doesn't exist, create new TreeItems
					int splitIdx = category.lastIndexOf('>');
					String parentCategory = category.substring(0, splitIdx);
					String cateName = category.substring(splitIdx + 1, category.length());
					TreeItem parentItem = findTreeItem(parentCategory);

					treeItem = parentItem.addTextItem(cateName);
					treeItem.setState(true);
					myTreeMap.put(category, treeItem);
					myTreeCateMap.put(treeItem, category);
				}
				return treeItem;
			}
		}else if(category.startsWith("共享数据") || category.toLowerCase().startsWith("shared data")){
			if(category.equals("共享数据") || category.toLowerCase().equals("shared data")){
				return getSharedTree();
			}else{
				TreeItem treeItem = shrTreeMap.get(category);
				if (treeItem == null) {
					// Category doesn't exist, create new TreeItems
					int splitIdx = category.lastIndexOf('>');
					String parentCategory = category.substring(0, splitIdx);
					String cateName = category.substring(splitIdx + 1, category.length());
					TreeItem parentItem = findTreeItem(parentCategory);

					treeItem = parentItem.addTextItem(cateName);
					treeItem.setState(true);
					shrTreeMap.put(category, treeItem);
					shrTreeCateMap.put(treeItem, category);
				}
				return treeItem;
			}
		}else{
			if(category.equals("系统数据") || category.toLowerCase().equals("system data")){
				return getSysTree();
			}else{
				TreeItem treeItem = sysTreeMap.get(category);
				if (treeItem == null) {
					// Category doesn't exist, create new TreeItems
					int splitIdx = category.lastIndexOf('>');
					String parentCategory = category.substring(0, splitIdx);
					String cateName = category.substring(splitIdx + 1, category.length());
					TreeItem parentItem = findTreeItem(parentCategory);

					treeItem = parentItem.addTextItem(cateName);
					treeItem.setState(true);
					sysTreeMap.put(category, treeItem);
					sysTreeCateMap.put(treeItem, category);
				}
				return treeItem;
			}
		}
	}

	/** Return the tree for system modules */
	public TreeItem getSysTree() {
		return sysTree;
	}

	/** Return the tree for private modules */
	public TreeItem getMyTree() {
		return myTree;
	}

	/** Return the tree for shared modules */
	public TreeItem getSharedTree() {
		return sharedTree;
	}

	public Map<TreeItem, String> getSysTreeCateMap(){
		return sysTreeCateMap;
	}

	public Map<TreeItem, String> getShrTreeCateMap(){
		return shrTreeCateMap;
	}

	public Map<TreeItem, String> getMyTreeCateMap(){
		return myTreeCateMap;
	}
}
