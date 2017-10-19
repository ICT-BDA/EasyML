/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.panel;

import eml.studio.client.ui.widget.command.ValueInvalidException;
import eml.studio.client.ui.widget.program.ScriptProgramWidget;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/** 
 * Parameter panel for a script program 
 * */
public class ScriptParameterPanel extends ParameterPanel {

	private TextBox inCountBox = null;
	private TextBox outCountBox = null;
	private TextArea scriptArea = null;
	private ScriptProgramWidget widget;
	
	/**
	 * Constructor
	 * 
	 * @param params   the corresponding parameters
	 * @param editable whether is editable
	 * @param programable whether widget is program-able
	 */
	public ScriptParameterPanel(ScriptProgramWidget widget,
			boolean editable) {
		this.widget = widget;
		init(editable);
	}

	/**
	 * Init UI
	 * @param editable Wheather is editable
	 */
	protected void init(boolean editable){

		initGridHead( 3 );
		inCountBox = new TextBox();
		outCountBox = new TextBox();
		inCountBox.setText( "" +widget.getInNodeShapes().size() );
		outCountBox.setText( "" + widget.getOutNodeShapes().size());

		paramsGrid.setVisible(true);
		paramsGrid.setWidget( 1 , 0, new Label("Input File Number"));
		paramsGrid.setWidget( 1, 1, new Label("Int"));
		paramsGrid.setWidget( 1, 2, inCountBox );
		inCountBox.setSize("95%", "100%");
		inCountBox.setStyleName("okTextbox");
		inCountBox.setEnabled(editable);
		inCountBox.setTabIndex(0);

		paramsGrid.setWidget( 2 , 0, new Label("Output File Number"));
		paramsGrid.setWidget( 2,  1, new Label("Int"));
		paramsGrid.setWidget( 2 , 2, outCountBox );
		outCountBox.setSize("95%", "100%");
		outCountBox.setStyleName("okTextbox");
		outCountBox.setEnabled(editable);
		outCountBox.setTabIndex(1);
		scriptArea = new TextArea();
		scriptArea.setText( widget.getProgramConf().getScriptContent());
		this.add( paramsGrid );
		this.add( new Label("Script"));
		this.add( scriptArea );
	}

	public void addCountBoxHandler(ChangeHandler handler){
		inCountBox.addChangeHandler(handler);
		outCountBox.addChangeHandler(handler);
	}

	public TextBox getInCountBox(){
		return inCountBox;
	}

	public TextBox getOutCountBox(){
		return outCountBox;
	}

	public TextArea getScriptArea() {
		return scriptArea;
	}

	@Override
	public void validate() throws ValueInvalidException{
		widget.getProgramConf().setScriptContent( scriptArea.getText() );
	}

	public void clonePanel(ScriptParameterPanel panel){
		this.getInCountBox().setValue(panel.getInCountBox().getValue());
		this.getOutCountBox().setValue(getOutCountBox().getValue());
	}
}
