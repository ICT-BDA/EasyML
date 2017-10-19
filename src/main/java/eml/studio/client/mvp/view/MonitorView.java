/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.view;

import eml.studio.client.OozieConnectionFactory;
import eml.studio.client.controller.DiagramController;
import eml.studio.client.controller.MonitorController;
import eml.studio.client.mvp.AppController;
import eml.studio.client.ui.panel.Grid.SearchGrid;
import eml.studio.client.mvp.presenter.MonitorPresenter;
import eml.studio.client.ui.tree.*;
import eml.studio.client.util.Constants;
import eml.studio.client.ui.panel.HistoryPopupPanel;
import eml.studio.client.ui.panel.JobDescPopupPanel;
import eml.studio.client.ui.panel.ParameterPopupPanel;
import eml.studio.client.ui.panel.Grid.MonitorJobDescGrid;
import eml.studio.client.ui.widget.DisclosureStackPanel;
import eml.studio.client.ui.widget.program.ProgramWidget;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MonitorView extends Generator implements MonitorPresenter.View {
	private Button refreshBtn = new Button(Constants.studioUIMsg.refresh());
	private Button cloneBtn = new Button(Constants.studioUIMsg.clone());
	private Button stopBtn = new Button(Constants.studioUIMsg.stop());
	private Button submitBtn = new Button(Constants.studioUIMsg.submit());
	private Button clearBtn = new Button(Constants.studioUIMsg.clear());
	private Button historyBtn = new Button(Constants.studioUIMsg.runHistory());

	private HeaderView headerView = new HeaderView();

	private TabPanel tabPanel = new TabPanel();
	private ProgramTree programTree;
	private DatasetTree datasetTree;
	private JobTree jobTree;
	private SearchTree resultTree;
	private MonitorJobDescGrid jobDescGrid;
	private SearchGrid searchGrid;
	private JobDescPopupPanel newJob = new JobDescPopupPanel(Constants.studioUIMsg.createJob());
	protected ParameterPopupPanel paramPopup = new ParameterPopupPanel();
	private Widget mainLayout;
	private DiagramController nowDiagramColler ;
	private HistoryPopupPanel historyPopupPanel;

	LayoutPanel layoutPanel = new LayoutPanel();
	HorizontalPanel btnPanel2 = new HorizontalPanel();

	public MonitorView() {
		mainLayout = this.createMainLayout();
	}

	public void initTabPanel() {
		tabPanel.clear();
		tabPanel.setWidth("100%");
		tabPanel.setTitle("myDisclosurePanel-headerView");
		tabPanel.add(programTree, Constants.studioUIMsg.program(), true);
		tabPanel.add(datasetTree, Constants.studioUIMsg.data(), true);
		tabPanel.add( jobTree, Constants.studioUIMsg.job(), true );
		tabPanel.selectTab(0);
	}

	@Override
	public Widget createMainLayout() {
		SplitLayoutPanel splitLayout = new SplitLayoutPanel();


		StackLayoutPanel stack = new StackLayoutPanel(Unit.EM);
		stack.setStyleName("sp");
		ScrollPanel leftcrp = new ScrollPanel();
		leftcrp.setSize("100%", "100%");
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("100%");

		programTree = ProgramTreeLoader.load(AppController.email);
		datasetTree = DatasetTreeLoader.load(AppController.email);
		jobTree = JobTreeLoader.load(AppController.email);
		resultTree = new SearchTree();
		initTabPanel();
		DisclosurePanel distackleft = new DisclosureStackPanel("Search")
		.asWidget();
		searchGrid = new SearchGrid(programTree,datasetTree,jobTree,resultTree);
		distackleft.setContent(searchGrid);
		SimplePanel tabSimPanel = new SimplePanel();
		tabSimPanel.add(tabPanel);

		vPanel.add(tabSimPanel);
		vPanel.add(distackleft);
		leftcrp.add(vPanel);
		stack.add(leftcrp, "", 0);

		StackLayoutPanel stackright = new StackLayoutPanel(Unit.EM);
		stackright.setStyleName("sp");
		DisclosurePanel distackright1 = new DisclosureStackPanel(Constants.studioUIMsg.jobPanelTitle())
		.asWidget();
		DisclosurePanel distackright2 = new DisclosureStackPanel(Constants.studioUIMsg.modulePanelTitle())
		.asWidget();
		ScrollPanel scvp = new ScrollPanel();
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setWidth("100%");

		HorizontalPanel btnPanel = new HorizontalPanel();
		historyBtn.removeStyleName("gwt-Button");
		historyBtn.addStyleName("history-button-style");
		submitBtn.setEnabled( false );
		submitBtn.removeStyleName("gwt-Button");
		submitBtn.addStyleName("button-style");
		clearBtn.removeStyleName("gwt-Button");
		clearBtn.addStyleName("button-style");
		cloneBtn.removeStyleName("gwt-Button");
		cloneBtn.addStyleName("button-style");
		refreshBtn.removeStyleName("gwt-Button");
		refreshBtn.addStyleName("button-style");
		stopBtn.removeStyleName("gwt-Button");
		stopBtn.addStyleName("button-style");

		btnPanel.add(historyBtn);
		btnPanel.add(new HTML("&nbsp;&nbsp;"));
		btnPanel.add(submitBtn);
		btnPanel.add(new HTML("&nbsp;&nbsp;"));
		btnPanel.add(clearBtn);
		btnPanel.add(new HTML("&nbsp;&nbsp;"));
		btnPanel.add(cloneBtn);
		btnPanel.add(new HTML("&nbsp;&nbsp;"));
		btnPanel.add(stopBtn);
		btnPanel.add(new HTML("&nbsp;&nbsp;"));
		btnPanel.add(refreshBtn);


		btnPanel2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		btnPanel2.add(btnPanel);
		btnPanel2.addStyleName("btnPanel");

		splitLayout.addWest(stack, 200);

		jobDescGrid = new MonitorJobDescGrid();

		distackright1.setContent(jobDescGrid);
		distackright2.setContent(propPanel);
		verticalPanel.add(distackright1);
		verticalPanel.add(distackright2);
		scvp.add(verticalPanel);
		stackright.add(scvp, "", 0);
		splitLayout.addEast(stackright, 287);

		// Use different controllers in different classes
		setController(new MonitorController(Constants.CONTROLLER_WIDTH,
				Constants.CONTROLLER_HEIGHT));
		rebuiltLayoutPanel(getController());
		splitLayout.add(layoutPanel);

		return splitLayout;
	}

	@Override
	public Button getClearButton() {
		return clearBtn;
	}

	@Override
	public Button getSubmitButton() {
		return submitBtn;
	}

	@Override
	public Button getRefreshButton() {
		return refreshBtn;
	}

	@Override
	public Button getCloneButton() {
		return cloneBtn;
	}

	@Override
	public TabPanel getTabPanel(){
		return tabPanel;
	}

	@Override
	public ProgramTree getProgramTree() {
		return programTree;
	}

	@Override
	public DatasetTree getDatasetTree() {
		return datasetTree;
	}
	@Override
	public JobTree getJobTree() {
		return jobTree;
	}
	@Override
	public SearchTree getResultTree() {
		return resultTree;
	}
	@Override
	public MonitorJobDescGrid getJobDescGrid() {
		return jobDescGrid;
	}

	@Override
	public Button getStopButton() {
		return stopBtn;
	}

	@Override
	public Button getHistoryButton() {
		return historyBtn;
	}

	@Override
	public MonitorController getController() {
		return (MonitorController) super.getController();
	}


	@Override
	public ParameterPopupPanel getParamPopup() {
		return paramPopup;
	}

	@Override
	public ProgramWidget getPopupWidget() {
		return programTree.getPopupWidget();
	}

	@Override
	public Widget asWidget() {

		DockLayoutPanel dockLayout = new DockLayoutPanel(Unit.PX);

		dockLayout.addNorth(headerView, 35);
		dockLayout.add(mainLayout);

		return dockLayout;
	}

	@Override
	public HeaderView getHeaderView() {
		return headerView;
	}

	@Override
	public JobDescPopupPanel getNewJobPopup() {
		return this.newJob;
	}

	public LayoutPanel getLayoutPanel() {
		return layoutPanel;
	}

	public void setLayoutPanel(LayoutPanel layoutPanel) {
		this.layoutPanel = layoutPanel;
	}
	@Override
	public DiagramController getNowDiagramColler() {
		return nowDiagramColler;
	}

	public void setNowDiagramColler(DiagramController nowDiagramColler) {
		this.nowDiagramColler = nowDiagramColler;
	}

	public void rebuiltLayoutPanel(DiagramController controller){
		controller.setGenerator(this);
		controller.setConnectionFactory(OozieConnectionFactory.singleton);
		nowDiagramColler = controller;

		if(AppController.email.equals("guest") || !AppController.power.split("")[2].equals("1")){
		}

		layoutPanel.clear();
		layoutPanel.add(btnPanel2);
		layoutPanel.setWidgetVerticalPosition(btnPanel2, Alignment.END);
		layoutPanel.add(controller.getViewAsScrollPanel());
	}

	@Override
	public HistoryPopupPanel getHistoryPopup(String jobId,boolean isExample,
			HandlerManager eventBus) {
		// TODO Auto-generated method stub
		historyPopupPanel = new HistoryPopupPanel(jobId, isExample, eventBus);
		return historyPopupPanel;
	}

}
