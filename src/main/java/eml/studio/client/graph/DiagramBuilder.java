/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.graph;

import java.util.logging.Logger;

import eml.studio.client.controller.DiagramController;
import eml.studio.client.controller.MonitorController;
import eml.studio.client.mvp.presenter.MonitorPresenter;
import eml.studio.client.rpc.DatasetService;
import eml.studio.client.rpc.DatasetServiceAsync;
import eml.studio.client.rpc.ProgramService;
import eml.studio.client.rpc.ProgramServiceAsync;
import eml.studio.client.ui.tree.DatasetLeaf;
import eml.studio.client.ui.tree.ProgramLeaf;
import eml.studio.client.ui.widget.dataset.DatasetWidget;
import eml.studio.client.ui.widget.panel.ParameterPanel;
import eml.studio.client.ui.widget.program.CommonProgramWidget;
import eml.studio.client.ui.widget.program.ProgramWidget;
import eml.studio.client.ui.widget.program.ScriptProgramWidget;
import eml.studio.client.ui.widget.program.SqlProgramWidget;
import eml.studio.client.util.UUID;
import eml.studio.shared.graph.OozieDatasetNode;
import eml.studio.shared.graph.OozieEdge;
import eml.studio.shared.graph.OozieGraph;
import eml.studio.shared.graph.OozieProgramNode;
import eml.studio.shared.model.Dataset;
import eml.studio.shared.model.Program;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * This class is used to built a visual diagram of the project
 */
public class DiagramBuilder {

	private Logger logger = Logger.getLogger( DiagramBuilder.class.getName() );

	protected static ProgramServiceAsync programSrv =
			GWT.create(ProgramService.class);

	protected static DatasetServiceAsync datasetSrv =
			GWT.create(DatasetService.class);
	private boolean isloading = false;
	private MonitorPresenter presenter = null;
	private MonitorController controller = null;
	private MonitorPresenter.View view = null;
	public DiagramBuilder(MonitorPresenter presenter){
		this.presenter = presenter;
		this.view = presenter.getView();
		this.controller = this.presenter.getView().getController();

	}

	public void bind(){
		presenter.getView().getProgramTree().addSelectionHandler(new SelectionHandler<TreeItem>() {

			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {

				TreeItem item = event.getSelectedItem();
				if (item instanceof ProgramLeaf) {
					Program program = ((ProgramLeaf) item).getModule();
					String uid = program.getName().replaceAll("[\\s,?]+", "") +
							"-" + UUID.randomID();

					ProgramWidget pw = null;
					if( !program.getProgramable() ){
						pw = new CommonProgramWidget(program, uid);
					}else if( !program.isDistributed() )
						pw = new ScriptProgramWidget(program,uid);
					else
						pw = new SqlProgramWidget(program,uid);
					//give pw a NEW_STATUS OozieAction
					pw.flushOozieAction();
					pw.genParamPanel(true);

					placeProgramWidget(pw);
				}
			}
		});

		presenter.getView().getDatasetTree().addSelectionHandler(new SelectionHandler<TreeItem>() {

			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem item = event.getSelectedItem();
				if (item instanceof DatasetLeaf) {
					Dataset dataset = ((DatasetLeaf) item).getModule();
					String uid = dataset.getName().replaceAll("[\\s,?]+", "") +
							"-" + UUID.randomID();
					final DatasetWidget dw = new DatasetWidget(dataset, uid);
					placeDatasetWidget(dw);
				}
			}
		});

