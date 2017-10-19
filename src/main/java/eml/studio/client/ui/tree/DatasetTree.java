/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import eml.studio.client.util.Constants;

/**
 * Dataset Tree Menu
 */
public class DatasetTree extends DatasetModuleTree {

	public DatasetTree() {
		super(Constants.studioUIMsg.systemData(),
				Constants.studioUIMsg.myData(),
				Constants.studioUIMsg.sharedData());
	}

}
