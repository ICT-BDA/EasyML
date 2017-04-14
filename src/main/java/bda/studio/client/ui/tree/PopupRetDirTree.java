/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client.ui.tree;

import java.util.logging.Logger;

/**
 * Preview the results tree in the results window
 */
public class PopupRetDirTree extends BaseTree{
	protected static Logger logger = Logger.getLogger(PopupRetDirTree.class.getName());

	private PopupRetDirLeaf root = null; //The root node of the tree
	
	public PopupRetDirTree(String path){
		root = new PopupRetDirLeaf(path);
		this.addItem(root);
	}
	/**
	 * @return the root
	 */
	public PopupRetDirLeaf getRoot() {
		return root;
	}
	/**
	 * @param root the root to set
	 */
	public void setRoot(PopupRetDirLeaf root) {
		this.root = root;
	}
	
}
