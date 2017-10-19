/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.presenter;

import eml.studio.client.event.LogoutEvent;
import eml.studio.client.mvp.AppController;
import eml.studio.client.mvp.view.AdminView;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * Background management presenter
 */
public class AdminPresenter implements Presenter {
	private ProgramLoader programLoader;
	private DatasetLoader datasetLoader;
	private AccountLoader accountLoader;
	private CategoryLoader categoryLoader;

	private final HandlerManager eventBus;
	private final AdminView adminView;

	public AdminPresenter(HandlerManager eventBus, AdminView adminView) {
		this.eventBus = eventBus;
		this.adminView = adminView;
		bind();
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(adminView.asWidget());
		init();
	}

	//Initialization
	private void init(){
		adminView.setAccount(AppController.username, AppController.email);		
		
		//Admin user have no workbench visit
		if(AppController.email.equals("admin")){										
			adminView.getWorkStage().getElement().getStyle().setDisplay(Display.NONE);
			adminView.getWorkStage().getElement().removeAttribute("href");
		}

		programLoader = new ProgramLoader(adminView);								
		datasetLoader = new DatasetLoader(adminView);
		accountLoader = new AccountLoader(adminView);
		categoryLoader = new CategoryLoader(adminView);
		programLoader.go();														
	}

	private void bind() {
		//Programs
		adminView.getUserProg().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				adminView.getProgContent().setVisible(true);
				adminView.getDataContent().setVisible(false);
				adminView.getUserContent().setVisible(false);
				adminView.getCateContent().setVisible(false);
				adminView.getUserProg().addStyleName("current");
				adminView.getUserData().removeStyleName("current");
				adminView.getUserList().removeStyleName("current");
				adminView.getCategory().removeStyleName("current");
				programLoader.setSearchFlag(false);
				programLoader.go();
			}

		});

		//Datasets
		adminView.getUserData().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				adminView.getProgContent().setVisible(false);
				adminView.getDataContent().setVisible(true);
				adminView.getUserContent().setVisible(false);
				adminView.getCateContent().setVisible(false);
				adminView.getUserProg().removeStyleName("current");
				adminView.getUserData().addStyleName("current");
				adminView.getUserList().removeStyleName("current");
				adminView.getCategory().removeStyleName("current");
				datasetLoader.setSearchFlag(false);
				datasetLoader.go();
			}

		});

		//Users
		adminView.getUserList().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				adminView.getProgContent().setVisible(false);
				adminView.getDataContent().setVisible(false);
				adminView.getUserContent().setVisible(true);
				adminView.getCateContent().setVisible(false);
				adminView.getUserProg().removeStyleName("current");
				adminView.getUserData().removeStyleName("current");
				adminView.getUserList().addStyleName("current");
				adminView.getCategory().removeStyleName("current");
				accountLoader.setSearchFlag(false);
				accountLoader.go();
			}

		});

		//Categories
		adminView.getCategory().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				adminView.getProgContent().setVisible(false);
				adminView.getDataContent().setVisible(false);
				adminView.getUserContent().setVisible(false);
				adminView.getCateContent().setVisible(true);
				adminView.getUserProg().removeStyleName("current");
				adminView.getUserData().removeStyleName("current");
				adminView.getUserList().removeStyleName("current");
				adminView.getCategory().addStyleName("current");
				categoryLoader.setSearchFlag(false);
				categoryLoader.go();
			}

		});

		//WorkStage
		adminView.getWorkStage().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				History.newItem("monitor");
			}

		});

		//Account Information
		adminView.getUserAnchor().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(!AppController.email.equals("admin")){
					History.newItem("account");
				}
			}

		});

		//Logout
		adminView.getLogout().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LogoutEvent());
			}

		});

	}
}