/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.controller;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import eml.studio.client.graph.OozieGraphBuilder;
import eml.studio.client.rpc.JobService;
import eml.studio.client.rpc.JobServiceAsync;
import eml.studio.client.ui.panel.StdPanel;
import eml.studio.client.ui.widget.BaseWidget;
import eml.studio.client.ui.widget.BaseWidgetMenuItemFactory;
import eml.studio.client.ui.widget.command.ValueInvalidException;
import eml.studio.client.ui.widget.dataset.DatasetWidget;
import eml.studio.client.ui.widget.panel.ParameterPanel;
import eml.studio.client.ui.widget.program.ProgramWidget;
import eml.studio.client.ui.widget.shape.NodeShape;
import eml.studio.client.ui.widget.shape.OutNodeShape;
import eml.studio.shared.graph.OozieGraph;
import eml.studio.shared.oozie.OozieAction;
import eml.studio.shared.util.ProgramUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Monitor controller is used to control operating logic in web app
 */
public class MonitorController extends DiagramController {
	private static Logger logger =
			Logger.getLogger(MonitorController.class.getName());

	private static JobServiceAsync jobSrv = GWT.create(JobService.class);

	public MonitorController(int canvasWidth, int canvasHeight) {
		super(canvasWidth, canvasHeight);
	}

	/**
	 * Synchronize current oozie job with server
	 * @param curOozJobId target job id
	 */
	public void updateWgtCurOozJobId(String curOozJobId){
		for( Map.Entry<String, BaseWidget> entry: widgets.entrySet()){
			BaseWidget bw = entry.getValue();
			if( bw instanceof DatasetWidget) continue;
			ProgramWidget proWgt = (ProgramWidget)bw;
			if( ProgramWidget.Model.LATEST_OOZIE_JOBID.equals(
					proWgt.getModel().getCurOozJobId() )){
				proWgt.getModel().setCurOozJobId( curOozJobId);
			}
		}
	}

	/**
	 * Change the program node need to be executed status to new
	 * @param actionNames acitons need to be executed
	 */
	public void reflushWidgetStatus(List<String> actionNames){
		for( String widgetId : actionNames){
			if( widgets.containsKey( widgetId )){
				ProgramWidget pw = (ProgramWidget) widgets.get(widgetId);
				//give pw a NEW_STATUS OozieAction
				pw.flushOozieAction();
				pw.clearMenu();

				pw.addMenuItem( BaseWidgetMenuItemFactory.createDeleteItem(pw));
			}
		}
	}

	/**
	 * Lock widgets' parameter panels
	 * @param actionNames target action widgets need to be locked
	 */
	public void lockWidgetParameterPanel(List<String> actionNames){
		for( String widgetId : actionNames){
			if( widgets.containsKey( widgetId )){
				ProgramWidget pw = (ProgramWidget) widgets.get(widgetId);
				pw.getParameterPanel().lock();
			}
		}
	}

	/**
	 * Unlock widgets' parameter panels
	 * @param actionNames target action widgets need to be unlocked
	 */
	public void unlockWidgetParameterPanel(List<String> actionNames){
		for( String widgetId : actionNames){
			if( widgets.containsKey( widgetId )){
				ProgramWidget pw = (ProgramWidget) widgets.get(widgetId);
				pw.getParameterPanel().unlock();
			}
		}
	}

	/**
	 * Update widget status
	 * 
	 * @param actions   oozie action list
	 */
	public void updateWidgetStatus(List<OozieAction> actions) {
		logger.info( "Actions number: " + actions.size() );
		for (OozieAction newAction : actions ) {
			String widgetId = newAction.getName();
			logger.info( "Action name: " + widgetId );
			if(setWidgetStatus(widgetId,widgets,newAction)) continue;
		}
	}

	/**
	 * set widget status
	 */
	public Boolean setWidgetStatus(String widgetId, Map<String,BaseWidget> widgets,OozieAction newAction){
		if (widgets.containsKey(widgetId)) {
			ProgramWidget widget = (ProgramWidget) widgets.get(widgetId);
			String wdgOozJob = widget.getModel().getCurOozJobId() ;


			if( !wdgOozJob.equals( newAction.getJobId() ) ){
				logger.info( widget.getId() + ":"+ wdgOozJob + " NOT EQUALs " + newAction.getJobId() );
				return true;
			}

			if( newAction != null ){
				logger.info( widget.getId() + ":"+ wdgOozJob + " EQUALs " + newAction.getJobId() );
				logger.info( newAction.getStatus() );
				if ( ProgramUtil.isOkState( newAction )) {
					if(ProgramUtil.isOkState( widget.getAction() ) ) return true;
					widget.setStyleName("actionOK");
					widget.setAction(newAction);
					widget.addMenuItem(BaseWidgetMenuItemFactory
							.createStdoutItem(widget));
					widget.addMenuItem(BaseWidgetMenuItemFactory
							.createStderrItem(widget));
				} else if (ProgramUtil.isErrorState(newAction)) {
					if(ProgramUtil.isErrorState( widget.getAction() ) ) return true;
					widget.setAction(newAction);
					widget.setStyleName("actionERROR");
					//add menu item
					widget.addMenuItem(BaseWidgetMenuItemFactory
							.createStdoutItem(widget));
					widget.addMenuItem(BaseWidgetMenuItemFactory
							.createStderrItem(widget));
				} else if( !ProgramUtil.isNewState(newAction)){
					widget.setAction(newAction);
					widget.setStyleName("actionRunning");
				}
			}
		}
		return false;
	}

