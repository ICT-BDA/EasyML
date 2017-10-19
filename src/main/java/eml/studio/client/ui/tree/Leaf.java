/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import eml.studio.client.ui.menu.HasRightMouseUpMenu;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.TreeItem;
import com.orange.links.client.menu.ContextMenu;

public abstract class Leaf<T> extends TreeItem implements HasRightMouseUpMenu {
	protected Label label = null;
	protected ContextMenu menu;
	private T module;

	/**
	 * Create a leaf node for the Tree
	 *
	 * @param name   name of the TreeItem
	 * @param module Attached moduleId for the TreeItem
	 */
	public Leaf(String name,
			T module,
			String style) {
		// add context menu
		this.menu = new ContextMenu();
		label = new Label(name);
		this.setWidget(label);

		label.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				// display the context menu when right click
				if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
					menu.setPopupPosition(event.getClientX(), event.getClientY());
					menu.show();
				}
			}
		});

		// set moduleId
		this.module = module;
		this.addStyleName("bda-treeleaf");
		if (!style.equals(""))
			this.addStyleName(style);
	}

	public Leaf(String name, T module) {
		this(name, module, "");
	}

	/**
	 * Clone a moduleId object
	 */
	public T getModule() {
		return module;
	}

	@Override
	public ContextMenu getContextMenu() {
		return menu;
	}

	/**
	 * Add a context menu item
	 */
	@Override
	public void addMenuItem(MenuItem menuItem) {
		menu.addItem(menuItem);
	}

	/**
	 * Remove this treeItem
	 */
	public void delete() {
		getParentItem().removeItem(this);
	}

	@Override
	public void setText(String text) {
		label.setText(text);
	}
}