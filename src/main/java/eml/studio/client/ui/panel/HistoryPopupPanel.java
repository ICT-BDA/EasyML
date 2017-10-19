/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import eml.studio.client.event.ToMonitorEvent;
import eml.studio.client.mvp.AppController;
import eml.studio.client.rpc.JobService;
import eml.studio.client.rpc.JobServiceAsync;
import eml.studio.client.util.Constants;
import eml.studio.client.util.Pagination;
import eml.studio.client.util.Pagination.PageType;
import eml.studio.shared.oozie.OozieJob;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Job run history viewer popup panel
 */
public class HistoryPopupPanel extends PopupPanel {

	protected static Logger logger = Logger.getLogger(HistoryPopupPanel.class.getName());
	protected static JobServiceAsync jobSrv = GWT.create(JobService.class);

	private final HandlerManager eventBus;
	private final DateTimeFormat formatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	private final String[]  columns = {"JobName","Status","Account","Description","Create Time","End Time","Operation"};
	private final int colNum = columns.length;  //History record total columns(include head line)
	private final int rowNum = 6; //History record every page rows
	private final Label desc = new Label("History Viewer");
	private HTML closeButton = new HTML("X");
	private final Grid runHistoryGrid = new Grid();
	private Grid pageGrid = new Grid();
	private String bdaJobId;
	private boolean isExample;  //Wheather is example job
	private int totalRecords = 0;
	private boolean firstLoad = true;

	//Paging
	int currentPage = 1;
	int totalPage; 
	int everyPageSize = rowNum -1; //Exclude head title line
	int expandSize = 3; 
	int curPageRealSize = everyPageSize; // Real record size for every page
	final Label first = new Label( Constants.adminUIMsg.firstPage() );
	final Label prev = new Label( Constants.adminUIMsg.prevPage() );
	final Label next = new Label( Constants.adminUIMsg.nextPage() );
	final Label last = new Label( Constants.adminUIMsg.lastPage() );
	private Pagination pagination;

	//Searching
	private final Label startTimeLabel = new Label(Constants.studioUIMsg.startTime());
	private final DateBox startTimeBox = new DateBox();
	private final Label endTimeLabel = new Label(Constants.studioUIMsg.endTime());
	private final DateBox endTimeBox = new DateBox();
	private final Button searchBtn = new Button(Constants.studioUIMsg.historySearch());
	private Date startTime;
	private Date endTime;

	//Batch delete
	private final Button batchDelBtn = new Button(Constants.studioUIMsg.historyBatchDel());
	private final CheckBox selectAllChkBox = new CheckBox(Constants.studioUIMsg.historySelectAll());
	private Set<String> delOozieJobs = new HashSet<String>();
	private HashMap<String,CheckBox> totalViewChkBox = new HashMap<String,CheckBox>(); //Total delete checkbox<ooziejob id,checkbox>
	private HashMap<String,CheckBox> pageChkBox = new HashMap<String,CheckBox>();//Current page checkbox<ooziejob id, checkbox>

	public HistoryPopupPanel(String jobId,boolean isExample, HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.bdaJobId = jobId;
		this.isExample = isExample;
		init();
		eventBind();
	}

