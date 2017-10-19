/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.controller;

import java.util.logging.Logger;

import eml.studio.client.event.LoginEvent;
import eml.studio.client.mvp.AppController;
import eml.studio.client.rpc.AccountService;
import eml.studio.client.rpc.AccountServiceAsync;
import eml.studio.client.rpc.MailService;
import eml.studio.client.rpc.MailServiceAsync;
import eml.studio.client.ui.panel.AlertPanel;
import eml.studio.shared.model.Account;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 *
 *  Login controller is used to control login procession
 */

public class LoginController {
	private static Logger logger = Logger.getLogger(LoginController.class.getName());
	protected AccountServiceAsync accountSrv = GWT.create(AccountService.class);
	protected MailServiceAsync mailSrv = GWT.create(MailService.class);
	protected AlertPanel alertPanel = new AlertPanel();

	private HandlerManager eventBus;
	private String token;

	public LoginController(HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.token = null;
		alertPanel.getConfirmBtn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				AppController.redirect("index.html");
			}

		});

		alertPanel.getClose().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				AppController.redirect("index.html");
			}

		});
	}

	public void logout() {
		accountSrv.logout(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.Location.reload();
			}

			@Override
			public void onSuccess(Void result) {
				AppController.redirect("index.html?url=" + AppController.getUrl());
			}

		});

	}

	public void dologin() {
		accountSrv.isLogin(new AsyncCallback<Account>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.warning(caught.getMessage());
				String urlString = AppController.getUrl();
				AppController.redirect("index.html?url=" + urlString);
			}

			@Override
			public void onSuccess(Account result) {
				if (result != null) {
					if(result.getEmail().equals("admin") && !token.equals("account")){
						AppController.username = result.getUsername();
						AppController.email = result.getEmail();
						AppController.power = result.getPower();
						eventBus.fireEvent(new LoginEvent("admin"));
					}else{
						AppController.username = result.getUsername();
						AppController.email = result.getEmail();
						AppController.power = result.getPower();
						eventBus.fireEvent(new LoginEvent(token));
					}
				} else {
					String urlString = AppController.getUrl();
					AppController.redirect("index.html?url=" + urlString);
				}
			}

		});
	}

	public void go(String token) {
		this.token = token;
		if(token.startsWith("verifyr")){
			mailSrv.VerifyLink(token, new AsyncCallback<String>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					logger.warning(caught.getMessage());
					AppController.redirect("index.html?url=" + AppController.getUrl());
				}

				@Override
				public void onSuccess(String result) {
					// TODO Auto-generated method stub
					String arr[] = result.split(" ");
					if(arr[0].equals("success")){
						AppController.username = result.split(" ")[1];
						AppController.email = result.split(" ")[2];
						AppController.verifylink = result.split(" ")[5];
						eventBus.fireEvent(new LoginEvent("register"));
					}else{
						alertPanel.setContent("Link failed");
						RootLayoutPanel.get().clear();
						alertPanel.show();
						logger.info(result);
					}
				}
			});
		}else if(token.startsWith("verifym")){
			mailSrv.VerifyLink(token, new AsyncCallback<String>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					logger.warning(caught.getMessage());
					AppController.redirect("index.html?url=" + AppController.getUrl());
				}

				@Override
				public void onSuccess(String result) {
					// TODO Auto-generated method stub
					String arr[] = result.split(" ");
					if(arr[0].equals("success")){
						AppController.username = arr[1];
						AppController.email = arr[2];
						AppController.verifylink = arr[5];
						eventBus.fireEvent(new LoginEvent("resetpwd"));
					}else{
						alertPanel.setContent("Link failed");
						RootLayoutPanel.get().clear();
						alertPanel.show();
						logger.info(result);
					}
				}

			});
		}else if(token.startsWith("register") || token.startsWith("resetpwd")){
			AppController.redirect("index.html?url=" + AppController.getUrl());
		}else {
			dologin();
		}
	}

}
