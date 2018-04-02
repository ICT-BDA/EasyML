/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.program;

import eml.studio.client.ui.panel.ETLPopPanel;
import eml.studio.client.ui.property.ProgramPropertyTable;
import eml.studio.client.ui.widget.BaseWidget;
import eml.studio.client.ui.widget.panel.ParameterPanel;
import eml.studio.client.ui.widget.shape.InNodeShape;
import eml.studio.client.ui.widget.shape.NodeShape;
import eml.studio.client.ui.widget.shape.OutNodeShape;
import eml.studio.client.util.UUID;
import eml.studio.shared.model.Program;
import eml.studio.shared.oozie.OozieAction;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.orange.links.client.menu.ContextMenu;

/**
 * Defines program widget in design panel, which attaches attributes like:
 *  - program parameters
 *  - file parameters
 *  - action status
 */
public abstract class ProgramWidget extends BaseWidget {
	protected ProgramConf programConf = null;
	public ProgramWidget(){
		super("","");
	}
	public class Model{

		public static final String LATEST_OOZIE_JOBID = "latest";
		private String curOozJobId = LATEST_OOZIE_JOBID;
		private boolean inActionList = false;

		public void setCurOozJobId(String oozJobId){
			curOozJobId = oozJobId; 
		}


		public String getCurOozJobId(){
			return curOozJobId;
		}

		public boolean isInActionList() {
			return inActionList;
		}

		public void setInActionList(boolean inActionList) {
			this.inActionList = inActionList;
		}

	}

	protected Program program;

	protected Model model;

	protected ProgramPropertyTable ptable;

	public ParameterPanel getParameterPanel() {
		return parameterPanel;
	}

	protected ParameterPanel parameterPanel;
	protected ETLPopPanel ETLPanel;
	public ETLPopPanel getETLPanel() {
		return ETLPanel;
	}

	public void setETLPanel() {
		this.ETLPanel = new ETLPopPanel(ProgramWidget.this);
		this.ETLPanel.setETLPanel(this.parameterPanel);
		this.ETLPanel.show();
		this.ETLPanel.center();
	}
	public void cloneETLPanel(ETLPopPanel panel){

	}

	private OozieAction action;

	public String getWorkPath(){
		//If the program corresponding to the widget does not commit execution,
		// the previously generated file path is returned
		if( !this.getModel().isInActionList() ){
			return this.getAction().getAppPath() + "/" + this.getAction().getName() + "/" ;
		}else{
			//return a file path for the new task
			return "${appPath}/" + this.getId() + "/";
		}
	}

	protected abstract ProgramConf createProgramConf();
	
	/**
	 * Generate a program widget with default parameters 
	 * 
	 * @param program
	 * @param widget_uuid
	 */
	public ProgramWidget(Program program,
			String widget_uuid) {
		super(program.getName(), widget_uuid);
		this.program = program;
		this.action = new OozieAction();
		this.action.setStatus( OozieAction.NEW_STATUS );
		this.model = new Model();
		programConf = createProgramConf();
		for (int i = 0; i < getProgramConf().getInputFileCount(); ++i) {
			inNodeShapes.add(new InNodeShape(this, i));
		}
		for (int i = 0; i < getProgramConf().getOutputFileCount(); ++i) {
			outNodeShapes.add(new OutNodeShape(this, i));
		}
		ptable = new ProgramPropertyTable(program);
		label.addStyleName("deselected");
	    if (program.isStandalone())
	    	label.addStyleName("bda-action-standalone");
	    else if(program.isETL()) 
	    	label.addStyleName("bda-action-ETL");
	    else if(program.isTensorflow())
	    	label.addStyleName("bda-action-tensorflow");
	    else 
	    	label.addStyleName("bda-action-distribute");
	}