	/**
	 * UI Initialization
	 */
	protected void init() {
		this.setSize("850px", "400px");
		this.setGlassEnabled(true);
		this.setModal(true);

		desc.setText(desc.getText()+"  Job Id - "+bdaJobId);
		closeButton.setSize("10px", "10px");
		closeButton.setStyleName("closebtn");

		VerticalPanel topPanel = new VerticalPanel(); //Outermost vertical panel
		topPanel.add(closeButton);
		topPanel.setCellHeight(closeButton, "13px");
		topPanel.setStyleName("vpanel");
		desc.setStyleName("popupTitle");
		topPanel.add(desc);
		topPanel.setCellHeight(desc, "30px");

		HorizontalPanel optPanel = new HorizontalPanel(); //Operation panel（include search, batch delete.etc）
		optPanel.addStyleName("run-history-optPanel");
		DateTimeFormat pickerFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
		startTimeBox.setFormat(new DateBox.DefaultFormat(pickerFormat));
		startTimeBox.getDatePicker().addStyleName("run-history-datepicker-popup");
		endTimeBox.setFormat(new DateBox.DefaultFormat(pickerFormat));
		endTimeBox.getDatePicker().addStyleName("run-history-datepicker-popup");
		searchBtn.removeStyleName("gwt-Button");
		searchBtn.addStyleName("run-history-search-button");
		//The initial time is set to 2016-1-1
		endTime = new Date();
		DateTimeFormat tmpFormatter = DateTimeFormat.getFormat("yyyy-MM-dd");
		startTime = tmpFormatter.parse("2016-01-01");
		selectAllChkBox.setVisible(false);
		batchDelBtn.removeStyleName("gwt-Button");
		batchDelBtn.addStyleName("run-history-batch-del-button");

		optPanel.add(startTimeLabel); 
		optPanel.add(startTimeBox);
		optPanel.add(endTimeLabel);
		optPanel.add(endTimeBox);
		optPanel.add(searchBtn);
		if(isExample && !AppController.power.equals("111"))  //Example job only can be deleted by administrator privileges
		{}
		else
			optPanel.add(batchDelBtn);
		optPanel.add(selectAllChkBox);

		runHistoryGrid.addStyleName("run-history-table"); //Data view
		runHistoryGrid.addStyleName("table-striped");
		runHistoryGrid.addStyleName("table-hover");
		runHistoryGrid.resize(rowNum, colNum);
		for(int i=0;i<colNum;i++)
		{
			runHistoryGrid.setText(0, i, columns[i]);
		}
		initGridData();

		topPanel.add(optPanel);
		topPanel.add(runHistoryGrid);

		VerticalPanel bottomPanel = new VerticalPanel(); //Paging control
		bottomPanel.add(pageGrid);
		bottomPanel.addStyleName("run-history-bottomPanel");

		VerticalPanel panel = new VerticalPanel();
		panel.add(topPanel);
		panel.add(bottomPanel);

		this.add(panel);
		this.setStyleName("loading_container");
	}

