/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.view;

import eml.studio.client.mvp.AppController;
import eml.studio.client.util.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Registration page layout
 * 
 */
public class RegisterView extends Composite {

	interface ResetpwdViewUiBinder extends UiBinder<Widget, RegisterView> {
	}

	private static ResetpwdViewUiBinder ourUiBinder = GWT.create(ResetpwdViewUiBinder.class);

	@UiField
	Anchor register;

	@UiField
	Label registerUsr;

	@UiField
	Label registerPwd;

	@UiField
	Label registerVer;

	@UiField
	Label registerCmp;

	@UiField
	Label registerPst;

	@UiField
	TextBox usrBox;

	@UiField
	Label usrerrorLabel;

	@UiField
	PasswordTextBox pwdBox;

	@UiField
	Label pwderrorLabel;

	@UiField
	PasswordTextBox verBox;

	@UiField
	Label vererrorLabel;

	@UiField
	TextBox cmpBox;

	@UiField
	Label cmperrorLabel;

	@UiField
	TextBox pstBox;

	@UiField
	Label psterrorLabel;

	@UiField
	Button confirmBtn;

	@UiField
	HTMLPanel container;

	public RegisterView() {
		initWidget(ourUiBinder.createAndBindUi(this));

		register.setText( Constants.registerUIMsg.register() );
		registerUsr.setText( Constants.registerUIMsg.registerUsr() );
		registerPwd.setText( Constants.registerUIMsg.registerPwd() );
		registerVer.setText( Constants.registerUIMsg.registerVer() );
		registerCmp.setText( Constants.registerUIMsg.registerCmp() );
		registerPst.setText( Constants.registerUIMsg.registerPst() );

		confirmBtn.removeStyleName("gwt-Button");
		confirmBtn.setText( Constants.registerUIMsg.confirm() );
		confirmBtn.getElement().getStyle().setMarginLeft(100, Unit.PX);
		confirmBtn.getElement().getStyle().setMarginTop(50, Unit.PX);
		confirmBtn.getElement().getStyle().setMarginBottom(100, Unit.PX);
	}

	public TextBox getUsernameBox(){
		return usrBox;
	}

	public Label getUsrerrorLabel(){
		return usrerrorLabel;
	}

	public TextBox getPasswordBox(){
		return pwdBox;
	}

	public Label getPwderrorLabel(){
		return pwderrorLabel;
	}

	public TextBox getVerpwdBox(){
		return verBox;
	}

	public Label getVererrorLabel(){
		return vererrorLabel;
	}

	public TextBox getCompanyBox(){
		return cmpBox;
	}

	public Label getCmperrorLabel(){
		return cmperrorLabel;
	}

	public TextBox getPositionBox(){
		return pstBox;
	}

	public Label getPsterrorLabel(){
		return psterrorLabel;
	}

	public Button getConfirmBtn() {
		return confirmBtn;
	}  

	public String getUsername(){
		return AppController.username;
	}

	public String getEmail(){
		return AppController.email;
	}
}