/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.presenter;

import eml.studio.client.event.LogoutEvent;
import eml.studio.client.mvp.AppController;
import eml.studio.client.mvp.view.HeaderView;
import eml.studio.client.ui.panel.UploadDatasetPanel;
import eml.studio.client.ui.panel.UploadProgramPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;

/**
 * The navigation bar loads
 * 
 */
public class HeaderLoader {
	private final HandlerManager eventBus;
	private final HeaderView headerView;
	private final MonitorPresenter presenter;

	/**
	 * Init
	 */
	public void init() {
		if(AppController.email.equals("guest")){
			headerView.getNavMenu().setVisible(false);
			headerView.getWorkStage().setVisible(false);
			headerView.getAdminAnchor().setVisible(false);
			headerView.getLogout().setVisible(false);
			headerView.getWorkStage().getElement().removeAttribute("href");
			headerView.getAdminAnchor().getElement().removeAttribute("href");
		}else{
			headerView.setAccount(AppController.username, AppController.email);

			String arr[] = AppController.power.split("");
			if(arr[1].equals("1")){
				headerView.getAdminAnchor().setVisible(true);
				headerView.getUserProg().setVisible(false);
				headerView.getUserData().setVisible(false);
				headerView.getUserList().setVisible(false);
				headerView.getWorkStage().setVisible(false);
				headerView.getUserProg().getElement().removeAttribute("href");
				headerView.getUserData().getElement().removeAttribute("href");
				headerView.getUserList().getElement().removeAttribute("href");
				headerView.getWorkStage().getElement().removeAttribute("href");
			}else{
				headerView.getWorkStage().setVisible(false);
				headerView.getAdminAnchor().setVisible(false);
				headerView.getUserProg().setVisible(false);
				headerView.getUserData().setVisible(false);
				headerView.getUserList().setVisible(false);
				headerView.getUserProg().getElement().removeAttribute("href");
				headerView.getUserData().getElement().removeAttribute("href");
				headerView.getUserList().getElement().removeAttribute("href");
				headerView.getWorkStage().getElement().removeAttribute("href");
				headerView.getAdminAnchor().getElement().removeAttribute("href");
			}	

			if(arr[3].equals("1")){
				headerView.getProgAnchor().setVisible(true);
				headerView.getDataAnchor().setVisible(true);
			}else{
				headerView.getProgAnchor().setVisible(false);
				headerView.getDataAnchor().setVisible(false);
				headerView.getProgAnchor().getElement().removeAttribute("href");
				headerView.getDataAnchor().getElement().removeAttribute("href");
			}	

			headerView.getNewJobAnchor().setVisible(true);
		}
	}

	public HeaderLoader(HandlerManager eventBus, HeaderView headerView,MonitorPresenter presenter) {
		this.eventBus = eventBus;
		this.headerView = headerView;
		this.presenter = presenter;
	}

	/**
	 * Event binding
	 */
	public void bind() {
		//logout
		headerView.getLogout().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LogoutEvent());
			}

		});

		//Create a new task
		headerView.getNewJobAnchor().addClickHandler( new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				presenter.clearCurrentJob();
				presenter.getView().getController().clear();
				presenter.getView().clearPropTable();
				presenter.unlockSubmit();
				presenter.updateJobIFView();
			}

		} );

		//Upload programs
		headerView.getProgAnchor().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UploadProgramPanel panel = new UploadProgramPanel(AppController.email,presenter);
				panel.show();
				panel.center();
			}

		});

		//Upload data
		headerView.getDataAnchor().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UploadDatasetPanel panel = new UploadDatasetPanel(AppController.email,presenter);
				panel.show();
				panel.center();
			}

		});

		//Backstage management
		headerView.getAdminAnchor().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				History.newItem("admin");
			}

		});

		//Personal information
		if(AppController.email != "guest"){
			headerView.getUserAnchor().addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					History.newItem("account");
				}

			});
		}

		//Work Stage
		headerView.getWorkStage().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				presenter.clearCurrentJob();
				presenter.getView().getController().clear();
				presenter.getView().clearPropTable();
				presenter.unlockSubmit();
				presenter.updateJobIFView();

			}

		});

	}
}
