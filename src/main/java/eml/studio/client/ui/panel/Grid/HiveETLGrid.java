/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel.Grid;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Hive ETL Information grid
 */
public class HiveETLGrid extends ETLGrid {
	protected Label urs = new Label("account");
	protected Label password = new Label("password");
	protected Label url = new Label("URL");

	protected TextBox ursTB;
	protected TextBox passwordTB;
	protected TextBox urlTB;

	protected ListBox tableLB;
	protected ListBox columnLB;
	protected ListBox formatLB;
	public HiveETLGrid(){

	}
}
