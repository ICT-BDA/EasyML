/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel.Grid;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Sql ETL information grid
 */
public class SqlETLGrid extends ETLGrid {

	protected Label user = new Label("account");
	protected Label password = new Label("password");
	protected Label url = new Label("URL");
	protected TextBox userTB;
	protected TextBox passwordTB;
	protected TextBox urlTB;

	public TextBox getUserTB() {
		return userTB;
	}

	public void setUserTB(String userTB) {
		this.userTB.setText(userTB);
	}

	public TextBox getPasswordTB() {
		return passwordTB;
	}

	public void setPasswordTB(String passwordTB) {
		this.passwordTB.setText(passwordTB);
	}

	public TextBox getUrlTB() {
		return urlTB;
	}

	public void setUrlTB(String urlTB) {
		this.urlTB.setText(urlTB);
	}

	public SqlETLGrid(){
		super();
		init();
	}

	protected void init(){

		//Panel information
		this.setWidget(0,0,url);
		urlTB = new TextBox();
		urlTB.setStyleName("bda-etlpanel-textbox");
		this.setWidget(0,1,urlTB);

		this.setWidget(1,0,user);
		userTB = new TextBox();
		userTB.setStyleName("bda-etlpanel-textbox");
		this.setWidget(1,1,userTB);

		this.setWidget(2,0,password);
		passwordTB = new TextBox();
		passwordTB.setStyleName("bda-etlpanel-textbox");
		this.setWidget(2,1,passwordTB);


	}
}
