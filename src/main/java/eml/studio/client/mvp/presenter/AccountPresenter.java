/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.presenter;

import eml.studio.client.event.LogoutEvent;
import eml.studio.client.mvp.AppController;
import eml.studio.client.mvp.view.AccountView;
import eml.studio.client.rpc.AccountService;
import eml.studio.client.rpc.AccountServiceAsync;
import eml.studio.client.ui.panel.AlertPanel;
import eml.studio.client.util.Constants;
import eml.studio.shared.model.Account;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * Modified persional info logical presenter
 */
public class AccountPresenter implements Presenter {
	protected AccountServiceAsync accountService = GWT.create(AccountService.class);
	protected AlertPanel alertPanel = new AlertPanel();
	private final HandlerManager eventBus;
	private final AccountView accountView;

	/**
	 * User information is initialized
	 */
	public void init(){
		if(AppController.power.startsWith("0")){ 			//Authority code starts with 0, general user have no authority to visit background management.
			accountView.getAdminAnchor().setVisible(false);
			accountView.getAdminAnchor().setEnabled(false);
		}
		accountView.setAccount(AppController.username, AppController.email);
		Account account = new Account();
		account.setUsername(AppController.username);
		account.setEmail(AppController.email);
		accountService.getAccountInfo(account, new AsyncCallback<Account>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				accountView.getVererrorLabel().setText(caught.getMessage());
			}

			@Override
			public void onSuccess(Account result) {
				// TODO Auto-generated method stub
				accountView.getUsrBox().setValue(result.getUsername());
				accountView.getCmpBox().setValue(result.getCompany());
				accountView.getPstBox().setValue(result.getPosition());
			}

		});
	}

	/**
	 * Event binding
	 */
	public void bind() {
		//Prompt box close button
		alertPanel.getClose().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				History.fireCurrentHistoryState();
			}

		});

		//Prompt box confirmation button
		alertPanel.getConfirmBtn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				History.fireCurrentHistoryState();
			}

		});

		//Backstage management
		accountView.getAdminAnchor().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				History.newItem("admin");
			}

		});

		//Work stage
		accountView.getWorkStage().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				History.newItem("monitor");
			}

		});

		//The user exits
		accountView.getLogout().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LogoutEvent());
			}

		});

		//Personal Information Page Navigation Click Response - Information Edit
		accountView.getNavInfo().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				accountView.getNavInfo().addStyleName("account-nav-selected");
				accountView.getNavPwd().removeStyleName("account-nav-selected");
				accountView.getInfoPanel().setVisible(true);
				accountView.getPwdPanel().setVisible(false);
			}

		});

		//Personal information page navigation click response - password modification
		accountView.getNavPwd().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				accountView.getNavInfo().removeStyleName("account-nav-selected");
				accountView.getNavPwd().addStyleName("account-nav-selected");
				accountView.getInfoPanel().setVisible(false);
				accountView.getPwdPanel().setVisible(true);
			}

		});

		//Personal Information Page Navigation Click Response - Information Modify OK
		accountView.getInfoConfirmBtn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Account account = info_validate();
				if(account != null){
					accountService.modifyInfo(account, new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							accountView.getPsterrorLabel().setText(caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							if(result.startsWith("success")){
								AppController.username = result.split(" ")[1];
								alertPanel.setContent( Constants.accountUIMsg.infoSuccess() );
								alertPanel.show();
							}else if(result.equals("username existed")){
								accountView.getUsrerrorLabel().setText( Constants.accountUIMsg.infoExist() );
							}else
								accountView.getPsterrorLabel().setText( Constants.accountUIMsg.infoFail() );
						}

					});
				}
			}

		});

		//Personal Information Page Navigation Click Response - Password Change OK
		accountView.getPwdConfirmBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				String account = pwd_validate();
				if (account != null) {
					accountService.modifyPassword(account, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							accountView.getVererrorLabel().setText(caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							if (result != null) {
								String arr[] = result.split(" ");
								if (arr[0].equals("[success]")) {
									AppController.email = arr[1];
									AppController.username = arr[2];
									alertPanel.setContent( Constants.accountUIMsg.pwdSuccess() );
									alertPanel.show();
								} else if (result.equals("wrong old password")) {
									accountView.getOlderrorLabel().setText( Constants.accountUIMsg.pwdWrong() );
									accountView.getOlderrorLabel().setVisible(true);
									accountView.getNewerrorLabel().setVisible(false);
									accountView.getVererrorLabel().setVisible(false);
								} else if (result.equals("new password failed")) {
									accountView.getVererrorLabel().setText( Constants.accountUIMsg.pwdFail() );
									accountView.getOlderrorLabel().setVisible(false);
									accountView.getNewerrorLabel().setVisible(false);
									accountView.getVererrorLabel().setVisible(true);
								} else {
									accountView.getVererrorLabel().setText(result);
									accountView.getOlderrorLabel().setVisible(false);
									accountView.getNewerrorLabel().setVisible(false);
									accountView.getVererrorLabel().setVisible(true);
								}
							}
						}

					});
				}
			}
		});
	}

	/**
	 * Verification of information
	 * @return Account account information
	 */
	public Account info_validate() {
		if (accountView.getUsrBox().getValue().length() <= 0) {
			accountView.getUsrerrorLabel().setText( Constants.accountUIMsg.usrEmpty() );
			accountView.getUsrerrorLabel().setVisible(true);
			accountView.getCmperrorLabel().setVisible(false);
			accountView.getPsterrorLabel().setVisible(false);
			accountView.getInfoConfirmBtn().setEnabled(true);
			return null;
		} else
			accountView.getUsrerrorLabel().setText("");

		if (accountView.getCmpBox().getValue().length() <= 0) {
			accountView.getCmperrorLabel().setText( Constants.accountUIMsg.cmpEmpty() );
			accountView.getUsrerrorLabel().setVisible(false);
			accountView.getCmperrorLabel().setVisible(true);
			accountView.getPsterrorLabel().setVisible(false);
			accountView.getInfoConfirmBtn().setEnabled(true);
			return null;
		} else
			accountView.getCmperrorLabel().setText("");

		if (accountView.getPstBox().getValue().length() <= 0) {
			accountView.getPsterrorLabel().setText( Constants.accountUIMsg.pstEmpty() );
			accountView.getUsrerrorLabel().setVisible(false);
			accountView.getCmperrorLabel().setVisible(false);
			accountView.getPsterrorLabel().setVisible(true);
			accountView.getInfoConfirmBtn().setEnabled(true);
			return null;
		} else
			accountView.getPsterrorLabel().setText("");

		Account account = new Account();
		account.setEmail(accountView.getEmail());
		account.setUsername(accountView.getUsrBox().getValue());
		account.setCompany(accountView.getCmpBox().getValue());
		account.setPosition(accountView.getPstBox().getValue());

		return account;
	}

	/**
	 * Password verification
	 * @return String Mail and old and new passwords
	 */
	public String pwd_validate() {
		if (accountView.getOldPwd().getValue().length() <= 0) {
			accountView.getOlderrorLabel().setText( Constants.accountUIMsg.pwdEmpty() );
			accountView.getOlderrorLabel().setVisible(true);
			accountView.getNewerrorLabel().setVisible(false);
			accountView.getVererrorLabel().setVisible(false);
			accountView.getPwdConfirmBtn().setEnabled(true);
			return null;
		} else
			accountView.getOlderrorLabel().setText("");

		if (accountView.getNewPwd().getValue().length() <= 0) {
			accountView.getNewerrorLabel().setText( Constants.accountUIMsg.pwdEmpty() );
			accountView.getOlderrorLabel().setVisible(false);
			accountView.getNewerrorLabel().setVisible(true);
			accountView.getVererrorLabel().setVisible(false);
			accountView.getPwdConfirmBtn().setEnabled(true);
			return null;
		}else if(accountView.getNewPwd().getValue().length()>20){
			accountView.getNewerrorLabel().setText( Constants.accountUIMsg.pwdLength() );
			accountView.getNewerrorLabel().setVisible(true);
			accountView.getOlderrorLabel().setVisible(false);
			accountView.getVererrorLabel().setVisible(false);
			accountView.getPwdConfirmBtn().setEnabled(true);
			return null;
		} else
			accountView.getNewerrorLabel().setText("");

		if (accountView.getOldPwd().getValue().equals(accountView.getNewPwd().getValue())) {
			accountView.getNewerrorLabel().setText( Constants.accountUIMsg.pwdMatch1() );
			accountView.getOlderrorLabel().setVisible(false);
			accountView.getNewerrorLabel().setVisible(true);
			accountView.getVererrorLabel().setVisible(false);
			accountView.getPwdConfirmBtn().setEnabled(true);
			return null;
		} else
			accountView.getNewerrorLabel().setText("");

		if (!accountView.getNewPwd().getValue().equals(accountView.getVerPwd().getValue())) {
			accountView.getVererrorLabel().setText( Constants.accountUIMsg.pwdMatch2() );
			accountView.getOlderrorLabel().setVisible(false);
			accountView.getNewerrorLabel().setVisible(false);
			accountView.getVererrorLabel().setVisible(true);
			accountView.getPwdConfirmBtn().setEnabled(true);
			return null;
		} else
			accountView.getVererrorLabel().setText("");

		String result = accountView.getEmail() + " " + accountView.getOldPwd().getValue() 
				+ " " + accountView.getNewPwd().getValue();

		return result;
	}

	public AccountPresenter(HandlerManager eventBus, AccountView accountView) {
		this.eventBus = eventBus;
		this.accountView = accountView;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(accountView.asWidget());
		init();
	}
}
