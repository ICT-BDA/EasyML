/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.menu;

import eml.studio.client.rpc.ProgramService;
import eml.studio.client.rpc.ProgramServiceAsync;
import eml.studio.client.ui.tree.ProgramLeaf;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Created a deprecate menu for a program
 */
public class ProgramDeprecateMenu {

	public static MenuItem create(final ProgramLeaf node) {
		Command command = new MenuItemCommand(node) {
			@Override
			public void execute() {
				String id = node.getModule().getId();
				boolean y = Window.confirm("Are you ready to deprecate " + node.getModule().getName() + "？");
				if (y) {
					ProgramServiceAsync svc = GWT.create(ProgramService.class);
					svc.deprecate(id, new AsyncCallback<Void>() {

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

		MenuItem item = new MenuItem("deprecate", command);
		return item;
	}
}