	@Override
	public String getId() {
		String id = super.getId();
		if( id == null ) return "";
		return id.replaceAll("[^0-9a-zA-Z\\-_]", "-");
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	/**
	 * give this object a NEW_STATUS OozieAction
	 */
	public void flushOozieAction(){
		action = new OozieAction();
		action.setStatus(OozieAction.NEW_STATUS);
		setStyleName("basewidget");
	}

	/**
	 * Draw widget at canvas
	 */
	@Override
	protected void custom() {
		canvas.clear();
		//Get the number of input and output NodeShape
		int inCount = inNodeShapes.size();
		int outCount = outNodeShapes.size();

		//Gets the top and bottom positions of the controls
		double top = offsetTop + boder / 2;
		double bottom = offsetTop + offsetHeight - boder / 2;

		//The interval between each top NodeShape
		double span = offsetWidth / (1 + inCount);
		for (int i = 0; i < inCount; ++i) {
			NodeShape nodeShape = this.inNodeShapes.get(i);
			nodeShape.setLeftRelative(offsetLeft + span * (i + 1));
			nodeShape.setTopRelative(top);
			canvas.beginPath();
			canvas.arc(nodeShape.getLeftRelative(), nodeShape.getTopRelative(), 3, 0,
					Math.PI * 2.0, true);
			canvas.setStrokeStyle(Color.DARK_BLUE);
			canvas.stroke();
			canvas.setFillStyle(Color.DARK_BLUE);
			canvas.fill();
			canvas.closePath();
		}
		//The interval between each bottom NodeShpae
		span = offsetWidth / (1 + outCount);
		for (int i = 0; i < outCount; ++i) {
			NodeShape nodeShape = outNodeShapes.get(i);// new OutNodeShape(this,
			nodeShape.setLeftRelative(offsetLeft + span * (i + 1));
			nodeShape.setTopRelative(bottom);
			canvas.beginPath();
			canvas.arc(nodeShape.getLeftRelative(), nodeShape.getTopRelative(), 3, 0,
					Math.PI * 2.0, true);
			canvas.setStrokeStyle(Color.GREEN);
			canvas.stroke();
			canvas.setFillStyle(Color.GREEN);
			canvas.fill();
			canvas.closePath();
		}
	}

	/**
	 * Get the files' name in all out node
	 */
	public void randFileName() {
		for (NodeShape shape : outNodeShapes) {
			OutNodeShape out_shape = (OutNodeShape) shape;
			out_shape.setFileId(UUID.randomUUID());
			this.getProgramConf().setOutputFilePath(out_shape.getPortId(), 
					out_shape.getWorkflowPath() + "/" + out_shape.getFileId(), out_shape.getFileId());
		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		super.onMouseDown(event);
	}

	@Override
	public void onSelected() {
		selected = true;
		label.removeStyleName("deselected");
		label.addStyleName("selected");
		this.getController().setPropTable(ptable);
	}

	public ProgramPropertyTable getPropertyTable() {
		return ptable;
	}

	@Override
	public void onDeSelected() {
		selected = false;
		label.removeStyleName("selected");
		label.addStyleName("deselected");
	}

	@Override
	public void setStyleName(String stylename) {
		label.setStyleName(stylename);
		if (selected)
			label.addStyleName("selected");
		else
			label.addStyleName("deselected");

	    if (program.isStandalone()) 
	        label.addStyleName("bda-action-standalone");
	      else if(program.isETL()) 
	      	label.addStyleName("bda-action-ETL");
	      else if(program.isTensorflow())
	      	label.addStyleName("bda-action-tensorflow");
	      else 
	      	label.addStyleName("bda-action-distribute");
	}

	@Override
	public void clearMenu(){
		menu.clear();
		menu = new ContextMenu();
	}

	public OozieAction getAction() {
		return action;
	}

	public void setAction(OozieAction action) {
		this.action = action;
	}

	/**
	 * As the parameters can be changed, the parameter panel should
	 * be created manually.
	 */
	public abstract void genParamPanel(boolean editable);

	public Model getModel(){
		return model;
	}

	public ProgramConf getProgramConf() {
		return programConf;
	}

	public void setPtable(ProgramPropertyTable ptable) {
		this.ptable = ptable;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