	/**
	 * Control event binding
	 */
	public void eventBind()
	{
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				HistoryPopupPanel.this.hide();
			}
		});

		searchBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(startTimeBox.getValue()==null || endTimeBox.getValue() == null)
				{
					currentPage = 1;
					initGridData();
				}
				else if(startTimeBox.getValue().after(endTimeBox.getValue()))
				{
					Window.alert("The start time is greater than the end time. Please select again! ");
				}
				else
				{
					startTime = startTimeBox.getValue();
					endTime = endTimeBox.getValue();
					currentPage = 1;
					initGridData();
				}
			}
		});

		batchDelBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(delOozieJobs.size() == 0  && !selectAllChkBox.isVisible()) //If user not select oozie job to delete and the select all checkbox is not visible, then set the select all checkbox visible
				{
					selectAllChkBox.setVisible(true);
					selectAllChkBox.setValue(false,true);
					for (Map.Entry<String, CheckBox> entry : totalViewChkBox.entrySet()) {
						CheckBox curBox = entry.getValue();
						curBox.setVisible(true);
						curBox.setValue(false, true);
					}
				}
				else if(delOozieJobs.size() ==0 && selectAllChkBox.isVisible()) //If user not select oozie job to delete and the select all checkbox is visible, then set the select all checkbox imvisible
				{
					selectAllChkBox.setVisible(false);
					for (Map.Entry<String, CheckBox> entry : totalViewChkBox.entrySet()) {
						CheckBox curBox = entry.getValue();
						curBox.setVisible(false);
					}
				}
				else
				{
					if(totalPage == 1 && delOozieJobs.size() == curPageRealSize)
						Window.alert("Can't delete all record, you should keep at least one record!");
					else{
						final DeletePanel deletePanel = new DeletePanel();
						deletePanel.setContent(Constants.studioUIMsg.historyDelHint()+ delOozieJobs.size()+" ?");
						deletePanel.show();
						deletePanel.getConfirmBtn().addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub

								deleteBatchOozJob(delOozieJobs);
								deletePanel.hide();
								selectAllChkBox.setValue(false);
							}

						});
					}
				}
			}
		});
		//Select all check box only valid for the current page
		selectAllChkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>(){

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				// TODO Auto-generated method stub
				if(event.getValue())
				{
					for (Map.Entry<String, CheckBox> entry : totalViewChkBox.entrySet()) {
						CheckBox curBox = entry.getValue();
						curBox.setValue(false, true);
					}	
					for (Map.Entry<String, CheckBox> entry : pageChkBox.entrySet()) {
						CheckBox curBox = entry.getValue();
						curBox.setValue(true,true);
					}
				}
				else
				{
					for (Map.Entry<String, CheckBox> entry : totalViewChkBox.entrySet()) {
						CheckBox curBox = entry.getValue();
						curBox.setValue(false, true);
					}	
					for (Map.Entry<String, CheckBox> entry : pageChkBox.entrySet()) {
						CheckBox curBox = entry.getValue();
						curBox.setValue(false,true);
					}
				}
			}});

		pageGrid.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				int index = pageGrid.getCellForEvent(event).getCellIndex();
				String pageText = pageGrid.getText(0, index);
				currentPage = pagination.getCurrentPage(currentPage, pageText, totalPage);
				pagination.reload(currentPage);
				pageLoader(currentPage);
			}
		});
	}

	/**
	 * Job run history data list initialization
	 */
	public void initGridData()
	{
		jobSrv.getRefOozieJobNum(bdaJobId,startTime,endTime,firstLoad,new AsyncCallback<Integer>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				logger.warning("The number of run history record statistics failed！");
			}

			@Override
			public void onSuccess(Integer result) {
				// TODO Auto-generated method stub
				if(result != null)
				{
					firstLoad = false;
					totalRecords = result;
					totalPage = result/everyPageSize;
					if(result%everyPageSize>0)
						totalPage = totalPage + 1;
					logger.info("record size = "+result+";totalPage="+totalPage+";startTime = "+startTime.toString() +";endTime ="+endTime.toString());
					initPageGrid();
					pageLoader(currentPage);
				}
			}

		});
	}

	/**
	 * Paging control initialization
	 */
	public void initPageGrid()
	{
		pagination = new Pagination(pageGrid,totalPage,PageType.MIDDLE);
		if(currentPage ==1)
			pagination.load(); //Initialize first page
		else
			pagination.reload(currentPage);
	}

	/**
	 * Paging data loading
	 * 
	 * @param currentPage current page number
	 */
	public void pageLoader(final int currentPage)
	{
		logger.info("current page="+currentPage);
		final int startIndex = (currentPage-1)*everyPageSize;
		int endIndex = startIndex;
		if(currentPage == totalPage)
			endIndex = totalRecords;
		else
			endIndex = startIndex + everyPageSize;
		final int size = endIndex - startIndex;
		pageChkBox = new HashMap<String,CheckBox>();

		jobSrv.getRefOozieJobPage(bdaJobId, startIndex, size, startTime, endTime, new AsyncCallback<List<OozieJob>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				logger.warning("The paging data of run history records loading failed! Page="+currentPage);
			}

			@Override
			public void onSuccess(final List<OozieJob> result) {
				// TODO Auto-generated method stub
				if(result != null)
				{
					curPageRealSize = result.size();
					logger.info("start="+startIndex+";end="+(startIndex+size)+";curSize = "+ result.size());
					pageGrid.addStyleName("run-history-page");

					clearHistoryGridData();
					for(int i = 0 ; i < result.size() ; i++)
					{
						final OozieJob oozieJob = result.get(i);
						runHistoryGrid.setText(i+1, 0, oozieJob.getAppName());
						runHistoryGrid.setText(i+1, 1, oozieJob.getStatus());
						runHistoryGrid.setText(i+1, 2, oozieJob.getAccount());
						runHistoryGrid.setText(i+1, 3, oozieJob.getDescription());
						if(oozieJob.getCreatedTime() == null)
							runHistoryGrid.setText(i+1, 4, "");
						else
							runHistoryGrid.setText(i+1, 4, formatter.format(oozieJob.getCreatedTime()));
						if(oozieJob.getEndTime() == null)
							runHistoryGrid.setText(i+1, 5, "");
						else
							runHistoryGrid.setText(i+1, 5, formatter.format(oozieJob.getEndTime()));
						final HorizontalPanel optPanel = new HorizontalPanel();
						Label historyViewLabel = new Label();
						historyViewLabel.setTitle( Constants.studioUIMsg.historyView());
						historyViewLabel.addStyleName("run-history-view");
						historyViewLabel.addClickHandler(new ClickHandler(){

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								HistoryPopupPanel.this.hide();
								String message =bdaJobId +"&instance="+oozieJob.getId();
								goToOozInstance(oozieJob.getId(),message);
							}
						});

						Label deleteLabel = new Label();
						deleteLabel.setTitle(Constants.studioUIMsg.historyDelete());
						deleteLabel.setStyleName("run-history-delete");
						deleteLabel.addClickHandler(new ClickHandler(){

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								if(result.size()==1 && totalPage == 1)  //Only one page and only include one record
									Window.alert("There has only one record, can't be deleted!");
								else
								{
									final DeletePanel deletePanel = new DeletePanel();
									deletePanel.setContent("Sure to delete this history record?");
									deletePanel.show();
									deletePanel.getConfirmBtn().addClickHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {
											// TODO Auto-generated method stub
											deleteOozJob(oozieJob.getId());
											deletePanel.hide();
										}
									});
								}
							}

						});

						CheckBox delChkBox = null;
						if(totalViewChkBox.containsKey(oozieJob.getId()))
							delChkBox = totalViewChkBox.get(oozieJob.getId());
						else
						{	  
							delChkBox = new CheckBox(); 
							delChkBox.setStyleName("run-history-record-chk-box");
							delChkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>(){

								@Override
								public void onValueChange(
										ValueChangeEvent<Boolean> event) {
									// TODO Auto-generated method stub
									if(event.getValue())
									{
										delOozieJobs.add(oozieJob.getId());
									}
									else
									{
										if(delOozieJobs.contains(oozieJob.getId()))
											delOozieJobs.remove(oozieJob.getId());
									}
								}

							});
							totalViewChkBox.put(oozieJob.getId(), delChkBox);
						}
						pageChkBox.put(oozieJob.getId(), delChkBox);

						if(selectAllChkBox.isVisible())
							delChkBox.setVisible(true);
						else
							delChkBox.setVisible(false);

						optPanel.add(historyViewLabel);
						if(isExample && !AppController.power.equals("111"))
						{}
						else
							optPanel.add(deleteLabel);
						optPanel.add(delChkBox);
						runHistoryGrid.setWidget(i+1, 6, optPanel);
					}
				}
			}});
	}

	/**
	 * Batch delete some job history records
	 * 
	 * @param oozJobIds oozie job id list to delete
	 */
	public void deleteBatchOozJob(final Set<String> oozJobIds)
	{
		jobSrv.deleteBatchOozieJob(oozJobIds, new AsyncCallback<Void>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				logger.warning("Batch delete run history record failed！ History record number="+oozJobIds.size());
			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub

				totalRecords = totalRecords - oozJobIds.size();
				totalPage = totalRecords/everyPageSize;
				if(totalRecords%everyPageSize>0)
					totalPage = totalPage + 1;
				logger.info("current record size = "+totalRecords+"totalPage="+totalPage);
				if(currentPage > totalPage)
					currentPage = totalPage;
				delOozieJobs = new HashSet<String>();
				for(String jobId : oozJobIds)
				{
					if(totalViewChkBox.containsKey(jobId))
						totalViewChkBox.remove(jobId);
				}
				initPageGrid();
				pageLoader(currentPage);
			}

		});
	}

	/**
	 * Delete a run history record
	 * 
	 * @param oozJobId  oozie job id to delete
	 */
	public void deleteOozJob(final String oozJobId)
	{
		jobSrv.deleteOozieJob(oozJobId, new AsyncCallback<Void>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				logger.warning("Delete run history record failed! OozJobId="+oozJobId);
			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				totalRecords = totalRecords - 1;
				totalPage = totalRecords/everyPageSize;
				if(totalRecords%everyPageSize>0)
					totalPage = totalPage + 1;
				logger.info("current record size = "+totalRecords+"totalPage="+totalPage);
				if(currentPage > totalPage)
					currentPage = totalPage;
				initPageGrid();
				pageLoader(currentPage);
			}

		});
	}

	/**
	 * Clear history data
	 */
	public void clearHistoryGridData()
	{
		for(int i=1 ; i<rowNum;i++)
		{
			for(int j = 0; j<colNum; j++)
				runHistoryGrid.clearCell(i, j);
		}
	}

	/**
	 * Jump to a oozie instance( which need to synchronize current oozie job actions before jumping）
	 * 
	 * @param oozJobId   oozie job id
	 * @param message page url
	 */
	public void goToOozInstance(final String oozJobId,final String message)
	{
		jobSrv.updateJobActionStatus(oozJobId, new AsyncCallback<Void>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				logger.warning("Synchronize current oozie instance actions status failed! Oozie job id ="+oozJobId);
			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				eventBus.fireEvent(new ToMonitorEvent(message));
			}
		});
	}

	@Override
	public void center(){
		super.center();
	}

}
