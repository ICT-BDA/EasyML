/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import eml.studio.client.i18n.Globalization;

import com.google.gwt.user.client.ui.TreeItem;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Tree Menu for modules like program and data
 */
public class CopyOfModuleTree extends BaseTree {
	protected static Logger logger = Logger.getLogger(ProgramModuleTree.class.getName());

	/** Tree for System modules */
	protected TreeItem sysTree = null;

	/** Tree for Private modules */
	protected TreeItem myTree = null;

	/** Tree for shared modules */
	protected TreeItem sharedTree = null;

	/** Map categories to TreeItems */
	protected Map<String, TreeItem> sysTreeMap = new HashMap<String, TreeItem>();

	/**
	 * Initialize the top-level tree items
	 *
	 * @param sysText    text for sysTree
	 * @param sharedText text for sharedTree
	 * @param myText     text for myTree
	 */
	public CopyOfModuleTree(String sysText,
			String myText,
			String sharedText) {
		sysTree = this.addTextItem(sysText);
		sharedTree = this.addTextItem(sharedText);
		myTree = this.addTextItem(myText);
		sysTree.setState(true);
	}

	/**
	 * Find the TreeItem for a category. If it not exists, create it.
	 * TODO: reconstruct
	 * @param category category of a moduleId
	 * @return TreeItem of the moduleId corresponding to the category
	 */
	public TreeItem findTreeItem(String category) {
		// default return the system Tree
		if (category.equals(""))
			return getSysTree();

		TreeItem treeItem = sysTreeMap.get(category);
		if (treeItem == null) {
			// Category doesn't exist, create new TreeItems
			int splitIdx = category.lastIndexOf('>');
			String parentCategory = category.substring(0, splitIdx);
			String cateName = category.substring(splitIdx + 1, category.length());
			TreeItem parentItem = findTreeItem(parentCategory);

			//      treeItem = parentItem.addTextItem(cateName);
			treeItem = parentItem.addTextItem(Globalization.getI18NString(cateName));
			treeItem.setState(true);
			sysTreeMap.put(category, treeItem);
		}
		return treeItem;
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


}
