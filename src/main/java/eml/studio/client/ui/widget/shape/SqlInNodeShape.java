/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.shape;

import eml.studio.client.ui.widget.BaseWidget;

import com.google.gwt.user.client.ui.TextBox;

/**
 * Sql in node shape in sql script widget
 */
public class SqlInNodeShape extends InNodeShape implements SqlNodeShape{

	private TextBox box = new TextBox();

	public SqlInNodeShape(BaseWidget parent, int portId) {
		super(parent, portId);
	}

	public SqlInNodeShape(BaseWidget parent, int portId, double leftRelative,
			double topRelative) {
		super(parent, portId, leftRelative, topRelative);
	}

	public TextBox getBox(){
		return box;
	}

	@Override
	public String getAliases() {
		return box.getText();
	}

}
