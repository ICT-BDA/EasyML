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
 * Password reset page layout
 * 
 */
public class ResetpwdView extends Composite {

  interface ResetpwdViewUiBinder extends UiBinder<Widget, ResetpwdView> {
  }

  private static ResetpwdViewUiBinder ourUiBinder = GWT.create(ResetpwdViewUiBinder.class);

  @UiField
  Anchor resetpwd;
  
  @UiField
  Label resetPwd;
  
  @UiField
  Label resetVer;
  
  @UiField
  PasswordTextBox pwdBox;
  
  @UiField
  PasswordTextBox verBox;
  
  @UiField
  Label pwderrorLabel;
  
  @UiField
  Label vererrorLabel;

  @UiField
  Button confirmBtn;
  
  @UiField
  HTMLPanel container;
  
  public ResetpwdView() {
    initWidget(ourUiBinder.createAndBindUi(this));

	resetpwd.setText( Constants.resetpwdUIMsg.resetpwd());
	resetPwd.setText( Constants.resetpwdUIMsg.resetPwd());
	resetVer.setText( Constants.resetpwdUIMsg.resetVer());
	
	confirmBtn.removeStyleName("gwt-Button");
	confirmBtn.setText( Constants.resetpwdUIMsg.confirm() );
	confirmBtn.getElement().getStyle().setMarginTop(100, Unit.PX);
	confirmBtn.getElement().getStyle().setMarginLeft(100, Unit.PX);
	confirmBtn.getElement().getStyle().setMarginBottom(200, Unit.PX);
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