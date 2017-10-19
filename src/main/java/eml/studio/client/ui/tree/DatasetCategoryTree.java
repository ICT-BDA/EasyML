/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import eml.studio.client.util.Constants;

/**
 * Program Tree Menu
 */
public class DatasetCategoryTree extends DatasetModuleTree {

	public DatasetCategoryTree() {
		super(Constants.studioUIMsg.systemData(),
				Constants.studioUIMsg.myData(),
				Constants.studioUIMsg.sharedData());
	}
}
