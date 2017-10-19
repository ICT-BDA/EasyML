/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.menu;

import com.google.gwt.user.client.Command;

/**
 * Used as a composite component of LeftMouseUpMenuItem
 *
 */
public abstract class MenuItemCommand implements Command {

	protected HasRightMouseUpMenu component;

	public MenuItemCommand(HasRightMouseUpMenu com) {
		this.component = com;
	}

}
