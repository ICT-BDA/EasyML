/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.panel;

import java.util.List;

import eml.studio.client.ui.widget.command.Parameter;
import eml.studio.client.ui.widget.command.ValueCheck;
import eml.studio.client.ui.widget.command.ValueInvalidException;
import eml.studio.client.ui.widget.program.CommonProgramWidget;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 *  Parameter panel for a program 
 */
public class CommonParameterPanel extends ParameterPanel {

	CommonProgramWidget widget;

	public CommonProgramWidget getWidget() {
		return widget;
	}

	public void setWidget(CommonProgramWidget widget) {
		this.widget = widget;
	}

	/**
	 * @param widget   the corresponding parameters
	 * @param editable whether is editable
	 * @param editable whether widget is program-able
	 */
	public CommonParameterPanel(CommonProgramWidget widget,
			boolean editable) {
		this.widget = widget;
		init(editable);
	}
	protected void init(boolean editable){
		int size = widget.getProgramConf().getParameterCount();
		initGridHead(size + 1);
		initGrid(0, editable);
	}

	protected void initGrid(int offset, boolean editable){
		List<Parameter> params = widget.getProgramConf().getParameters();

		if( params.size() > 0 ) paramsGrid.setVisible(true);
		//show the parameter setting boxes
		for (int i = 0; i < params.size() ; i++) {

			final Parameter sParameter = params.get(i);
			paramsGrid.setWidget( offset + i + 1, 0, new Label(sParameter.getParaName()));
			paramsGrid.setWidget( offset + i + 1, 1, new Label(sParameter.getParamType()));

			if (sParameter.getParamType().equals("Boolean")) {
				// add a ListBox
				final ListBox box = new ListBox();
				box.addItem("false");
				box.addItem("true");
				if (sParameter.getParamValue().equals("true"))
					box.setSelectedIndex(1);
				else
					box.setSelectedIndex(0);
				box.setTabIndex(i);
				box.setEnabled(editable);
				paramsGrid.setWidget( offset + i + 1, 2, box);
				paramBoxs.add(box);
			} else {
				// add a TextBox for parameter input
				final TextBox box = new TextBox();
				box.setText(sParameter.getParamValue());
				box.setSize("95%", "100%");
				box.setStyleName("okTextbox");
				box.setEnabled(editable);
				box.setTabIndex(i);
				paramsGrid.setWidget(offset + i + 1, 2, box);
				paramBoxs.add(box);
			}
		}

		paramsGrid.setCellPadding(5);

		this.add(paramsGrid);
	}

	/**
	 * Validate and set the value of parameters
	 * 
	 * @throws ValueInvalidException
	 */
	@Override
	public void validate() throws ValueInvalidException {
		List<Parameter> params = widget.getProgramConf().getParameters();
		for (int i = 0; i < params.size(); ++i) {
			Parameter p = params.get(i);

			if (p.getParamType().equals("Boolean")) {
				ListBox box = (ListBox) paramBoxs.get(i);
				String value = "false";
				if (box.getSelectedIndex() == 1)
					value = "true";
				ValueCheck.validate(p, value);
				p.setParamValue(value);
			} else {
				// validate the input parameters
				TextBox box = (TextBox) paramBoxs.get(i);
				String value = box.getText();
				ValueCheck.validate(p, value);
				p.setParamValue(value);
			}
		}
	}

	/**
	 * Clone value of this class's element from panel
	 * @param panel source panel
	 */
	public void cloneValue(CommonParameterPanel panel){
		List<Parameter> params = widget.getProgramConf().getParameters();
		for (int i = 0; i < params.size(); ++i) {
			Parameter p = params.get(i);
			if (p.getParamType().equals("Boolean")) {
				ListBox box = (ListBox) paramBoxs.get(i);
				ListBox newbox =(ListBox) panel.getParamBoxs().get(i);
				box.setSelectedIndex(newbox.getSelectedIndex());
			} else {
				TextBox box = (TextBox) paramBoxs.get(i);
				TextBox newbox =(TextBox) panel.getParamBoxs().get(i);
				box.setValue(newbox.getValue());
			}
		}
	}
}
