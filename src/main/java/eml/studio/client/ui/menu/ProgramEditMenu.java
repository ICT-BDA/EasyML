/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.menu;

import eml.studio.client.mvp.AppController;
import eml.studio.client.ui.panel.EditProgramPanel;
import eml.studio.client.ui.tree.ProgramLeaf;
import eml.studio.client.ui.tree.ProgramTree;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Created a edit context menu for a program
 */
public class ProgramEditMenu {

	public static MenuItem create(final ProgramLeaf node, final ProgramTree tree) {

		Command command = new MenuItemCommand(node) {
			@Override
			public void execute() {
				EditProgramPanel panel = new EditProgramPanel(AppController.email,tree,node);

				panel.show(node.getModule());

				this.component.getContextMenu().hide();
			}
		};
		MenuItem item = new MenuItem("Edit", command);
		return item;
	}
}