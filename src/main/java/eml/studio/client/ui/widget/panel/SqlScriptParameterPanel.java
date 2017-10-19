/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.panel;

import eml.studio.client.ui.widget.command.ScriptFileDescription;
import eml.studio.client.ui.widget.command.ValueInvalidException;
import eml.studio.client.ui.widget.program.SqlProgramWidget;
import eml.studio.client.ui.widget.shape.InNodeShape;
import eml.studio.client.ui.widget.shape.OutNodeShape;
import eml.studio.client.ui.widget.shape.SqlNodeShape;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;

/** 
 * Parameter panel for a sql script program 
 */
public class SqlScriptParameterPanel extends ParameterPanel {
	private TextArea scriptArea = null;
	private SqlProgramWidget widget;
	private FileConfigPanel panel = null;

	/**
	 * Constructor
	 * 
	 * @param params the corresponding parameters
	 * @param editable whether is editable
	 * @param programable whether widget is program-able
	 */
	public SqlScriptParameterPanel(SqlProgramWidget widget,
			boolean editable) {
		this.widget = widget;
		this.panel = new FileConfigPanel(widget);
		init(editable);
	}

	/**
	 * Init UI
	 * @param editable Wheather is editable 
	 */
	protected void init(boolean editable){
		scriptArea = new TextArea();
		scriptArea.setText( widget.getProgramConf().getScriptContent());
		this.add( panel );
		this.add( new Label("Script"));
		this.add( scriptArea );
	}

	public TextArea getScriptArea() {
		return scriptArea;
	}

	@Override
	public void validate() throws ValueInvalidException{

		int i = 0;
		for(InNodeShape shape: widget.getInNodeShapes()){
			SqlNodeShape sqlShape = (SqlNodeShape)shape;
			ScriptFileDescription sfd = (ScriptFileDescription)widget.getProgramConf().getInputFile(i++);
			sfd.setOtherName( sqlShape.getAliases() );
		}

		i = 0;
		for(OutNodeShape shape: widget.getOutNodeShapes()){
			SqlNodeShape sqlShape = (SqlNodeShape)shape;
			ScriptFileDescription sfd = (ScriptFileDescription)widget.getProgramConf().getOutputFile(i++);
			sfd.setOtherName( sqlShape.getAliases() );
		}

		widget.getProgramConf().setScriptContent( scriptArea.getText() );
	}

	public FileConfigPanel getFileConfigPanel(){
		return panel;
	}

}
