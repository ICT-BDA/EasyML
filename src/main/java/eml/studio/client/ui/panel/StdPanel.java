/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Show std result Panel
 * define a show(msg, title), rather than create a new
 */
public class StdPanel extends PopupPanel {

	public StdPanel(String msg, String title) {
		init(msg, title);
	}

	protected void init(String msg, String title) {
		this.setTitle("stdErr");
		this.setGlassEnabled(true);

		HTML closeButton = new HTML("X");
		closeButton.setSize("10px", "10px");
		closeButton.setStyleName("closebtn");
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				StdPanel.this.hide();
			}
		});

		ScrollPanel scvp = new ScrollPanel();
		VerticalPanel verticalPanel = new VerticalPanel();

		verticalPanel.add(closeButton);
		verticalPanel.setCellHeight(closeButton, "30px");
		verticalPanel.setStyleName("vpanel");
		HTML desc = new HTML(title);
		desc.setStyleName("popupTitle");
		verticalPanel.add(desc);
		verticalPanel.setCellHeight(desc, "30px");

		TextArea label = new TextArea();
		label.setText(msg);
		label.setReadOnly(true);
		label.setSize("650px", "400px");
		verticalPanel.add(label);
		scvp.add(verticalPanel);
		this.add(scvp);
		this.setStyleName("loading_container");
		this.center();
		this.show();
	}
}
