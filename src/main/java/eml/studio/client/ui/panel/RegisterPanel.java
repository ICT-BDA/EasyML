/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import eml.studio.client.rpc.AccountService;
import eml.studio.client.rpc.AccountServiceAsync;
import eml.studio.client.rpc.MailService;
import eml.studio.client.rpc.MailServiceAsync;
import eml.studio.client.util.Constants;
import eml.studio.shared.model.Account;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Registration panel
 * 
 */
public class RegisterPanel extends PopupPanel {

	protected AccountServiceAsync accountSrv = GWT.create(AccountService.class);
	protected MailServiceAsync mailSrv = GWT.create(MailService.class);

	TextBox mailbox = new TextBox();
	TextBox validateBox = new TextBox();
	Label mailerrorLabel;

	Button confirmBtn = new Button(Constants.logUIMsg.registerConfirm());
	Button verityBtn = new Button(Constants.logUIMsg.registerVerify());

	HorizontalPanel btnPanel = new HorizontalPanel();
	VerticalPanel vpanel = new VerticalPanel();

	public RegisterPanel() {
		this.setStyleName("bda-login");
		vpanel.setStyleName("bda-login-form");

		Label mailLabel = new Label(Constants.logUIMsg.registerEmail());
		mailbox.setStyleName("form-control");
		mailerrorLabel = new Label();
		mailerrorLabel.addStyleName("bad-login-error");

		Button cancelBtn = new Button(Constants.logUIMsg.registerCancel());
		cancelBtn.getElement().getStyle().setMarginLeft(7, Unit.PX);
		cancelBtn.getElement().getStyle().setMarginRight(60, Unit.PX);
		cancelBtn.addStyleName("button-style");
		verityBtn.addStyleName("button-style");

		btnPanel.setStyleName("bda-form-group");
		btnPanel.getElement().getStyle().setMarginTop(15, Unit.PX);
		btnPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		btnPanel.add(cancelBtn);
		btnPanel.add(verityBtn);

		vpanel.add(mailLabel);
		vpanel.add(mailbox);
		vpanel.add(mailerrorLabel);
		vpanel.add(btnPanel);

		this.add(vpanel);


		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mailbox.setValue("");
				mailerrorLabel.setText("");
				RegisterPanel.this.hide();
			}

		});

		//Send a verification email
		verityBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if( !mailbox.getValue().matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$") ){
					mailerrorLabel.setText(Constants.registerUIMsg.emailFormatErrorMsg());
				}else{
					String href = Window.Location.getHref();
					if(href.split("=").length > 1){
						href = href.split("=")[1];
					}
					int splitIdx = href.lastIndexOf('/');
					final String base_url = href.substring(0, splitIdx);

					final Account account = new Account();
					account.setEmail(mailbox.getValue());
					mailSrv.SendMail(base_url, account, "r", new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							mailerrorLabel.setText(caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							if(result.equals("email existed")){
								mailerrorLabel.setText( Constants.registerUIMsg.emailHasRegisteredMsg());
							}else if(result.equals("send email failed")){
								mailerrorLabel.setText(Constants.registerUIMsg.verifyEmailErrMsg());
							}else if(result.equals("success")){
								mailerrorLabel.setText(Constants.registerUIMsg.verifyEmailSuccessMsg());
							}else
								mailerrorLabel.setText(result);
						}		
					});
				}
			}
		});
	}
}
