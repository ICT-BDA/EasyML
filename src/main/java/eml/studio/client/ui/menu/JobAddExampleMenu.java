/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.menu;

import eml.studio.client.rpc.JobService;
import eml.studio.client.rpc.JobServiceAsync;
import eml.studio.client.ui.tree.JobLeaf;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Menu of adding a job to example
 */
public class JobAddExampleMenu {
	public static MenuItem create(final JobLeaf node) {
		Command command = new MenuItemCommand(node) {

			@Override
			public void execute() {
				String id = node.getModule().getJobId();
				boolean y = Window.confirm("Are you sure you want to join the example task?");
				if (y) {
					JobServiceAsync srv = GWT.create(JobService.class);
					srv.setExampleJobs(id, new AsyncCallback<Void>() {

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
		MenuItem item = new MenuItem("Join example task", command);
		return item;
	}
}
