/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import eml.studio.client.util.Constants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Job Desc confirm Panel
 */
public class JobDescPopupPanel extends BasePopupPanel {
	private TextBox namebox = new TextBox();
	private TextArea descArea = new TextArea();
	private Label errorLabel = new Label();

	Button submitBtn = new Button(Constants.studioUIMsg.confirm());

	public JobDescPopupPanel(String title) {

		Label label = new Label(title);
		label.setStyleName("bda-newjob-head");
		verticalPanel.add(label);
		verticalPanel.add(createGrid());
		HorizontalPanel hpanel = new HorizontalPanel();

		hpanel.setStyleName("bda-newjob-hpanel");
		verticalPanel.add(errorLabel);
		Button cancelBtn = new Button(Constants.studioUIMsg.cancel());
		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				JobDescPopupPanel.this.hide();
			}

		});

		hpanel.add(submitBtn);
		hpanel.add(cancelBtn);
		submitBtn.removeStyleName("gwt-Button");
		cancelBtn.removeStyleName("gwt-Button");
		submitBtn.addStyleName("button-style");
		cancelBtn.addStyleName("button-style");
		errorLabel.setStyleName("error-label");
		verticalPanel.add(hpanel);
		verticalPanel.addStyleName("bda-newjob");
		this.setCloseEnable(false);
	}

	/**
	 * Create a Grid that describes the job
	 */
	@SuppressWarnings("deprecation")
	private Grid createGrid() {
		Grid grid = new Grid(2, 2);
		grid.setWidth("280px");
		grid.setWidget(0, 0, new Label(Constants.studioUIMsg.jobName()));
		/**
		 * Panel information
		 */

		namebox.setStyleName("boxstyle");
		grid.setWidget(0, 1, namebox);
		grid.setWidget(1, 0, new Label(Constants.studioUIMsg.jobDescription()));

		SubmitListener sl = new SubmitListener();
		namebox.addKeyboardListener(sl);

		descArea.setStyleName("boxstyle");
		descArea.setHeight("auto");
		grid.setWidget(1, 1, descArea);
		grid.setStyleName("bda-newjob-grid");
		return grid;
	}

	public void setJobName(String name) {
		namebox.setText(name);
	}

	public void setJobDesc(String desc) {
		descArea.setText(desc);
	}

	public String getJobName() {
		return namebox.getText();
	}

	public String getJobDesc() {
		return descArea.getText();
	}

	public HasClickHandlers getSubmitBtn() {
		return submitBtn;
	}

	public void setErrorMsg(String msg) {
		errorLabel.setText(msg);
	}

	private class SubmitListener extends KeyboardListenerAdapter {
		@Override
		public void onKeyPress(Widget sender, char key, int mods) {
			if (KeyboardListener.KEY_ENTER == key)
				submitBtn.click();
		}
	}

}
