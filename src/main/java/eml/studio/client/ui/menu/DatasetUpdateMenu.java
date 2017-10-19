/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.menu;

import eml.studio.client.ui.panel.UpdateDatasetPanel;
import eml.studio.client.ui.tree.DatasetLeaf;
import eml.studio.client.ui.tree.DatasetTree;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Context menu to update the information of a Dataset
 */
public class DatasetUpdateMenu {

	public static MenuItem create(final DatasetLeaf node, final DatasetTree tree) {
		Command command = new MenuItemCommand(node) {
			@Override
			public void execute() {
				UpdateDatasetPanel panel = new UpdateDatasetPanel(tree, node);

				panel.show(node.getModule());

				this.component.getContextMenu().hide();
			}
		};

		MenuItem item = new MenuItem("Update", command);
		return item;
	}
}