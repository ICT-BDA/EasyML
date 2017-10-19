/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel.Grid;

import eml.studio.client.util.Constants;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * MonitorJobDescGrid Panel information
 */
public class MonitorJobDescGrid extends Grid {

	private TextBox namebox = new TextBox();
	private TextBox userbox = new TextBox();
	private TextBox jobStatusBox = new TextBox();
	private TextBox bdaJobIdBox = new TextBox();
	private TextBox startTimeBox = new TextBox();
	private TextBox useTimeBox = new TextBox();
	private TextBox endTimeBox = new TextBox();
	private TextArea descBox = new TextArea();

	/**
	 * Init Grid
	 */
	private void init() {
		int i = 0;
		this.setWidth("280px");

		this.setWidget(i, 0, new Label(Constants.studioUIMsg.jobName()));
		namebox.setEnabled(true);
		namebox.setStyleName("boxstyle");
		this.setWidget(i++, 1, namebox);

		this.setWidget(i, 0, new Label(Constants.studioUIMsg.jobOwner()));
		userbox.setEnabled(false);
		userbox.setStyleName("boxstyle");
		this.setWidget(i++, 1, userbox);

		this.setWidget(i, 0, new Label(Constants.studioUIMsg.jobId()));
		bdaJobIdBox.setEnabled(false);
		bdaJobIdBox.setStyleName("boxstyle");
		this.setWidget(i++, 1, bdaJobIdBox);

		this.setWidget(i, 0, new Label(Constants.studioUIMsg.jobStatus()));
		jobStatusBox.setEnabled(false);
		jobStatusBox.setStyleName("boxstyle");
		this.setWidget(i++, 1, jobStatusBox);

		this.setWidget(i, 0, new Label(Constants.studioUIMsg.startTime()));
		startTimeBox.setEnabled(false);
		startTimeBox.setStyleName("boxstyle");
		this.setWidget(i++, 1, startTimeBox);

		this.setWidget(i, 0, new Label(Constants.studioUIMsg.endTime()));
		endTimeBox.setEnabled(false);
		endTimeBox.setStyleName("boxstyle");
		this.setWidget(i++, 1, endTimeBox);

		this.setWidget(i, 0, new Label(Constants.studioUIMsg.useTime()));
		useTimeBox.setEnabled(false);
		useTimeBox.setStyleName("boxstyle");
		this.setWidget(i++, 1, useTimeBox);

		this.setWidget(i, 0, new Label(Constants.studioUIMsg.jobDescription()));
		descBox.setEnabled(true);
		descBox.setStyleName("boxstyle");
		descBox.setHeight("auto");
		this.setWidget(i++, 1, descBox);
	}

	public void clearBoxs() {
		namebox.setText("");
		userbox.setText("");
		bdaJobIdBox.setText("");
		jobStatusBox.setText("");
		startTimeBox.setText("");
		useTimeBox.setText("");
		endTimeBox.setText("");
		descBox.setText("");
	}
	public MonitorJobDescGrid() {
		super(9, 2);
		init();
	}

	public String getJobName() {
		return namebox.getText();
	}

	public void setJobName(String name) {
		if (name == null)
			name = "";
		namebox.setText(name);
	}

	public void setEmailAccount(String email) {
		if (email == null)
			email = "";
		userbox.setText(email);
	}

	public String getEmailAccount() {
		return userbox.getText();
	}

	public void setJobStatus(String status) {
		if (status == null)
			status = "";
		jobStatusBox.setText(status);
	}

	public String getJobStatus() {
		return jobStatusBox.getText();
	}

	public void setStartTime(String time) {
		if (time == null)
			time = "";
		startTimeBox.setText(time);
	}

	public String getStartTime() {
		return startTimeBox.getText();
	}

	public void setUseTime(String time) {
		if (time == null)
			time = "";
		useTimeBox.setText(time);
	}

	public String getUseTime() {
		return useTimeBox.getText();
	}

	public void setEndTime(String time) {
		if (time == null)
			time = "";
		endTimeBox.setText(time);
	}

	public String getEndTime() {
		return endTimeBox.getText();
	}

	public void setDescription(String desc) {
		if (desc == null)
			desc = "";
		descBox.setText(desc);
	}

	public String getDescription() {
		return descBox.getText();
	}

	public String getBdaJobID() {
		return bdaJobIdBox.getText();
	}

	public void setBdaJobID(String bdaJobID) {
		if (bdaJobID == null)
			bdaJobID = "";
		bdaJobIdBox.setText(bdaJobID);
	}
}
