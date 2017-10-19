/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.menu;

import com.google.gwt.user.client.ui.MenuItem;
import com.orange.links.client.menu.HasContextMenu;

/**
 * Right mouse up Menu interface
 */
public interface HasRightMouseUpMenu extends HasContextMenu {
	public void addMenuItem(MenuItem menuItem);
}
