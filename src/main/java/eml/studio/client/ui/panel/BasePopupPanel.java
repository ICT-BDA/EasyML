/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Base panel class of jocDesc
 */
public class BasePopupPanel extends PopupPanel {
	private static Logger logger = Logger.getLogger(BasePopupPanel.class
			.getName());
	public VerticalPanel verticalPanel = new VerticalPanel();
	public HTML closeButton = new HTML("X");

	public BasePopupPanel() {
		init();
	}

	protected void init() {
		this.setGlassEnabled(true);

		this.setModal(true);
		this.setCloseEnable(true);
		verticalPanel.setStyleName("vpanel");
		this.add(verticalPanel);
		this.setStyleName("loading_container");
	}

	public void setCloseEnable(boolean enable) {
		if (!enable) {
			verticalPanel.remove(closeButton);
		} else {
			closeButton.setSize("10px", "10px");
			closeButton.setStyleName("closebtn");
			closeButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					BasePopupPanel.this.hide();
				}
			});
			verticalPanel.add(closeButton);
			verticalPanel.setCellHeight(closeButton, "13px");
		}
	}
}
