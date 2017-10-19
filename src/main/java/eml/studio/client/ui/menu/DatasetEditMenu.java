/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.menu;

import eml.studio.client.mvp.AppController;
import eml.studio.client.ui.panel.EditDatasetPanel;
import eml.studio.client.ui.tree.DatasetLeaf;
import eml.studio.client.ui.tree.DatasetTree;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Created a edit context menu for a Dataset
 */
public class DatasetEditMenu {
	public static MenuItem create(final DatasetLeaf node, final DatasetTree tree) {

		Command command = new MenuItemCommand(node) {
			@Override
			public void execute() {
				EditDatasetPanel panel = new EditDatasetPanel(AppController.email,tree,node);

				panel.show(node.getModule());

				this.component.getContextMenu().hide();
			}
		};
		MenuItem item = new MenuItem("Edit", command);
		return item;
	}
}
