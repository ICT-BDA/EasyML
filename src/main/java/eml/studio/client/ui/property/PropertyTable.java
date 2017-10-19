/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.property;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for property table
 */
public abstract class PropertyTable extends ScrollPanel {

	protected Grid grid;
	protected VerticalPanel vp;

	private Map<Property, Label> properties;

	public PropertyTable(int rows, int cols) {
		super();

		grid = new Grid(rows, cols);
		vp = new VerticalPanel();
		this.setAlwaysShowScrollBars(false);
		this.setSize("100%", "80%");
		vp.setBorderWidth(0);
		vp.add(grid);
		this.add(vp);
		properties = new HashMap<Property, Label>();
	}

	public void addProperty(Property p, int row) {
		grid.setWidget(row, 0, new Label(p.getName()));

		Label box = new Label();
		box.setText(p.getValue());
		grid.setWidget(row, 1, box);
		box.setStyleName("propetybox");
		properties.put(p, box);
	}

}