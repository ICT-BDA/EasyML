/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import java.util.logging.Logger;

import eml.studio.client.ui.panel.component.DescribeGrid;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

/**
 * The base class panel of all upload, update and edit etc. PopupPanel
 */
public class BasePanel extends PopupPanel implements HasAllKeyHandlers{

	private static Logger logger = Logger.getLogger(BasePanel.class.getName());
	protected VerticalPanel verticalPanel = new VerticalPanel();
	protected FocusPanel focusPanel = new FocusPanel();
	protected HTML closeButton = new HTML("X");
	protected String[][] labarr = {null,null,null,null,null};
	protected String type = null;
	protected DescribeGrid grid = null;
	protected String owner;

	public BasePanel() {
		this.setGlassEnabled(true);
		this.setModal(false);

		closeButton.setSize("10px", "10px");
		closeButton.setStyleName("closebtn");

		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clean();
				BasePanel.this.hide();
			}
		});
		verticalPanel.add(closeButton);
		verticalPanel.setCellHeight(closeButton, "13px");
		verticalPanel.setStyleName("vpanel");
		focusPanel.add(verticalPanel.asWidget());
		this.add(focusPanel);
		this.setStyleName("loading_container");

		this.sinkEvents(Event.ONKEYDOWN);
		focusPanel.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress( KeyPressEvent event) {
				event.getCharCode();
			}
		});

	}

	protected void init() {
		grid = new DescribeGrid(labarr, type);
	}

	@Override
	public void show(){
		super.show();
	}
	public void clean(){
		this.verticalPanel.clear();
		verticalPanel.add(closeButton);
		verticalPanel.setCellHeight(closeButton, "13px");
	}

	public void setValues(String[] values){
		labarr[4] = values;
	}

	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String[][] getLabarr() {
		return labarr;
	}
	public void setLabarr(String[][] labarr) {
		this.labarr = labarr;
	}

	public HTML getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(HTML closeButton) {
		this.closeButton = closeButton;
	}

	public VerticalPanel getVerticalPanel() {
		return verticalPanel;
	}

	public void setVerticalPanel(VerticalPanel verticalPanel) {
		this.verticalPanel = verticalPanel;
	}

	public DescribeGrid getGrid() {
		return grid;
	}

	public void setGrid(DescribeGrid grid) {
		this.grid = grid;
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return this.addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return this.addKeyDownHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return this.addKeyPressHandler(handler);
	}
}
