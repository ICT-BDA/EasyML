/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.panel;

import eml.studio.client.ui.widget.command.ScriptFileDescription;
import eml.studio.client.ui.widget.program.SqlProgramWidget;
import eml.studio.client.ui.widget.shape.SqlInNodeShape;

/**
 * Sql script program widget  input file node configuration table, which can dynamically add or delete input nodes
 * 
 */
public class InSqlScriptFileConfigTable extends SqlScriptFileConfigTable{

	public InSqlScriptFileConfigTable(SqlProgramWidget widget,String name) {
		super(widget, name);
	}

	@Override
	public void addRow(int row, String default_text){
		SqlInNodeShape shape = new SqlInNodeShape( widget, row);
		widget.getInNodeShapes().add(row, shape );
		widget.getProgramConf().getInFileDescriptions().add( new ScriptFileDescription());
		shape.getBox().setText(default_text);
		super.addRow( row, shape.getBox());
	}

	@Override
	public void deleteRow(int row){
		widget.getInNodeShapes().get( row ).deleteAllConnections();
		widget.getInNodeShapes().remove(row);
		widget.getProgramConf().getInFileDescriptions().remove(row);
		super.deleteRow(row);
	}


}
