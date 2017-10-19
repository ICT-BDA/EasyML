/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.presenter;

import eml.studio.client.mvp.AppController;
import eml.studio.client.mvp.view.ResetpwdView;
import eml.studio.client.rpc.AccountService;
import eml.studio.client.rpc.AccountServiceAsync;
import eml.studio.shared.model.Account;
import eml.studio.client.ui.panel.AlertPanel;
import eml.studio.client.util.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Reset Password（forget password）
 */
public class ResetpwdPresenter implements Presenter {
	protected AccountServiceAsync accountService = GWT.create(AccountService.class);
	protected AlertPanel alertPanel = new AlertPanel();
	private final HandlerManager eventBus;
	private final ResetpwdView resetpwdView;


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

		resetpwdView.getConfirmBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				Account account = validate();
				if (account != null) {
					accountService.resetPassword(account, new AsyncCallback<Account>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							resetpwdView.getVererrorLabel().setText(caught.getMessage());
						}

						@Override
						public void onSuccess(Account result) {
							// TODO Auto-generated method stub
							if(result != null){
								AppController.username = result.getUsername();
								AppController.email = result.getEmail();
								alertPanel.setContent(Constants.resetpwdUIMsg.passwordResetSuccessMsg());
								alertPanel.show();
							}else
								resetpwdView.getVererrorLabel().setText(Constants.resetpwdUIMsg.otherErrMsg());
						}

					});
				}
			}
		});
	}

	/**
	 * Password verification
	 * @return  Account
	 */
	public Account validate() {
		if (resetpwdView.getPasswordBox().getValue().length() <= 0) {
			resetpwdView.getPwderrorLabel().setText(Constants.resetpwdUIMsg.passwordEmptyMsg());
			resetpwdView.getPwderrorLabel().setVisible(true);
			resetpwdView.getVererrorLabel().setVisible(false);
			resetpwdView.getConfirmBtn().setEnabled(true);
			return null;
		}else if(resetpwdView.getPasswordBox().getValue().length()>20){
			resetpwdView.getPwderrorLabel().setText(Constants.resetpwdUIMsg.passwordLengthMsg());
			resetpwdView.getPwderrorLabel().setVisible(true);
			resetpwdView.getVererrorLabel().setVisible(false);
			resetpwdView.getConfirmBtn().setEnabled(true);
			return null;
		} else
			resetpwdView.getPwderrorLabel().setText("");

		if (!resetpwdView.getPasswordBox().getValue().equals(resetpwdView.getVerpwdBox().getValue())) {
			resetpwdView.getVererrorLabel().setText(Constants.resetpwdUIMsg.passwordMismatchMsg());
			resetpwdView.getVererrorLabel().setVisible(true);
			resetpwdView.getPwderrorLabel().setVisible(false);
			resetpwdView.getConfirmBtn().setEnabled(true);
			return null;
		} else
			resetpwdView.getVererrorLabel().setText("");

		Account account = new Account();
		account.setEmail(resetpwdView.getEmail());
		account.setPassword(resetpwdView.getPasswordBox().getValue());
		account.setVerifylink(AppController.verifylink);

		return account;
	}

	public ResetpwdPresenter(HandlerManager eventBus, ResetpwdView resetpwdView) {
		this.eventBus = eventBus;
		this.resetpwdView = resetpwdView;
	}

	@Override
	public void go(HasWidgets container) { 
		bind();
		container.clear();
		container.add(resetpwdView.asWidget());
	}

}
