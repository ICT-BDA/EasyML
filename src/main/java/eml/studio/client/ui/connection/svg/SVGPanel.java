/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.connection.svg;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.logging.Logger;

/**
 * SVG Panel use to draw lines {@link SVGLine}.
 */
public class SVGPanel extends Composite {
	private static Logger logger = Logger.getLogger(SVGPanel.class.getName());
	interface SVGPanelUiBinder extends UiBinder<HTMLPanel, SVGPanel> {
	}

	private static SVGPanel.SVGPanelUiBinder ourUiBinder = GWT
			.create(SVGPanel.SVGPanelUiBinder.class);

	@UiField
	Element svg;

	public SVGPanel() {
		initWidget(ourUiBinder.createAndBindUi(this));
	}

	public void addElement(Element elem) {
		svg.appendChild(elem);
	}

	public void removeElement(Element elem) {
		svg.removeChild(elem);
	}
}