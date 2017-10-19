/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import eml.studio.client.controller.DiagramController;
import eml.studio.client.controller.MonitorController;
import eml.studio.client.event.ToMonitorEvent;
import eml.studio.client.graph.DiagramBuilder;
import eml.studio.client.mvp.AppController;
import eml.studio.client.mvp.view.HeaderView;
import eml.studio.client.rpc.JobService;
import eml.studio.client.rpc.JobServiceAsync;
import eml.studio.client.ui.panel.HistoryPopupPanel;
import eml.studio.client.ui.panel.JobDescPopupPanel;
import eml.studio.client.ui.panel.ParameterPopupPanel;
import eml.studio.client.ui.panel.Grid.MonitorJobDescGrid;
import eml.studio.client.ui.property.PropertyTable;
import eml.studio.client.ui.tree.DatasetTree;
import eml.studio.client.ui.tree.JobLeaf;
import eml.studio.client.ui.tree.JobTree;
import eml.studio.client.ui.tree.JobTreeLoader;
import eml.studio.client.ui.tree.ProgramTree;
import eml.studio.client.ui.tree.SearchTree;
import eml.studio.client.ui.widget.command.ValueInvalidException;
import eml.studio.client.ui.widget.program.ProgramWidget;
import eml.studio.client.util.TimeUtils;
import eml.studio.shared.graph.OozieGraph;
import eml.studio.shared.graph.OozieProgramNode;
import eml.studio.shared.model.BdaJob;
import eml.studio.shared.oozie.OozieAction;
import eml.studio.shared.oozie.OozieJob;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/**
 *  Job monitoring presenter
 */
public class MonitorPresenter implements Presenter {
	protected static JobServiceAsync jobSrv = GWT.create(JobService.class);
	private static Logger logger = Logger.getLogger(MonitorPresenter.class
			.getName());
	private final HandlerManager eventBus;
	private final View view;
	private final HeaderLoader headerLoader;
	private final BdaJobMonitor bdaJobMonitor;
	private final DiagramBuilder diagramBuilder;
	private final String arr[];

	private BdaJob currentJob = null;
	private OozieJob currentOozieJob = null;
	private boolean instanceFlag = false; //是否是运行实例（oozieJob)

	public MonitorPresenter(HandlerManager eventBus, View view, String bdaJobId) {
		this.eventBus = eventBus;
		this.view = view;
		this.headerLoader = new HeaderLoader(eventBus, view.getHeaderView(),
				MonitorPresenter.this);
		this.bdaJobMonitor = new BdaJobMonitor(this);
		this.diagramBuilder = new DiagramBuilder(this);
		this.currentJob = new BdaJob();
		this.currentJob.setJobId(bdaJobId);
		this.currentJob.setAccount(AppController.email);
		this.arr = AppController.power.split("");
	}
	
	public MonitorPresenter(HandlerManager eventBus, View view, String bdaJobId, String oozieJobId) {
		this.eventBus = eventBus;
		this.view = view;
		this.headerLoader = new HeaderLoader(eventBus, view.getHeaderView(),
				MonitorPresenter.this);
		this.bdaJobMonitor = new BdaJobMonitor(this);
		this.diagramBuilder = new DiagramBuilder(this);
		this.currentJob = new BdaJob();
		this.currentJob.setJobId(bdaJobId);
		this.currentJob.setAccount(AppController.email);

		this.currentOozieJob = new OozieJob();
		this.currentOozieJob.setId(oozieJobId);
		this.currentOozieJob.setAccount(AppController.email);
		instanceFlag = true;
		this.arr = AppController.power.split("");
	}

	protected void init() {
		headerLoader.init();
		if (AppController.email.equals("guest")) {
			view.getSubmitButton().setVisible(false);
		} else {
			if (arr[2].equals("1")) {
				view.getSubmitButton().setVisible(true);
				view.getCloneButton().setVisible(true);
			} else {
				view.getSubmitButton().setVisible(false);
				view.getCloneButton().setVisible(false);
				view.getSubmitButton().setEnabled(false);
				view.getCloneButton().setEnabled(false);
				view.getTabPanel().remove(view.getProgramTree());
				view.getTabPanel().remove(view.getDatasetTree());
				view.getTabPanel().selectTab(0);
			}
		}
		if(instanceFlag) //If is oozie instance, then submit、clone .etc button is disable
		{
			view.getSubmitButton().setEnabled(false);
			view.getCloneButton().setEnabled(false);
			view.getClearButton().setEnabled(false);
			view.getStopButton().setEnabled(false);
			view.getRefreshButton().setEnabled(false);
		}
		JobTreeLoader.load(AppController.email);
		initJob();
		loadInitJob();
	}

