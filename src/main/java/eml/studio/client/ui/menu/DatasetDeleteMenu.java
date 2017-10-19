/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.menu;

import eml.studio.client.rpc.DatasetService;
import eml.studio.client.rpc.DatasetServiceAsync;
import eml.studio.client.ui.tree.DatasetLeaf;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Context menu to delete a Dataset
 */
public class DatasetDeleteMenu {

	public static MenuItem create(final DatasetLeaf node) {

		Command command = new MenuItemCommand(node) {

			@Override
			public void execute() {
				String id = node.getModule().getId();
				boolean y = Window.confirm("Ready to delete " + node.getText() + "？");
				if (y) {
					DatasetServiceAsync svc = GWT.create(DatasetService.class);
					svc.delete(id, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(Void result) {
							node.delete();
						}
					});
				}

				this.component.getContextMenu().hide();
			}
		};

		MenuItem item = new MenuItem("Delete", command);
		return item;
	}
}