		presenter.getView().getResultTree().addSelectionHandler(new SelectionHandler<TreeItem>() {

			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {

				TreeItem item = event.getSelectedItem();
				if (item instanceof ProgramLeaf) {
					Program program = ((ProgramLeaf) item).getModule();
					String uid = program.getName().replaceAll("[\\s,?]+", "") +
							"-" + UUID.randomID();

					ProgramWidget pw = null;
					if( !program.getProgramable() ){
						pw = new CommonProgramWidget(program, uid);
					}else if( !program.isDistributed() )
						pw = new ScriptProgramWidget(program,uid);
					else
						pw = new SqlProgramWidget(program,uid);

					//give pw a NEW_STATUS OozieAction
					pw.flushOozieAction();
					pw.genParamPanel(true);

					placeProgramWidget(pw);
				}

				if (item instanceof DatasetLeaf) {
					Dataset dataset = ((DatasetLeaf) item).getModule();
					String uid = dataset.getName().replaceAll("[\\s,?]+", "") +
							"-" + UUID.randomID();
					final DatasetWidget dw = new DatasetWidget(dataset, uid);
					placeDatasetWidget(dw);
				}
			}
		});
	}


	/** place a dataset widget to paint panel */
	private void placeDatasetWidget(DatasetWidget w) {
		view.getNowDiagramColler().addWidget(w);
		view.setPropTable(w.getPropertyTable());
	}

	/** place a program widget to paint panel */
	public void placeProgramWidget(ProgramWidget w) {
		view.getProgramTree().setPopupWidget(w);
		if(w instanceof CommonProgramWidget){
			if (((CommonProgramWidget) w).getProgramConf().getParameterCount() > 0) {
				ParameterPanel panel = w.getParameterPanel();
				view.getParamPopup().setParameterPanel(panel);
				view.getParamPopup().center();
			}
		}

		view.setPropTable(w.getPropertyTable());
		view.getNowDiagramColler().addWidget(w);
	}

	/**
	 * Redraw Data flow chart and draft
	 */
	public void rebuildDAG(final OozieGraph graph,  final boolean editable) {
		// If you are performing a DAG redraw task, do not repeat it
		if (isloading)
			return;

		//Before you start building the map,
		//the submit button is locked so that it can not be submitted
		//Until you confirm that the current task has been completed, or begin a new task
		//And then use unlockSubmit to unlock
		presenter.lockSubmit();
		isloading = true;
		controller.clear();

		// the expected widget number after loading the DAG
		final int target_widget_size = controller.getWidgetNum() +
				graph.getNodeNum();
		// draw dataset nodes
		for (OozieDatasetNode node : graph.getDatasetNodes())
			setupDatasetWidget(node,controller);

		// draw program nodes
		for (OozieProgramNode node : graph.getProgramNodes()){
			logger.info("(BEGIN) setup widget " + node.getId() );
			setupProgramWidget(node, editable,controller);
			logger.info("(END) setup widget " + node.getId() );
		}
		// draw edges
		Timer timer = new Timer() {

			@Override
			public void run() {
				if (controller.getWidgetNum() >= target_widget_size) {
					for (OozieEdge e : graph.getEdges())
						setupEdge(e);

					this.cancel();
					isloading = false;

					if( !editable )
						presenter.afterDAGBuild();
					else{
						presenter.unlockSubmit();
					}
				}
			}
		};

		timer.scheduleRepeating(2);
	}


	/**
	 * load a program widget according to a node from graph-describe.xml
	 */
	private void setupProgramWidget(final OozieProgramNode node,
			final boolean editable, final DiagramController dcontroller) {
		programSrv.load(node.getModuleId(), new AsyncCallback<Program>() {

			@Override
			public void onFailure(Throwable e) {
				Window.alert(e.getMessage());
			}

			@Override
			public void onSuccess(Program program) {
				// create widget according to moduleId

				ProgramWidget widget = null;

				logger.info("(TRY)Create widget: " + node.getId());

				if( program.getProgramable() ){
					if(! program.isDistributed() )
						widget = createScriptWidget(program, node, editable);
					else{
						widget = createSqlScriptWidget(program,node, editable);
					}
				}
				else widget = createCommonWidget(program, node, editable);
				logger.info("(SUCCEED)Create widget: " + node.getId());

				if( !editable ){
					if( ProgramWidget.Model.LATEST_OOZIE_JOBID.equals( node.getOozJobId() )){
						// set latest oozie job id
						widget.getModel().setCurOozJobId(
								presenter.getCurrentJob().getCurOozJobId() );
					}else{
						widget.getModel().setCurOozJobId( node.getOozJobId() );
					}
					logger.info( widget.getModel().getCurOozJobId() );
				}else{
					widget.getModel().setCurOozJobId( ProgramWidget.Model.LATEST_OOZIE_JOBID);
				}

				// set the input/output files
				logger.info("initialize output files");
				int i = 0;
				for (String file : node.getFiles()) {
					widget.getOutNodeShapes().get(i++).setFileId(file);
				}

				dcontroller.addWidget(widget, node.getX(), node.getY());
			}
		});
	}

	/**
	 * Create SQL script widget via program and oozie program node
	 * @param program
	 * @param node
	 * @param editable
	 * @return
	 */
	private SqlProgramWidget createSqlScriptWidget(Program program, OozieProgramNode node, boolean editable){

		SqlProgramWidget widget = new SqlProgramWidget(program, node.getId() );

		if( node.getScript() != null ){

			widget.getProgramConf().setScriptContent( node.getScript() );
		}

		widget.genParamPanel(editable);

		logger.info( "append input file to widget");
		for( int i = 0; i < node.getInputFileCount(); ++ i ){
			widget.addInput( node.getInputAliases().get(i));
		}
		logger.info( "append output file to widget");
		for( int i = 0; i < node.getOutputFileCount(); ++ i ){
			widget.addOutput( node.getOutputAliases().get(i));
		}
		logger.info("active the widget");
		widget.active();
		return widget;
	}

	/**
	 * Create script widget via program and oozie program node
	 * @param program
	 * @param node
	 * @param editable
	 * @return
	 */
	private ScriptProgramWidget createScriptWidget(Program program, OozieProgramNode node, boolean editable){
		ScriptProgramWidget widget = new ScriptProgramWidget(program, node.getId() );

		if( node.getScript() != null ){
			logger.info("initialize script");
			widget.active(node.getInputFileCount(), node.getOutputFileCount());
			widget.getProgramConf().setScriptContent( node.getScript() );
		}
		widget.genParamPanel(editable);
		return widget;
	}


	/**
	 * Create CommonWidget via program and oozie program node
	 * @param program
	 * @param node
	 * @param editable
	 * @return CommonProgramWidget
	 */
	private CommonProgramWidget createCommonWidget(Program program, OozieProgramNode node, boolean editable){

		CommonProgramWidget widget = new CommonProgramWidget(program, node.getId());
		// set the parameters of program
		for (String kv: node.getParams()) {
			int split_idx = kv.indexOf(":");
			int param_id = Integer.parseInt(kv.substring(0, split_idx));
			String value = kv.substring(split_idx + 1);
			// logger.info( "parameter: (" + param_id + ", " + value +")");
			widget.getProgramConf().setParameter(param_id, value);
		}
		widget.genParamPanel(editable);
		logger.info("create CommonProgramWidget: " + widget.getId());
		return widget;
	}


	/** load a dataset widget according to a node from graph-describe.xml */
	private void setupDatasetWidget(final OozieDatasetNode node,final DiagramController dcontroller) {
		datasetSrv.load(node.getModuleId(), new AsyncCallback<Dataset>() {

			@Override
			public void onFailure(Throwable e) {
				Window.alert(e.getMessage());
			}

			@Override
			public void onSuccess(Dataset dataset) {
				logger.info("setup dataset:" + dataset.getName());
				// create widget according to moduleId
				DatasetWidget widget = new DatasetWidget(dataset, node.getId());
				widget.getOutNodeShapes().get(0).setFileId(node.getFile());
				dcontroller.addWidget(widget, node.getX(), node.getY());
				logger.info("setup dataset success");
			}
		});
	}

	/**
	 * Draw an edge based on xml description
	 */
	private void setupEdge(final OozieEdge e) {
		String[] arr = e.getSrc().split(":");
		String src_id = arr[0];
		int src_port = Integer.parseInt(arr[1]);

		String[] items = e.getDst().split(":");
		String dst_id = items[0];
		int dst_port = Integer.parseInt(items[1]);
		logger.info("src=" + src_id + ":" + src_port +
				", dst=" + dst_id + ":" + dst_port);
		controller.addConnection(src_id, src_port, dst_id, dst_port);
	}


}
