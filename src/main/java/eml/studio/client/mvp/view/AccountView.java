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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * Personal information page layout
 */
public class AccountView extends Composite {

	interface AccountViewUiBinder extends UiBinder<Widget, AccountView> {
	}

	private static AccountViewUiBinder ourUiBinder = GWT.create(AccountViewUiBinder.class);

	@UiField
	Anchor workStage; 

	@UiField
	Anchor adminAnchor;

	@UiField
	Anchor userAnchor;

	@UiField
	Anchor logout;

	@UiField
	Anchor navInf;

	@UiField
	Anchor navPwd;

	@UiField
	Label usernameLabel;

	@UiField
	Label companyLabel;

	@UiField
	Label positionLabel;

	@UiField
	TextBox usrBox;

	@UiField
	TextBox cmpBox;

	@UiField
	TextBox pstBox;

	@UiField
	Label usrerrorLabel;

	@UiField
	Label cmperrorLabel;

	@UiField
	Label psterrorLabel;

	@UiField
	Button infoConfirmBtn;

	@UiField
	VerticalPanel infoPanel;

	@UiField
	Label oldpwdLabel;

	@UiField
	Label newpwdLabel;

	@UiField
	Label verpwdLabel;

	@UiField  
	PasswordTextBox oldPwd;  

	@UiField  
	PasswordTextBox newPwd;  

	@UiField  
	PasswordTextBox verPwd;  

	@UiField
	Label olderrorLabel;

	@UiField
	Label newerrorLabel;

	@UiField
	Label vererrorLabel;

	@UiField
	Button pwdConfirmBtn;

	@UiField  
	VerticalPanel pwdPanel;

	@UiField
	HTMLPanel container;

	public AccountView() {
		initWidget(ourUiBinder.createAndBindUi(this));

		workStage.setText( Constants.headerUIMsg.workStage() );
		adminAnchor.setText( Constants.headerUIMsg.manager() );
		logout.setText( Constants.headerUIMsg.logout() );
		navInf.setText( Constants.accountUIMsg.information() );
		navPwd.setText( Constants.accountUIMsg.account() );
		usernameLabel.setText( Constants.accountUIMsg.username() );
		companyLabel.setText( Constants.accountUIMsg.company() );
		positionLabel.setText( Constants.accountUIMsg.position() );
		oldpwdLabel.setText( Constants.accountUIMsg.oldpwd() );
		newpwdLabel.setText( Constants.accountUIMsg.newpwd() );
		verpwdLabel.setText( Constants.accountUIMsg.verpwd() );

		navInf.addStyleName("account-nav-selected");
		infoConfirmBtn.removeStyleName("gwt-Button");
		infoConfirmBtn.setText( Constants.accountUIMsg.confirm() );
		infoConfirmBtn.getElement().getStyle().setMarginTop(70, Unit.PX);
		infoConfirmBtn.getElement().getStyle().setMarginLeft(100, Unit.PX);
		pwdConfirmBtn.removeStyleName("gwt-Button");
		pwdConfirmBtn.setText( Constants.accountUIMsg.confirm() );
		pwdConfirmBtn.getElement().getStyle().setMarginTop(70, Unit.PX);
		pwdConfirmBtn.getElement().getStyle().setMarginLeft(100, Unit.PX);

		infoPanel.setVisible(true);
		pwdPanel.setVisible(false);
	}

	public void setAccount(String usernameAccount, String emailAccount){
		if( usernameAccount == "" || usernameAccount == null){
			userAnchor.setText(emailAccount);
		}else{
			userAnchor.setText(usernameAccount);
		}
	}

	public Anchor getAdminAnchor(){
		return adminAnchor;
	}

	public Anchor getWorkStage(){
		return workStage;
	}

	public Anchor getUserAnchor(){
		return userAnchor;
	}

	public Anchor getLogout() {
		return logout;
	}

	public Anchor getNavInfo(){
		return navInf;
	}

	public Anchor getNavPwd(){
		return navPwd;
	}

	public TextBox getUsrBox(){
		return usrBox;
	}

	public Label getUsrerrorLabel(){
		return usrerrorLabel;
	}

	public TextBox getCmpBox(){
		return cmpBox;
	}

	public Label getCmperrorLabel(){
		return cmperrorLabel;
	}

	public TextBox getPstBox(){
		return pstBox;
	}

	public Label getPsterrorLabel(){
		return psterrorLabel;
	}

	public Button getInfoConfirmBtn() {
		return infoConfirmBtn;
	}  

	public VerticalPanel getInfoPanel(){
		return infoPanel;
	}

	public PasswordTextBox getOldPwd(){
		return oldPwd;
	}

	public Label getOlderrorLabel(){
		return olderrorLabel;
	}

	public PasswordTextBox getNewPwd(){
		return newPwd;
	}

	public Label getNewerrorLabel(){
		return newerrorLabel;
	}

	public PasswordTextBox getVerPwd(){
		return verPwd;
	}

	public Label getVererrorLabel(){
		return vererrorLabel;
	}

	public Button getPwdConfirmBtn(){
		return pwdConfirmBtn;
	}

	public VerticalPanel getPwdPanel(){
		return pwdPanel;
	}

	public String getUsername(){
		return AppController.username;
	}

	public String getEmail(){
		return AppController.email;
	}
}