/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.panel;

import java.util.ArrayList;
import java.util.List;

import eml.studio.client.ui.widget.command.ValueInvalidException;
import eml.studio.client.util.Constants;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/** 
 * Parameter panel for a program
 */
public abstract class ParameterPanel extends VerticalPanel {

	// store the values of TextBox for value check
	List<FocusWidget> paramBoxs = new ArrayList<FocusWidget>();
	protected Grid paramsGrid;

	public List<FocusWidget> getParamBoxs() {
		return paramBoxs;
	}

	public void setParamBoxs(List<FocusWidget> paramBoxs) {
		this.paramBoxs = paramBoxs;
	}

	protected void initGridHead(int size){
		this.setSpacing(3);
		paramsGrid = new Grid(size, 3);

		paramsGrid.setStyleName("gridstyle");
		paramsGrid.setBorderWidth(1);
		paramsGrid.setWidth("250px");
		Label nameLabel = new Label(Constants.studioUIMsg.parameter());
		nameLabel.setWidth("65px");
		paramsGrid.setWidget(0, 0, nameLabel);

		Label typeLabel = new Label(Constants.studioUIMsg.type());
		typeLabel.setWidth("40px");
		paramsGrid.setWidget(0, 1, typeLabel);

		Label valueLabel = new Label(Constants.studioUIMsg.value());
		paramsGrid.setWidget(0, 2, valueLabel);
		paramsGrid.setVisible(false);
	}


	public void lock(){
		for( FocusWidget wgt : paramBoxs ){
			wgt.setEnabled( false );
		}
	}

	public void unlock(){
		for( FocusWidget wgt : paramBoxs ){
			wgt.setEnabled( true );
		}
	}

	/**
	 * Validate and set the value of parameters
	 * 
	 * @throws ValueInvalidException
	 */
	public abstract void validate() throws ValueInvalidException;
}
