/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.panel;

import eml.studio.client.ui.widget.program.SqlProgramWidget;

import com.google.gwt.user.client.ui.VerticalPanel;

public class FileConfigPanel extends VerticalPanel{
	private SqlScriptFileConfigTable inputConfigTable = null;
	private SqlScriptFileConfigTable outputConfigTable = null;

	protected void init(){
		this.add( inputConfigTable);
		this.add( outputConfigTable);
	}

	public FileConfigPanel(SqlProgramWidget widget){
		inputConfigTable = new InSqlScriptFileConfigTable(widget, "input");
		outputConfigTable = new OutSqlScriptFileConfigTable(widget, "output");
		init();
	}

	public SqlScriptFileConfigTable getInputConfigTable(){
		return inputConfigTable;
	}

	public SqlScriptFileConfigTable getOutputConfigTable(){
		return outputConfigTable;
	}

	public void setInputConfigTable(SqlScriptFileConfigTable inputConfigTable) {
		this.inputConfigTable = inputConfigTable;
	}

	public void setOutputConfigTable(SqlScriptFileConfigTable outputConfigTable) {
		this.outputConfigTable = outputConfigTable;
	}
}
