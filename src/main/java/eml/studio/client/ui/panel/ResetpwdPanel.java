/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import eml.studio.client.rpc.MailService;
import eml.studio.client.rpc.MailServiceAsync;
import eml.studio.client.util.Constants;
import eml.studio.shared.model.Account;

/**
 * Reset password panel
 * 
 */
public class ResetpwdPanel extends PopupPanel {

	protected MailServiceAsync mailService = GWT.create(MailService.class);

	private Label mailLabel = new Label(Constants.logUIMsg.resetEmail());
	private TextBox mailBox = new TextBox();
	private Label mailerrorLabel = new Label();
	private Button cancelBtn = new Button(Constants.logUIMsg.resetCancel());
	private Button sendBtn = new Button(Constants.logUIMsg.resetSend());
	private HorizontalPanel btnHpanel = new HorizontalPanel();
	private VerticalPanel container = new VerticalPanel();

	public ResetpwdPanel() {
		this.setStyleName("bda-login");
		mailBox.setStyleName("form-control");
		mailerrorLabel.addStyleName("bad-login-error");

		cancelBtn.addStyleName("button-style");
		cancelBtn.getElement().getStyle().setMarginLeft(7, Unit.PX);
		cancelBtn.getElement().getStyle().setMarginRight(30, Unit.PX);
		sendBtn.addStyleName("button-style");
		sendBtn.getElement().getStyle().setWidth(100, Unit.PX);
		btnHpanel.add(cancelBtn);
		btnHpanel.add(sendBtn);
		btnHpanel.setStyleName("bda-form-group");
		btnHpanel.getElement().getStyle().setMarginTop(15, Unit.PX);
		btnHpanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		container.add(mailLabel);
		container.add(mailBox);
		container.add(mailerrorLabel);
		container.add(btnHpanel);

		container.setStyleName("bda-login-form");

		this.add(container);

		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mailBox.setValue("");
				mailerrorLabel.setText("");
				ResetpwdPanel.this.hide();
			}

		});

		//Send a verification email
		sendBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if( !mailBox.getValue().matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$") ){
					mailerrorLabel.setText(Constants.resetpwdUIMsg.emailFormatErrorMsg());
				}else{
					String href = Window.Location.getHref();
					if(href.split("=").length > 1){
						href = href.split("=")[1];
					}
					int splitIdx = href.lastIndexOf('/');
					final String base_url = href.substring(0, splitIdx);

					final Account account = new Account();
					account.setEmail(mailBox.getValue());
					mailService.SendMail(base_url, account, "m", new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							mailerrorLabel.setText(caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							if(result.equals("success")){
								mailerrorLabel.setText(Constants.resetpwdUIMsg.sendEmailSuccessMsg());
							}else if(result.equals("send email failed")){
								mailerrorLabel.setText(Constants.resetpwdUIMsg.sendEmailErrMsg());
							}else if(result.equals("email doesn't exist")){
								mailerrorLabel.setText(Constants.resetpwdUIMsg.emailNotExistMsg());
							}else
								mailerrorLabel.setText(result);
						}
					});
				}
			}
		});
	}

}
