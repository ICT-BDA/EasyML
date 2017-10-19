/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget;

import com.google.gwt.user.client.ui.DisclosurePanel;

/**
 * Base Class of DisclosureStackPanel
 */
public class DisclosureStackPanel {
	private DisclosurePanel panel;

	public DisclosureStackPanel(String title) {

		panel = new DisclosurePanel(title);
		panel.setOpen(true);
		panel.setAnimationEnabled(true);
		panel.getElement().setAttribute("style", "width:100%;");
		panel.setStyleName("myDisclosurePanel");
		panel.getHeader().setStyleName("myDisclosurePanel-header");
		panel.setSize("100%", "100%");
	}

	public DisclosurePanel asWidget() {
		return panel;
	}
}
