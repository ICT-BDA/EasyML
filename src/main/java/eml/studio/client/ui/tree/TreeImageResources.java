/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import eml.studio.client.resources.Resources;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree;

public class TreeImageResources implements Tree.Resources {

	@Override
	public ImageResource treeClosed() {
		return Resources.instance.getTreeClosed();
	}

	@Override
	public ImageResource treeLeaf() {
		return Resources.instance.getTreeLeaf();
	}

	@Override
	public ImageResource treeOpen() {
		return Resources.instance.getTreeOpen();
	}

}
