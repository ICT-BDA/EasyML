/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.program;

import eml.studio.client.ui.widget.command.CommandParseException;
import eml.studio.client.ui.widget.command.CommandParser;
import eml.studio.client.ui.widget.command.Commander;
import eml.studio.client.ui.widget.panel.SqlScriptParameterPanel;
import eml.studio.client.ui.widget.shape.NodeShape;
import eml.studio.shared.model.Program;

import com.google.gwt.user.client.Window;

/**
 * Define the script program widget in design panel
 */
public class SqlProgramWidget extends ProgramWidget {

	public SqlProgramWidget(){
		super();
	}
	public SqlProgramWidget(Program program, String widget_uuid) {
		super(program, widget_uuid);
	}

	@Override
	protected ProgramConf createProgramConf() {
		Commander cmd = null;
		try {
			cmd = CommandParser.parse(program.getCommandline());
		} catch (CommandParseException e) {
			Window.alert( e.getMessage() );
		}
		SqlScriptProgramConf conf = new SqlScriptProgramConf(cmd);

		return conf;
	}

	public void active(){

		//draw
		custom();
		for( NodeShape shape: inNodeShapes){
			shape.setSynchronized(false);
			shape.getConnections().setSynchronized(false);
			shape.getConnections().draw();
			shape.draw();
		}
		for( NodeShape shape: outNodeShapes){
			shape.setSynchronized(false);
			shape.getConnections().setSynchronized(false);
			shape.getConnections().draw();
			shape.draw();
		}
	}

	public void addInput(String aliases){
		SqlScriptParameterPanel sqlParamPanel = (SqlScriptParameterPanel)parameterPanel;
		sqlParamPanel.getFileConfigPanel().getInputConfigTable().addRow(aliases);
	}

	public void addOutput(String aliases){
		SqlScriptParameterPanel sqlParamPanel = (SqlScriptParameterPanel)parameterPanel;
		sqlParamPanel.getFileConfigPanel().getOutputConfigTable().addRow(aliases);
	}
	
	//clone
	@Override
	public SqlProgramWidget clone(String newId){

		SqlProgramWidget sqlProgramWidget = new SqlProgramWidget(this.getProgram(),newId);
		
		//Clone the parameter data
		sqlProgramWidget.cloneNode(this);
		sqlProgramWidget.genParamPanel(true);

		SqlScriptParameterPanel newpanel = (SqlScriptParameterPanel)sqlProgramWidget.getParameterPanel();
		SqlScriptParameterPanel oldpanel = (SqlScriptParameterPanel)this.getParameterPanel();

		newpanel.getFileConfigPanel().setInputConfigTable(
				oldpanel.getFileConfigPanel().getInputConfigTable().clone());

		newpanel.getFileConfigPanel().setOutputConfigTable(
				oldpanel.getFileConfigPanel().getOutputConfigTable().clone());
		return sqlProgramWidget;
	}
	
	/**
	 * As the parameters can be changed, the parameter panel should
	 * be created manually.
	 */
	@Override
	public void genParamPanel(boolean editable) {
		parameterPanel = new SqlScriptParameterPanel(this,editable);
		ptable.addParameterPanel(parameterPanel);
	}

	@Override
	public SqlScriptProgramConf getProgramConf(){
		return (SqlScriptProgramConf)programConf;
	}
}
