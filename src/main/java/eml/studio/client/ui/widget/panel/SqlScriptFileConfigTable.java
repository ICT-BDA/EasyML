/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.panel;

import eml.studio.client.ui.widget.program.SqlProgramWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Sql script program widget configuration table 
 * 
 * - Can dynamically add or delete input or output files of the widget in the table 
 * 
 */
public class SqlScriptFileConfigTable extends FlexTable {

	protected String name = null;
	protected SqlProgramWidget widget = null;

	public SqlScriptFileConfigTable(SqlProgramWidget widget, String name){
		this.widget = widget;
		this.name = name;
		this.insertRow(0);
		Label add = new Label();
		add.addStyleName("admin-user-edit");
		this.setWidget(0, 0, new Label(name));
		this.setWidget(0, 1, new Label());
		this.setWidget(0, 2, add);
		this.setWidget(0, 3, new Label());
		add.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				int i = 0;
				while( i < SqlScriptFileConfigTable.this.getRowCount() 
						&& SqlScriptFileConfigTable.this.getWidget(i, 2 ) != event.getSource() ) i ++ ;

				if( i < SqlScriptFileConfigTable.this.getRowCount() ){
					addRow( i, "");
				}

			}

		});
	}

	public int getCount(){
		return this.getRowCount() - 1;
	}
	
	public void deleteRow(int row){
		this.removeRow(row);
		for( int i = row; i < this.getRowCount() - 1; ++ i ){
			Label label = (Label)this.getWidget(i,0);
			label.setText(name + " " + i);
		}
		active();
	}


	public void addRow(String aliases) {
		addRow( this.getRowCount() - 1, aliases );
	}

	public void addRow(int row,String default_text){
		TextBox box = new TextBox();
		box.setText(default_text);
		addRow(row, box);
	}

	protected void addRow(int row, TextBox box){
		this.insertRow( row );
		this.setWidget(row, 0, new Label(name + " " + row));
		this.setWidget(row, 1, box);

		Label add = new Label();
		add.addStyleName("admin-user-edit");
		Label del = new Label();
		del.addStyleName("admin-user-delete");
		this.setWidget(row, 2, add);
		this.setWidget(row, 3, del);

		for( int i = row + 1; i < this.getRowCount() - 1; ++ i ){
			Label label = (Label)this.getWidget(i,0);
			label.setText(name + " " + i);
		}

		add.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				int i = 0;
				while( i < SqlScriptFileConfigTable.this.getRowCount() && SqlScriptFileConfigTable.this.getWidget(i, 2 ) != event.getSource() ) i ++ ;
				if( i < SqlScriptFileConfigTable.this.getRowCount() ){
					addRow( i, "");
				}
			}
		});

		del.addClickHandler( new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				int i = 0;
				while( i < SqlScriptFileConfigTable.this.getRowCount() && SqlScriptFileConfigTable.this.getWidget(i, 3) != event.getSource() ) i ++;
				if( i < SqlScriptFileConfigTable.this.getRowCount() ){
					deleteRow( i );
				}
			}
		});

		active();
	}

	protected void active(){
		widget.active();
	}

	public SqlScriptFileConfigTable clone(){

		SqlScriptFileConfigTable st = new SqlScriptFileConfigTable(this.widget,this.name);
		for( int i = 0; i < this.getRowCount() - 1; i++ ){
			for(int j = 0; j< 4; j++){
				st.setWidget(i, j, this.getWidget(i,j));
			}
		}
		active();
		return st;
	}
}