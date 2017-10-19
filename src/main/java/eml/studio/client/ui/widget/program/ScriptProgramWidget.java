/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.program;

import eml.studio.client.ui.widget.command.FileHolder.FileType;
import eml.studio.client.ui.widget.command.ScriptFileDescription;
import eml.studio.client.ui.widget.panel.ScriptParameterPanel;
import eml.studio.client.ui.widget.shape.InNodeShape;
import eml.studio.client.ui.widget.shape.NodeShape;
import eml.studio.client.ui.widget.shape.OutNodeShape;
import eml.studio.shared.model.Program;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;

/**
 * Define the script program widget in design panel
 */
public class ScriptProgramWidget extends ProgramWidget{

	public ScriptProgramWidget(){
		super();
	}
	public ScriptProgramWidget(Program program, String widget_uuid) {
		super(program, widget_uuid);
	}

	private void handleNode(int index, FileType fileType, boolean remove){
		switch(fileType)
		{
		case InputFile:
			if( remove ){
				inNodeShapes.get( index ).deleteAllConnections();
				inNodeShapes.remove( index );
				getProgramConf().getInFileDescriptions().remove(index);
			}else{
				inNodeShapes.add( new InNodeShape(this, index));
				getProgramConf().getInFileDescriptions().add( new ScriptFileDescription());
			}
			break;
		case OutputFile:
			if( remove ){
				outNodeShapes.get( index ).deleteAllConnections();
				outNodeShapes.remove( index );
				getProgramConf().getOutFileDescriptions().remove(index);
			}else{
				outNodeShapes.add( new OutNodeShape(this, index));
				getProgramConf().getOutFileDescriptions().add( new ScriptFileDescription());
			}
			break;
		}
	}
	
	/**
	 * Make the setting number of input and output files n and m  effective
	 * 
	 * @param n input file number
	 * @param m  output file number
	 */
	public void active(int n,int m){
		int inSize = inNodeShapes.size();
		int outSize = outNodeShapes.size();

		if( n < 0 ) n = inSize;
		if( m < 0 ) m = outSize;

		// delete the extra input node shape
		while( n < inSize ){
			-- inSize;
			handleNode(inSize, FileType.InputFile, true);
		}

		//delete the extra output node shape
		while( m < outSize ){
			-- outSize;
			handleNode(outSize, FileType.OutputFile, true);
		}

		while( inSize < n ){
			handleNode(inSize++, FileType.InputFile, false);
		}

		while( outSize < m ){
			handleNode(outSize++, FileType.OutputFile, false);
		}

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

	@Override
	public void genParamPanel(boolean editable){
		final ScriptParameterPanel panel =
				new ScriptParameterPanel(this,editable);
		parameterPanel = panel;
		ptable.addParameterPanel( panel );
		panel.addCountBoxHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				int n,m;
				n = m = -1;
				try{
					n = Integer.parseInt( panel.getInCountBox().getText() );
				}catch(Exception ex){
				}
				try{
					m = Integer.parseInt( panel.getOutCountBox().getText() );
				}catch(Exception ex){
				}
				active(n,m);
			}

		});

	}

	@Override
	public ScriptProgramWidget clone(String newId){
		ScriptProgramWidget widget = new ScriptProgramWidget(this.getProgram(),newId);
		widget.genParamPanel(true);
		ScriptParameterPanel panel = (ScriptParameterPanel)(widget.getParameterPanel());
		panel.clonePanel((ScriptParameterPanel)this.getParameterPanel());
		return widget;
	}

	@Override
	protected ProgramConf createProgramConf() {
		return new ScriptProgramConf(this.program.getCommandline());
	}

	@Override
	public ScriptProgramConf getProgramConf(){
		return (ScriptProgramConf)programConf;
	}

}
