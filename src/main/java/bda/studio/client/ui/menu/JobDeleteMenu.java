/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client.ui.menu;

import bda.studio.client.rpc.JobService;
import bda.studio.client.rpc.JobServiceAsync;
import bda.studio.client.ui.tree.JobLeaf;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Create a delete menu for job
 */
public class JobDeleteMenu {

  public static MenuItem create(final JobLeaf node) {
    Command command = new MenuItemCommand(node) {

      @Override
      public void execute() {
        String id = node.getModule().getJobId();
        boolean y = Window.confirm("Are you sure you want to delete?");
        if (y) {
          JobServiceAsync srv = GWT.create(JobService.class);
          srv.deleteJob(id, new AsyncCallback<Void>() {

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