	/**
	 * resume oozie job
	 *
	 * @param JobId target job id
	 */
	public void resumeJob(String JobId) {
		//JobMasterAsyc.resumeJob(JobId, new AsyncCallback<Void>() {
		jobSrv.resumeJob(JobId, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.warning("job resume failed!");
			}

			@Override
			public void onSuccess(Void result) {
				logger.warning("resume succeeded!");
			}
		});
	}

	/**
	 * suspend oozie job
	 *
	 * @param JobId target job id
	 */
	public void suspendJob(String JobId) {
		jobSrv.suspendJob(JobId, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.warning("job suspend failed!");
			}

			@Override
			public void onSuccess(Void result) {
				logger.warning("Suspend succeeded!");
			}
		});
	}

	/** kill a oozie job */
	public void killJob(String JobId) {
		jobSrv.killJob(JobId, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.warning("job kill failed!");
			}

			@Override
			public void onSuccess(Void result) {
				logger.info("Kill succeeded!");
			}
		});
	}

	/** start a oozie job */
	public void startJob(String JobId) {

		jobSrv.startJob(JobId, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.warning("job start failed!");
			}

			@Override
			public void onSuccess(Void result) {
				logger.info("start succeeded!");
			}
		});
	}

	/** ReRun oozie job */
	public void rerunJob(final String JobId) {

		jobSrv.reRun(JobId, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.warning("job rerun failed!");
			}

			@Override
			public void onSuccess(Void result) {
				logger.info("Rerun succeeded!");
				// updateJob(JobId);
			}
		});
	}


	/** Get the stdout of a program widget */
	public void getStdOut(String jobId, final String actionId) {

		jobSrv.getStdOut(jobId, actionId, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.warning("Get StdOutErr Failed!");
			}

			@Override
			public void onSuccess(String result) {
				String title = "Standard output - " + actionId.split("@")[1];
				showMsg(result, title);
			}
		});
	}
	/** Get the stderr of a program widget */
	public void getStdErr(String jobId, final String actionId) {
		jobSrv.getStdErr(jobId, actionId, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.warning("Get StdOutErr Failed!");
			}

			@Override
			public void onSuccess(String result) {
				String title = "Standard error - " + actionId.split("@")[1];
				showMsg(result, title);
			}
		});
	}

	private void showMsg(String msg, String title) {
		new StdPanel(msg, title);
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		super.onMouseUp(event);
		if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
			NodeShape shape = (NodeShape) getShapeUnderMouse();
			if (shape instanceof OutNodeShape) {
				OutNodeShape outShape = (OutNodeShape)shape;
				int x = outShape.getOffsetLeft() + 2*outShape.getRadius();
				int y = outShape.getOffsetTop() + 2*outShape.getRadius();
				outShape.getContextMenu().setPopupPosition(x,y);
				outShape.getContextMenu().show();
			}
		}
	}


	/**
	 * Validate the parameters in the program widgets
	 */
	public void parameterValidate() throws ValueInvalidException {

		for (Map.Entry<String, BaseWidget> entry : widgets.entrySet()) {
			if (entry.getValue() instanceof ProgramWidget) {
				ParameterPanel panel = ((ProgramWidget) entry.getValue())
						.getParameterPanel();
				if (panel != null) {
					panel.validate();
				}
			}
		}
	}


	/**
	 * Create an XML that describes the Dataflow diagram and save it
	 */
	public OozieGraph generateGraphXML() {
		OozieGraphBuilder graphBuilder = new OozieGraphBuilder(this);
		return graphBuilder.asGraph();
	}


	/** Place a widget to paint panel
	 *
	 *@param w widget need to be add
	 *@param left distance to the left boundary
	 *@param top distance to the top boundary
	 */
	@Override
	public void addWidget(final BaseWidget w, int left, int top) {
		super.addWidget(w, left, top);
		w.clearMenu();
		w.addMenuItem(BaseWidgetMenuItemFactory.createDeleteItem(w));
		if(w instanceof ProgramWidget){
			if(((ProgramWidget) w).getProgram().isETL())
				w.addMenuItem(BaseWidgetMenuItemFactory.createPremeterItem(w));
		}

	}



}
