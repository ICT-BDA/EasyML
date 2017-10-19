/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * The basic class for menu tree
 */
public abstract class BaseTree extends Tree {

	public BaseTree() {
		super(new TreeImageResources(), true);

		this.addStyleName("bda-treedir");

		this.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem item = event.getSelectedItem();
				Boolean state = item.getState();
				// [GWT Issue 3660] to avoid pass invoke onSection two times
				TreeItem parent = item.getParentItem();
				item.getTree().setSelectedItem(parent, false);
				if (parent != null)
					parent.setSelected(false);
				item.setState(!state);
			}
		});
	}
}
