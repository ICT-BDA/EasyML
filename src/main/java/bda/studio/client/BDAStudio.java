/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client;

import bda.studio.client.controller.LoginController;
import bda.studio.client.mvp.AppController;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * The bda studio page module of the  project
 * 
 */
public class BDAStudio implements EntryPoint {
	
	/**
	 * BDAStudio module load method
	 */
	@Override
	public void onModuleLoad() {
		HandlerManager eventBus = new HandlerManager(null);
		LoginController loginController = new LoginController(eventBus);
		AppController appViewer = new AppController(eventBus, loginController);

		appViewer.go(RootLayoutPanel.get());
	}

}
