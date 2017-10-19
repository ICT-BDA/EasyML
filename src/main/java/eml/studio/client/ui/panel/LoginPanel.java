/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import eml.studio.client.rpc.AccountService;
import eml.studio.client.rpc.AccountServiceAsync;
import eml.studio.client.util.Constants;
import eml.studio.client.util.UUID;
import eml.studio.client.util.Util;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Login Panel
 */
public class LoginPanel extends PopupPanel {
	protected AccountServiceAsync accountSrv = GWT.create(AccountService.class);

	public final Label errorLabel = new Label();
	protected TextBox emailField = new TextBox();
	protected PasswordTextBox passwordField = new PasswordTextBox();
	final CheckBox checkBox = new CheckBox(Constants.logUIMsg.remember());
	final Label guestLabel = new Label(Constants.logUIMsg.guest());
	final Label forgetLabel = new Label(Constants.logUIMsg.forget());
	final Button signinButton = new Button(Constants.logUIMsg.login().toLowerCase());
	final Button signupButton = new Button(Constants.logUIMsg.register());
	private VerticalPanel loginContainer = new VerticalPanel();
	private HorizontalPanel pwdPanel = new HorizontalPanel();
	private HorizontalPanel btnPanel = new HorizontalPanel();

	protected boolean autoLogin = true;

	public LoginPanel() {
		this.setStyleName("bda-login");
		boolean checked = Boolean.parseBoolean(Cookies.getCookie("bdachecked"));
		checkBox.setValue(checked);

		Label emailLabel = new Label(Constants.logUIMsg.email());
		emailField.setStyleName("form-control");
		emailField.setValue(Cookies.getCookie("bdaemail"));
		Label passwordLabel = new Label(Constants.logUIMsg.password());
		passwordField.setStyleName("form-control");
		if (checked)
			passwordField.setValue(UUID.randomID());

		errorLabel.addStyleName("bad-login-error");

		checkBox.setStyleName("bda-login-checkbox");
		forgetLabel.setStyleName("bda-login-forgetpwd");
		pwdPanel.setStyleName("bda-form-group");
		pwdPanel.add(checkBox);
		pwdPanel.add(forgetLabel);

		signinButton.addStyleName("button-style");
		signinButton.getElement().getStyle().setMarginLeft(7, Unit.PX);
		signinButton.getElement().getStyle().setMarginRight(60, Unit.PX);
		signupButton.addStyleName("button-style");
		btnPanel.setStyleName("bda-form-group");
		btnPanel.add(signinButton);
		btnPanel.add(signupButton);

		HorizontalPanel hp = new HorizontalPanel();
		hp.add(emailLabel);
		hp.add(guestLabel);
		guestLabel.getElement().getStyle().setWidth(100, Unit.PX);
		guestLabel.getElement().getStyle().setTextAlign(TextAlign.RIGHT);
		guestLabel.getElement().getStyle().setMarginLeft(80, Unit.PX);
		guestLabel.getElement().getStyle().setCursor(Cursor.POINTER);
		loginContainer.add(hp);
		loginContainer.add(emailField);
		loginContainer.add(passwordLabel);
		loginContainer.add(passwordField);
		loginContainer.add(errorLabel);
		loginContainer.add(pwdPanel);
		loginContainer.add(btnPanel);

		loginContainer.setStyleName("bda-login-form");
		loginContainer.setBorderWidth(0);
		loginContainer.setStyleName((String) null);

		this.add(loginContainer);

		passwordField.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				autoLogin = false;
			}

		});

		checkBox.addValueChangeHandler(new ValueChangeHandler() {

			@Override
			public void onValueChange(ValueChangeEvent event) {
				// Window.alert( checkBox.getValue().toString() );
				Cookies.setCookie("bdachecked", checkBox.getValue().toString(), Util.getCookieExpireDate());
				if (!getRemmenber()) {
					Cookies.removeCookie("bdaserial");
					Cookies.removeCookie("bdatoken");
				}
			}

		});
	}

	public void addGuestLoginHandler(ClickHandler handler){
		guestLabel.addClickHandler(handler);
	}

	public void addSignInHandler(ClickHandler handler) {
		signinButton.addClickHandler(handler);
	}

	public void addSignUpHandler(ClickHandler handler) {
		signupButton.addClickHandler(handler);
	}

	public void addForgetPwdHandler(ClickHandler handler){
		forgetLabel.addClickHandler(handler);
	}

	public String getEmail() {
		return emailField.getValue();
	}

	public String getPassword() {
		return passwordField.getValue();
	}

	public boolean getRemmenber() {
		return checkBox.getValue();
	}

	public boolean getAutoLogin() {
		return autoLogin && getRemmenber();
	}
}