	/**
	 * Init a job
	 */
	protected void initJob() {
		this.getView().getController().clear();
		this.getView().clearPropTable();
		this.unlockSubmit();
		if(instanceFlag)
			this.updateOozieJobIFView();
		else
			this.updateJobIFView();
	}
	protected void bind() {
		headerLoader.bind();
		diagramBuilder.bind();

		// Clean the paint
		view.getClearButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearCurrentJob();
				updateJobIFView();
				view.clearPropTable();
				view.getController().clear();
				unlockSubmit();
			}
		});

		// submit a job
		view.getSubmitButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if ("".equals(view.getJobDescGrid().getJobName())
						|| "".equals(view.getJobDescGrid().getDescription())) {
					view.getNewJobPopup().setJobName(
							view.getJobDescGrid().getJobName());
					view.getNewJobPopup().setJobDesc(
							view.getJobDescGrid().getDescription());
					view.getNewJobPopup().center();
				} else {
					try {
						submit();
					} catch (ValueInvalidException e) {
						Window.alert(e.getMessage());
						e.printStackTrace();
					}
				}
			}

		});

		getView().getRefreshButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				bdaJobMonitor.updateCurrentJob();
			}
		});

		getView().getCloneButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cloneCurJob();
			}
		});

		getView().getStopButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getView().getController().killJob(currentJob.getCurOozJobId());
				bdaJobMonitor.updateCurrentJob();
			}
		});

		getView().getJobTree().addSelectionHandler(
				new SelectionHandler<TreeItem>() {

					@Override
					public void onSelection(SelectionEvent<TreeItem> event) {
						TreeItem item = event.getSelectedItem();
						if (item instanceof JobLeaf) {
							String jobId = ((JobLeaf) item).getModule()
									.getJobId();
							eventBus.fireEvent(new ToMonitorEvent(jobId));
						}
					}

				});
		getView().getResultTree().addSelectionHandler(
				new SelectionHandler<TreeItem>() {

					@Override
					public void onSelection(SelectionEvent<TreeItem> event) {
						TreeItem item = event.getSelectedItem();
						// Job
						if (item instanceof JobLeaf) {
							String jobId = ((JobLeaf) item).getModule()
									.getJobId();
							eventBus.fireEvent(new ToMonitorEvent(jobId));
						}
					}

				});

		// new a job
		view.getNewJobPopup().getSubmitBtn()
		.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (view.getNewJobPopup().getJobName().equals("")) {
					view.getNewJobPopup().setErrorMsg("Task name can not be empty");
				} else {
					view.getJobDescGrid().setJobName(
							view.getNewJobPopup().getJobName());
					view.getJobDescGrid().setDescription(
							view.getNewJobPopup().getJobDesc());
					view.getNewJobPopup().hide();
					try {
						submit();
					} catch (ValueInvalidException e) {
						Window.alert(e.getMessage());
						e.printStackTrace();
					}
				}
			}

		});

		view.getParamPopup().addSubmitHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ParameterPopupPanel paramPopup = view.getParamPopup();
				paramPopup.hide();
				ProgramWidget pw = view.getPopupWidget();
				// add the parameter setting panel to the right
				pw.getPropertyTable().addParameterPanel(paramPopup.getPanel());
			}
		});
		
		getView().getHistoryButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(currentJob.getJobId()!=null)
				{
					getView().getHistoryPopup(currentJob.getJobId(),currentJob.getIsExample(),eventBus).center();
				}
				else
					Window.alert("There is no job instance in the workspace, you can not view the history！");
			}

		});

	}

	public void afterDAGBuild() {
		this.bdaJobMonitor.afterDAGBuild();
	}

	/**
	 * Lock submit button
	 */
	public void lockSubmit() {
		this.getView().getSubmitButton().setEnabled(false);
		this.getView().getController().lockDrawConnection();
	}

	/**
	 * Unlock submit button
	 */
	public void unlockSubmit() {
		this.getView().getSubmitButton().setEnabled(true);
		this.getView().getController().unlockDrawConnection();
	}

	/**
	 * Submit a job to Server
	 * @throws ValueInvalidException
	 */
	private void submit() throws ValueInvalidException {
		// The workflow must be generated before graphDescribe
		// because the middle is generated during this process
		// The uuid of the file is generated before the graphDescribe will be generated without the middle file name.

		MonitorController controller = view.getController();

		controller.parameterValidate();

		final OozieGraph graph = controller.generateGraphXML();

		String account = view.getJobDescGrid().getEmailAccount();
		if (account.equals(""))
			account = "";
		String jobDesc = view.getJobDescGrid().getDescription();

		String bdaJobId = null;
		if (currentJob != null) {
			bdaJobId = currentJob.getJobId();
		}

		if(graph.getActiveList().size()>0)
		{

			lockSubmit();
			view.getController().lockWidgetParameterPanel(graph.getActiveList());

			jobSrv.submit(view.getJobDescGrid().getJobName(), bdaJobId, graph,
					account, jobDesc, new AsyncCallback<BdaJob>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("submit failed!");
					unlockSubmit();
					view.getController().unlockWidgetParameterPanel(
							graph.getActiveList());
				}

				@Override
				public void onSuccess(BdaJob result) {
					if (result == null) {
						Window.alert("submit failed");
						unlockSubmit();
						view.getController().unlockWidgetParameterPanel(
								graph.getActiveList());
					}
					currentJob = result;
					view.getController().reflushWidgetStatus(
							graph.getActiveList());
					bdaJobMonitor.afterSubmit(result);
				}

			});
		}else{
			Window.alert("The job is finished, there is no action to execute!");
		}
	}

	/**
	 * Clone current job to a new job
	 */
	private void cloneCurJob() {

		String cloneJobId = currentJob.getJobId();

		if (cloneJobId != null && !cloneJobId.equals("undefined")) {
			jobSrv.getJob(cloneJobId, new AsyncCallback<BdaJob>() {
				@Override
				public void onFailure(Throwable caught) {
					logger.warning("loadInitJob failed!");
				}

				@Override
				public void onSuccess(BdaJob result) {

					String appName = currentJob.getCurOozJob().getAppName();
					String description = currentJob.getCurOozJob()
							.getDescription();
					clearCurrentJob();
					currentJob.getCurOozJob().setAppName(appName);
					currentJob.getCurOozJob().setDescription(description);
					updateJobIFView();
					view.clearPropTable();
					diagramBuilder.rebuildDAG(result.getOozieGraph(), true);
				}
			});
		}
	}

	/**
	 * Clear current Job
	 */
	public void clearCurrentJob() {
		BdaJob cloneJob = new BdaJob();
		OozieJob oozJob = new OozieJob();
		cloneJob.setAccount(AppController.email);
		cloneJob.setCurOozJob(oozJob);
		currentJob = cloneJob;
	}

	/**
	 * Init job
	 */
	private void loadInitJob() {
		String jobid = currentJob.getJobId();
		if(instanceFlag)
		{
			final String curOozieJobId = currentOozieJob.getId();
			if (curOozieJobId != null && !curOozieJobId.equals("undefined")) {
				jobSrv.getOozieJob(curOozieJobId, new AsyncCallback<OozieJob>() {
					@Override
					public void onFailure(Throwable caught) {
						logger.warning(caught.getMessage());
					}

					@Override
					public void onSuccess(OozieJob result) {
						currentOozieJob = result;
						currentJob.setCurOozJob(currentOozieJob);
						diagramBuilder.rebuildDAG(result.getGraph(), false);
					}
				});
			}
			if (jobid != null && !jobid.equals("undefined")) {
				jobSrv.getJob(jobid, new AsyncCallback<BdaJob>() {
					@Override
					public void onFailure(Throwable caught) {
						logger.warning(caught.getMessage());
					}

					@Override
					public void onSuccess(BdaJob result) {
						currentJob = result;
					}
				});
			}
		}else{
			if (jobid != null && !jobid.equals("undefined")) {
				jobSrv.getJob(jobid, new AsyncCallback<BdaJob>() {
					@Override
					public void onFailure(Throwable caught) {
						logger.warning(caught.getMessage());
					}

					@Override
					public void onSuccess(BdaJob result) {
						currentJob = result;
						diagramBuilder.rebuildDAG(result.getOozieGraph(), false);
					}
				});
			}
		}

	}

	/**
	 * Update Job info View
	 */
	public void updateJobIFView() {
		if (currentJob == null)
			return;
		getView().getJobDescGrid().setEmailAccount(currentJob.getAccount());
		getView().getJobDescGrid().setBdaJobID(currentJob.getJobId());

		if (currentJob.getCurOozJob() != null) {
			getView().getJobDescGrid().setJobName(
					currentJob.getCurOozJob().getAppName());
			getView().getJobDescGrid().setJobStatus(
					currentJob.getCurOozJob().getStatus());
			getView().getJobDescGrid()
			.setStartTime(
					TimeUtils.format(currentJob.getCurOozJob()
							.getCreatedTime()));
			getView().getJobDescGrid().setEndTime(
					TimeUtils.format(currentJob.getCurOozJob().getEndTime()));
			getView().getJobDescGrid().setDescription(
					currentJob.getCurOozJob().getDescription());
			String exeTime = TimeUtils.timeDiff(currentJob.getCurOozJob()
					.getCreatedTime(), currentJob.getCurOozJob().getEndTime());
			if (exeTime == null) {
				getView().getJobDescGrid().setUseTime("");
			} else {
				getView().getJobDescGrid().setUseTime(exeTime);
			}

		}
	}
	
	/**
	 *   If is oozie job instance, the view should be updated to oozie job
	 */
	public void updateOozieJobIFView() {

		if (currentJob == null)
			return;
		getView().getJobDescGrid().setEmailAccount(currentJob.getAccount());
		getView().getJobDescGrid().setBdaJobID(currentJob.getJobId());

		if (currentOozieJob != null) {
			getView().getJobDescGrid().setJobName(
					currentOozieJob.getAppName());
			getView().getJobDescGrid().setJobStatus(
					currentOozieJob.getStatus());
			getView().getJobDescGrid()
			.setStartTime(
					TimeUtils.format(currentOozieJob
							.getCreatedTime()));
			getView().getJobDescGrid().setEndTime(
					TimeUtils.format(currentOozieJob.getEndTime()));
			getView().getJobDescGrid().setDescription(
					currentOozieJob.getDescription());
			String exeTime = TimeUtils.timeDiff(currentOozieJob
					.getCreatedTime(), currentOozieJob.getEndTime());
			if (exeTime == null) {
				getView().getJobDescGrid().setUseTime("");
			} else {
				getView().getJobDescGrid().setUseTime(exeTime);
			}
		}
	}
	
	/**
	 * Get oozie actions which are not belong to current graph（When the workflow is submitted， the finished action can not be written to the workflow）
	 * 
	 * @param graph
	 * @return
	 */
	public List<OozieAction> getNonLatestAction(OozieGraph graph)
	{
		final List<OozieAction> nonLatestActions = new ArrayList<OozieAction>();
		List<OozieProgramNode> pNodes = graph.getProgramNodes();
		for(OozieProgramNode pNode : pNodes)
		{
			String actionName = pNode.getId();
			String oozieJobId = pNode.getOozJobId();
			if(oozieJobId.toLowerCase().contains("latest"))
				continue;
			else
			{
				jobSrv.getOozieAction(oozieJobId, actionName, new AsyncCallback<OozieAction>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						logger.warning(caught.getMessage());
					}

					@Override
					public void onSuccess(OozieAction result) {
						// TODO Auto-generated method stub
						nonLatestActions.add(result);
					}

				});
			}
		}
		return nonLatestActions;
	}

	public View getView() {
		return view;
	}

	@Override
	public void go(final HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
		init();
	}

	public BdaJob getCurrentJob() {
		return currentJob;
	}

	public void setCurrentJob(BdaJob currentJob) {
		this.currentJob = currentJob;
	}
	
	public OozieJob getCurrentOozieJob() {
		return currentOozieJob;
	}

	public void setCurrentOozieJob(OozieJob currentOozieJob) {
		this.currentOozieJob = currentOozieJob;
	}
	
	public boolean isInstanceFlag() {
		return instanceFlag;
	}

	public void setInstanceFlag(boolean instanceFlag) {
		this.instanceFlag = instanceFlag;
	}

	/**
	 * View interface
	 */
	public interface View {

		Button getClearButton();

		Button getSubmitButton();
		
		Button getRefreshButton();

		Button getCloneButton();

		Button getStopButton();

		HeaderView getHeaderView();

		TabPanel getTabPanel();

		ProgramTree getProgramTree();

		DatasetTree getDatasetTree();

		JobTree getJobTree();

		SearchTree getResultTree();

		JobDescPopupPanel getNewJobPopup();

		ParameterPopupPanel getParamPopup();

		ProgramWidget getPopupWidget();

		MonitorJobDescGrid getJobDescGrid();

		MonitorController getController();

		Widget asWidget();
		
		DiagramController getNowDiagramColler();
		
		void setPropTable(PropertyTable propertyTable);
		
		void clearPropTable();
		
		Button getHistoryButton();
		
		HistoryPopupPanel getHistoryPopup(String jobId,boolean isExample, HandlerManager eventBus);
	}

}
