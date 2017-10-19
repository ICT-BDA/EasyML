/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.panel;

import eml.studio.client.ui.widget.command.ScriptFileDescription;
import eml.studio.client.ui.widget.program.SqlProgramWidget;
import eml.studio.client.ui.widget.shape.SqlOutNodeShape;

/**
 * Sql script program widget  output file node configuration table, which can dynamically add or delete output nodes
 * 
 */
public class OutSqlScriptFileConfigTable extends SqlScriptFileConfigTable{

	public OutSqlScriptFileConfigTable(SqlProgramWidget widget,String name) {
		super(widget, name);
	}

	@Override
	public void addRow(int row, String default_text){
		SqlOutNodeShape shape = new SqlOutNodeShape( widget, row);
		widget.getOutNodeShapes().add(row, shape );
		widget.getProgramConf().getOutFileDescriptions().add( new ScriptFileDescription());
		shape.getBox().setText( default_text );
		super.addRow( row, shape.getBox());
	}

	@Override
	public void deleteRow(int row){
		widget.getOutNodeShapes().get( row ).deleteAllConnections();
		widget.getOutNodeShapes().remove(row);
		widget.getProgramConf().getOutFileDescriptions().remove(row);
		super.deleteRow(row);
	}
}
