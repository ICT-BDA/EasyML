/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.presenter;

import eml.studio.client.mvp.AppController;
import eml.studio.client.mvp.view.RegisterView;
import eml.studio.client.rpc.AccountService;
import eml.studio.client.rpc.AccountServiceAsync;
import eml.studio.client.ui.panel.AlertPanel;
import eml.studio.client.util.Constants;
import eml.studio.shared.model.Account;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * User registration
 */
public class RegisterPresenter implements Presenter {
	protected AccountServiceAsync accountService = GWT.create(AccountService.class);
	protected AlertPanel alertPanel = new AlertPanel();
	private final HandlerManager eventBus;
	private final RegisterView registerView;

	/**
	 * Event binding
	 */
	public void bind() {
		alertPanel.getClose().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				AppController.redirect("index.html");
			}

		});

		alertPanel.getConfirmBtn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				AppController.redirect("index.html");
			}

		});

		//Registration information, confirmation button
		registerView.getConfirmBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				Account account = validate();
				if (account != null) {
					accountService.register(account, new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							registerView.getPsterrorLabel().setText(caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							if(result != null){
								String arr[] = result.split(" ");
								if(arr[0].equals("[success]")){
									alertPanel.setContent(Constants.registerUIMsg.registerSuccessMsg());
									alertPanel.show();
								}else if(result.equals("username error")){
									registerView.getUsrerrorLabel().setText(Constants.registerUIMsg.usernameExistMsg());
								}else
									registerView.getPsterrorLabel().setText(result);
							}
						}

					});
				}
			}
		});
	}

	/**
	 * Registration information verification
	 * @return Account
	 */
	public Account validate() {
		if (registerView.getUsernameBox().getValue().length() <= 0) {
			registerView.getUsrerrorLabel().setText(Constants.registerUIMsg.usernameEmptyMsg());
			registerView.getUsrerrorLabel().setVisible(true);
			registerView.getPwderrorLabel().setVisible(false);
			registerView.getVererrorLabel().setVisible(false);
			registerView.getCmperrorLabel().setVisible(false);
			registerView.getPsterrorLabel().setVisible(false);
			registerView.getConfirmBtn().setEnabled(true);
			return null;
		} else
			registerView.getUsrerrorLabel().setText("");

		if (registerView.getPasswordBox().getValue().length() <= 0) {
			registerView.getPwderrorLabel().setText(Constants.registerUIMsg.passwordEmptyMsg());
			registerView.getPwderrorLabel().setVisible(true);
			registerView.getUsrerrorLabel().setVisible(false);
			registerView.getVererrorLabel().setVisible(false);
			registerView.getCmperrorLabel().setVisible(false);
			registerView.getPsterrorLabel().setVisible(false);
			registerView.getConfirmBtn().setEnabled(true);
			return null;
		}else if(registerView.getPasswordBox().getValue().length()>20){
			registerView.getPwderrorLabel().setText(Constants.registerUIMsg.passwordLengthMsg());
			registerView.getPwderrorLabel().setVisible(true);
			registerView.getUsrerrorLabel().setVisible(false);
			registerView.getVererrorLabel().setVisible(false);
			registerView.getCmperrorLabel().setVisible(false);
			registerView.getPsterrorLabel().setVisible(false);
			registerView.getConfirmBtn().setEnabled(true);
			return null;
		} else
			registerView.getPwderrorLabel().setText("");

		if (!registerView.getPasswordBox().getValue().equals(registerView.getVerpwdBox().getValue())) {
			registerView.getVererrorLabel().setText(Constants.registerUIMsg.passwordMismatchMsg());
			registerView.getVererrorLabel().setVisible(true);
			registerView.getUsrerrorLabel().setVisible(false);
			registerView.getPwderrorLabel().setVisible(false);
			registerView.getCmperrorLabel().setVisible(false);
			registerView.getPsterrorLabel().setVisible(false);
			registerView.getConfirmBtn().setEnabled(true);
			return null;
		} else
			registerView.getVererrorLabel().setText("");

		if (registerView.getCompanyBox().getValue().length() <= 0) {
			registerView.getCmperrorLabel().setText(Constants.registerUIMsg.companyEmptyMsg());
			registerView.getCmperrorLabel().setVisible(true);
			registerView.getUsrerrorLabel().setVisible(false);
			registerView.getPwderrorLabel().setVisible(false);
			registerView.getVererrorLabel().setVisible(false);
			registerView.getPsterrorLabel().setVisible(false);
			registerView.getConfirmBtn().setEnabled(true);
			return null;
		} else
			registerView.getCmperrorLabel().setText("");

		if (registerView.getPositionBox().getValue().length() <= 0) {
			registerView.getPsterrorLabel().setText(Constants.registerUIMsg.positionEmptyMsg());
			registerView.getPsterrorLabel().setVisible(true);
			registerView.getUsrerrorLabel().setVisible(false);
			registerView.getPwderrorLabel().setVisible(false);
			registerView.getVererrorLabel().setVisible(false);
			registerView.getCmperrorLabel().setVisible(false);
			registerView.getConfirmBtn().setEnabled(true);
			return null;
		} else
			registerView.getPsterrorLabel().setText("");

		Account account = new Account();
		account.setEmail(registerView.getEmail());
		account.setUsername(registerView.getUsernameBox().getValue());
		account.setPassword(registerView.getPasswordBox().getValue());
		account.setCompany(registerView.getCompanyBox().getValue());
		account.setPosition(registerView.getPositionBox().getValue());
		account.setVerifylink(AppController.verifylink);
		account.setPower("011");

		return account;
	}

	public RegisterPresenter(HandlerManager eventBus, RegisterView registerView) {
		this.eventBus = eventBus;
		this.registerView = registerView;
	}

	@Override
	public void go(HasWidgets container) { 
		bind();
		container.clear();
		container.add(registerView.asWidget());
	}
}
