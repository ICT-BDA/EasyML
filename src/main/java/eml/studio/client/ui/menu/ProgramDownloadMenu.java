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
 * Create a menu for program downloading
 */
public class ProgramDownloadMenu {

	public static MenuItem create(final ProgramLeaf node) {
		Command command = new MenuItemCommand(node) {

			@Override
			public void execute() {
				String id = node.getModule().getId();
				ProgramServiceAsync svc = GWT.create(ProgramService.class);
				svc.download(id, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					@Override
					public void onSuccess(String result) {
						String url = GWT.getModuleBaseURL().split("EML")[0]
								+ "EMLStudioMonitor/filedownload?filename=" + result;
						Window.open(url, "_blank",
								"status=0,toolbar=0,menubar=0,location=0");
					}

				});
				this.component.getContextMenu().hide();
			}
		};

		MenuItem item = new MenuItem("Download", command);
		return item;
	}
}
