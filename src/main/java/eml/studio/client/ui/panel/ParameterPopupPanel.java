/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import eml.studio.client.ui.widget.panel.ParameterPanel;
import eml.studio.client.util.Constants;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Popup panel that describe the parameters
 */
public class ParameterPopupPanel extends BasePopupPanel {

	protected Button submitBtn = new Button(Constants.studioUIMsg.confirm());
	protected ParameterPanel panel;

	HorizontalPanel hpanel = new HorizontalPanel();

	public ParameterPopupPanel() {
		submitBtn.removeStyleName("gwt-Button");
		submitBtn.addStyleName("button-style");
		submitBtn.getElement().getStyle().setMarginLeft(180, Unit.PX);
		submitBtn.getElement().getStyle().setMarginTop(20, Unit.PX);
		submitBtn.getElement().getStyle().setMarginBottom(-20, Unit.PX);
		hpanel.setStyleName("bda-newjob-hpanel");
		hpanel.add(submitBtn);
		verticalPanel.add(hpanel);
		verticalPanel.addStyleName("bda-parameter-vpanel");
		this.setCloseEnable(false);
	}

	public void addSubmitHandler(ClickHandler handler) {
		submitBtn.addClickHandler(handler);
	}

	public void setParameterPanel(ParameterPanel panel) {
		if (panel == null)
			return;
		verticalPanel.clear();
		verticalPanel.add(panel);
		verticalPanel.add(submitBtn);
		this.panel = panel;
	}

	public ParameterPanel getPanel() {
		return panel;
	}

}