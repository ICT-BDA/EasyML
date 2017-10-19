/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.view;

import eml.studio.client.util.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Navigation bar layout
 * 
 */
public class HeaderView extends Composite {

	interface HeaderViewUiBinder extends UiBinder<Widget, HeaderView> {
	}

	private static HeaderViewUiBinder ourUiBinder = GWT.create(HeaderViewUiBinder.class);

	@UiField
	Anchor userAnchor;

	@UiField
	Anchor logout;

	@UiField
	Anchor newJobAnchor;

	@UiField
	Anchor dataAnchor;

	@UiField
	Anchor progAnchor;

	@UiField
	Anchor userProg;

	@UiField
	Anchor userData;

	@UiField
	Anchor userList;

	@UiField
	Anchor notebook;

	@UiField
	HTMLPanel navMenu;

	@UiField
	Anchor adminAnchor;

	@UiField
	Anchor workStage;

	public HeaderView() {
		initWidget(ourUiBinder.createAndBindUi(this));
		init();
	}

	public void init(){
		newJobAnchor.setText( Constants.headerUIMsg.createJob() );
		dataAnchor.setText( Constants.headerUIMsg.uploadData() );
		progAnchor.setText( Constants.headerUIMsg.uploadProgram() );
		userData.setText( Constants.headerUIMsg.dataManagement() );
		userProg.setText( Constants.headerUIMsg.progManagement());
		userList.setText(Constants.headerUIMsg.userManagement());
		notebook.setText( Constants.headerUIMsg.notebook());

		workStage.setText(Constants.headerUIMsg.workStage());
		adminAnchor.setText( Constants.headerUIMsg.manager());
		logout.setText( Constants.headerUIMsg.logout());
	}

	public void setAccount(String usernameAccount, String emailAccount){
		if( usernameAccount == "" || usernameAccount == null){
			userAnchor.setText(emailAccount);
		}else{
			userAnchor.setText(usernameAccount);
		}
	}

	public Anchor getLogout() {
		return logout;
	}

	public Anchor getNewJobAnchor() {
		return newJobAnchor;
	}

	public Anchor getProgAnchor() {
		return progAnchor;
	}

	public Anchor getDataAnchor() {
		return dataAnchor;
	}

	public Anchor getUserAnchor(){
		return userAnchor;
	}

	public Anchor getUserProg(){
		return userProg;
	}

	public Anchor getUserData(){
		return userData;
	}

	public Anchor getUserList(){
		return userList;
	}

	public HTMLPanel getNavMenu(){
		return navMenu;
	}

	public Anchor getAdminAnchor(){
		return adminAnchor;
	}

	public Anchor getWorkStage(){
		return workStage;
	}
}