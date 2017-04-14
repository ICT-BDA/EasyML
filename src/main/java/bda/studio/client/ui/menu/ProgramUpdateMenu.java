/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client.ui.menu;

import bda.studio.client.ui.panel.UpdateProgramPanel;
import bda.studio.client.ui.tree.ProgramLeaf;
import bda.studio.client.ui.tree.ProgramTree;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Context menu to update the information of a program
 */
public class ProgramUpdateMenu {

  public static MenuItem create(final ProgramLeaf node, final ProgramTree tree) {

    Command command = new MenuItemCommand(node) {

      @Override
	public void execute() {
        UpdateProgramPanel panel = new UpdateProgramPanel(tree,node);
        panel.show(node.getModule());
        this.component.getContextMenu().hide();
      }

    };

    MenuItem item = new MenuItem("Update", command);
    return item;
  }
}
