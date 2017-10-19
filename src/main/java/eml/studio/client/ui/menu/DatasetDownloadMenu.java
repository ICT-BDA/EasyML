/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.menu;

import java.util.logging.Logger;

import eml.studio.client.rpc.DatasetService;
import eml.studio.client.rpc.DatasetServiceAsync;
import eml.studio.client.ui.tree.DatasetLeaf;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Create a menu for program downloading
 */
public class DatasetDownloadMenu {
	private static final double MAX_DOWNLOAD_SIZE = 1000000; //Maximum file size (in kb)
	protected static Logger logger = Logger.getLogger(DatasetDownloadMenu.class.getName());

	public static MenuItem create(final DatasetLeaf node) {
		Command command = new MenuItemCommand(node) {

			@Override
			public void execute() {
				String id = node.getModule().getId();
				final DatasetServiceAsync svc = GWT.create(DatasetService.class);
				svc.download(id, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					@Override
					public void onSuccess(final String result) {
						Window.alert("Start calculating the file size");
						svc.getFileSize(result, new AsyncCallback<Double>(){

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								logger.info("Failed to get download file size!");
							}

							@Override
							public void onSuccess(Double size) {
								// TODO Auto-generated method stub
								if(size<MAX_DOWNLOAD_SIZE )
								{
									Window.alert("Start downloading the file");
									String url = GWT.getModuleBaseURL().split("EML")[0]
											+ "EMLStudioMonitor/filedownload?filename=" + result;
									Window.open(url, "_blank",
											"status=0,toolbar=0,menubar=0,location=0");
								}
								else
									Window.alert("The download file exceeds the limit size (1g) and can not download the file！");
							}

						});

					}

				});
				this.component.getContextMenu().hide();
			}
		};

		MenuItem item = new MenuItem("Download", command);
		return item;
	}
}
