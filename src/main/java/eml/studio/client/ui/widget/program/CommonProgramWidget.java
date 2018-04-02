/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.program;

import eml.studio.client.ui.widget.command.CommandParseException;
import eml.studio.client.ui.widget.command.CommandParser;
import eml.studio.client.ui.widget.command.Commander;
import eml.studio.client.ui.widget.panel.CommonParameterPanel;
import eml.studio.shared.model.Program;

import com.google.gwt.user.client.Window;

/**
 * Class of common program widget
 */
public class CommonProgramWidget extends ProgramWidget {

	public CommonProgramWidget(Program program, String widget_uuid) {
		super(program, widget_uuid);
	}
	public CommonProgramWidget(){
		super();
	}

	/**
	 * Create Program Conf
	 * @return program conf
	 */
	@Override
	protected ProgramConf createProgramConf() {
		Commander cmd = null;
		try {
			cmd = CommandParser.parse(program.getCommandline());
		} catch (CommandParseException e) {
			Window.alert( e.getMessage() );
		}
	    CommonProgramConf conf = null;
	    
	    if(program.isTensorflow())
	         conf = new CommonProgramConf(cmd, true);
	    else
	         conf = new CommonProgramConf(cmd, program.isStandalone());

		return conf;
	}

	/**
	 * As the parameters can be changed, the parameter panel should
	 * be created manually.
	 */
	@Override
	public void genParamPanel(boolean editable) {
		parameterPanel = new CommonParameterPanel(this,editable);
		ptable.addParameterPanel(parameterPanel);
	}

	@Override
	public CommonProgramConf getProgramConf(){
		return (CommonProgramConf)programConf;
	}

	/**
	 * Clone this object and generate a new Common Program Widget
	 * @param newId id of new widget
	 * @return new CommonProgramWidget
	 */
	@Override
	public CommonProgramWidget clone(String newId){
		CommonProgramWidget newWidget = new CommonProgramWidget(this.getProgram(),newId);
		newWidget.genParamPanel(true);
		((CommonParameterPanel)newWidget.getParameterPanel()).
		cloneValue((CommonParameterPanel)this.getParameterPanel());

		return newWidget;
	}

}
