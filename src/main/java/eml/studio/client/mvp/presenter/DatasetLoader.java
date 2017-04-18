/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.presenter;

import java.util.List;
import eml.studio.client.mvp.view.AdminView;
import eml.studio.client.rpc.CategoryService;
import eml.studio.client.rpc.CategoryServiceAsync;
import eml.studio.client.rpc.DatasetService;
import eml.studio.client.rpc.DatasetServiceAsync;
import eml.studio.client.ui.panel.AlertPanel;
import eml.studio.client.ui.panel.DeletePanel;
import eml.studio.client.util.Constants;
import eml.studio.shared.model.Category;
import eml.studio.shared.model.Dataset;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Load Data management page data is loaded
 * 
 */
public class DatasetLoader {
  private final AdminView adminView;
  protected DatasetServiceAsync datasetService = GWT.create(DatasetService.class);
  protected CategoryServiceAsync cateogryService = GWT.create(CategoryService.class);
  protected AlertPanel alertPanel = new AlertPanel();
  protected DeletePanel deletePanel = new DeletePanel();
  final DateTimeFormat formatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
  int currentPage = 1;
  int lastPage;
  int headStart;
  int tailStart;
  int resultSize;
  int resultStart;
  int everyPageSize;
  final Label first = new Label( Constants.adminUIMsg.firstPage() );
  final Label prev = new Label( Constants.adminUIMsg.prevPage() );
  final Label next = new Label( Constants.adminUIMsg.nextPage() );
  final Label last = new Label( Constants.adminUIMsg.lastPage() );
  
  public DatasetLoader(AdminView adminView, int currentPage, int lastPage, int resultSize) {
	this.adminView = adminView;
	this.currentPage = currentPage;
	this.lastPage = lastPage;
	this.resultSize = resultSize;
  }
  
  /**
	* Init data
	*/
  public void init() {
	alertPanel.getClose().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			History.fireCurrentHistoryState();
		}
				
	});
		
	alertPanel.getConfirmBtn().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			History.fireCurrentHistoryState();
		}
				
	});
	  
	resultStart = (currentPage-1)*13;
	if(currentPage == lastPage){
		everyPageSize = resultSize - resultStart;
	}else{
		everyPageSize = 13;
	}
	
	//Reset the table row number and set the header
	adminView.getDataGrid().resize(everyPageSize+1, 7);
	adminView.getDataGrid().setText(0, 0, "Name");
	adminView.getDataGrid().setText(0, 1, "Category");
	adminView.getDataGrid().setText(0, 2, "Description");
	adminView.getDataGrid().setText(0, 3, "Version");
	adminView.getDataGrid().setText(0, 4, "CreateDate");
	adminView.getDataGrid().setText(0, 5, "Owner");
	adminView.getDataGrid().setText(0, 6, "Operation");

	//Get the data into the form
	datasetService.loadPart(resultStart, everyPageSize, new AsyncCallback<List<Dataset>>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			alertPanel.setContent(caught.getMessage());
			alertPanel.show();
		}

		@Override
		public void onSuccess(List<Dataset> result) {
			// TODO Auto-generated method stub
			for(int i=0;i<everyPageSize;i++){
				adminView.getDataGrid().setText(i+1, 0, result.get(i).getName());
				final String categoryId = result.get(i).getCategory();
				final int a = i;
				if(categoryId.length() == 0){
					adminView.getDataGrid().setText(a+1, 1, "————");
				}else if(("我的数据").equals(categoryId) || ("my data").equals(categoryId.toLowerCase())
					|| ("共享数据").equals(categoryId) || ("shared data").equals(categoryId.toLowerCase())
					|| ("系统数据").equals(categoryId) || ("system data").equals(categoryId.toLowerCase()) ){
					adminView.getDataGrid().setText(a+1, 1, categoryId);
				}else{
					cateogryService.getCategory(categoryId, new AsyncCallback<Category>(){
	
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							adminView.getDataGrid().setText(a+1, 1, categoryId);
						}
	
						@Override
						public void onSuccess(Category result) {
							// TODO Auto-generated method stub
							if(result != null){
								adminView.getDataGrid().setText(a+1, 1, result.getPath());
							}else
								adminView.getDataGrid().setText(a+1, 1, "————");
						}
						
					});
				}
				if(result.get(i).getDescription().length() > 50){
					adminView.getDataGrid().setText(i+1, 2, result.get(i).getDescription().substring(0, 49)+"...");
				}else{
					adminView.getDataGrid().setText(i+1, 2, result.get(i).getDescription());
				}
				adminView.getDataGrid().setText(i+1, 3, result.get(i).getVersion());
				adminView.getDataGrid().setText(i+1, 4, result.get(i).getCreatedate());
				adminView.getDataGrid().setText(i+1, 5, result.get(i).getOwner());

				final String dataId = result.get(i).getId();
				final String dataName = result.get(i).getName();
				Label deleteData = new Label();
				deleteData.setTitle( Constants.adminUIMsg.deleteData() );
				deleteData.addStyleName("admin-user-delete");
				deleteData.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						deletePanel.setContent( Constants.adminUIMsg.dataDelete1() + dataName + Constants.adminUIMsg.dataDelete2() );
						deletePanel.show();
						deletePanel.getConfirmBtn().addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Dataset dataset = new Dataset();
								dataset.setId(dataId);
								datasetService.delete(dataId, new AsyncCallback<Void>(){

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										deletePanel.hide();
										alertPanel.setContent(caught.getMessage());
										alertPanel.show();
									}

									@Override
									public void onSuccess(Void result) {
										// TODO Auto-generated method stub
										deletePanel.hide();
										alertPanel.setContent( Constants.adminUIMsg.dataSuccess() );
										alertPanel.show();
									}
									
								});
							}

						});
					}

				});
				HorizontalPanel operate = new HorizontalPanel();
				operate.addStyleName("admin-user");
				operate.add(deleteData);
				adminView.getDataGrid().setWidget(i + 1, 6, operate);
			}
		}
	});
  }
  
  /**
	* Load page loader
   *
   * @param pageSize
	*/
  public void pageLoader(int pageSize){
	if(pageSize>20){
		headStart = 1;
		tailStart = lastPage - 9;
		adminView.getDataPage().resize(1, 25);
		adminView.getDataPage().setWidget(0, 0, first);
		adminView.getDataPage().setWidget(0, 1, prev);
		for(int count=2;count<12;count++){
			adminView.getDataPage().setWidget(0, count, new Label(headStart+""));
			headStart++;
		}
		adminView.getDataPage().setText(0, 12, "...");
		for(int count=13;count<23;count++){
			adminView.getDataPage().setWidget(0, count, new Label(tailStart+""));
			tailStart++;
		}
		adminView.getDataPage().setWidget(0, 23, next);
		adminView.getDataPage().setWidget(0, 24, last);
	}else{
		adminView.getDataPage().resize(1, pageSize+4);
		adminView.getDataPage().setWidget(0, 0, first);
		adminView.getDataPage().setWidget(0, 1, prev);
		for(int count=2;count<pageSize+2;count++){
			adminView.getDataPage().setWidget(0, count, new Label((count-1)+""));
		}
		adminView.getDataPage().setWidget(0, pageSize+2, next);
		adminView.getDataPage().setWidget(0, pageSize+3, last);
	}
	adminView.getDataPage().getWidget(0, 2).removeStyleName("gwt-Label");
	adminView.getDataPage().getWidget(0, 2).addStyleName("admin-page-selected");
  }

  /**
	* Reload page loader
   *
   * @param pageSize
	*/
  public void pageReloader(int pageSize){
	if(pageSize > 20){
		if(currentPage == 1 || currentPage == lastPage){
			headStart = 1;
			tailStart = lastPage - 9;
		}else{
			if(currentPage > 9 && (currentPage+9)<lastPage){
				headStart = currentPage - 9;
				tailStart = 0;
			}else{
				headStart = 1;
				tailStart = lastPage - 9;
			}
		}
		if(tailStart == 0){
			adminView.getDataPage().clear();
			adminView.getDataPage().resize(1, 25);
			adminView.getDataPage().setWidget(0, 0, first);
			adminView.getDataPage().setWidget(0, 1, prev);
			adminView.getDataPage().setText(0, 2, "...");
			for(int count=3;count<22;count++){
				adminView.getDataPage().setWidget(0, count, new Label(headStart+""));
				headStart++;
			}
			adminView.getDataPage().setText(0, 22, "...");
			adminView.getDataPage().setWidget(0, 23, next);
			adminView.getDataPage().setWidget(0, 24, last);
			int stylePage = (currentPage - Integer.parseInt(adminView.getDataPage().getText(0, 3)))+3;
			adminView.getDataPage().getWidget(0, stylePage).removeStyleName("gwt-Label");
			adminView.getDataPage().getWidget(0, stylePage).addStyleName("admin-page-selected");
		}else{
			adminView.getDataPage().clear();
			adminView.getDataPage().resize(1, 25);
			adminView.getDataPage().setWidget(0, 0, first);
			adminView.getDataPage().setWidget(0, 1, prev);
			for(int count=2;count<12;count++){
				adminView.getDataPage().setWidget(0, count, new Label(headStart+""));
				headStart++;
			}
			adminView.getDataPage().setText(0, 12, "...");
			for(int count=13;count<23;count++){
				adminView.getDataPage().setWidget(0, count, new Label(tailStart+""));
				tailStart++;
			}
			adminView.getDataPage().setWidget(0, 23, next);
			adminView.getDataPage().setWidget(0, 24, last);
			if(currentPage>=Integer.parseInt(adminView.getDataPage().getText(0, 2))&&currentPage<=Integer.parseInt(adminView.getDataPage().getText(0, 11))){
				adminView.getDataPage().getWidget(0, currentPage+1).removeStyleName("gwt-Label");
				adminView.getDataPage().getWidget(0, currentPage+1).addStyleName("admin-page-selected");
			}else if(currentPage>=Integer.parseInt(adminView.getDataPage().getText(0, 13))&&currentPage<=Integer.parseInt(adminView.getDataPage().getText(0, 22))){
				int stylePage = (currentPage - Integer.parseInt(adminView.getDataPage().getText(0, 13)))+13;
				adminView.getDataPage().getWidget(0, stylePage).removeStyleName("gwt-Label");
				adminView.getDataPage().getWidget(0, stylePage).addStyleName("admin-page-selected");
			}
		}
	}else{
		if(currentPage > 0 && currentPage < (pageSize + 1)){
			adminView.getDataPage().resize(1, pageSize+4);
			adminView.getDataPage().setWidget(0, 0, first);
			adminView.getDataPage().setWidget(0, 1, prev);
			for(int count=2;count<pageSize+2;count++){
				adminView.getDataPage().setWidget(0, count, new Label((count-1)+""));
			}
			adminView.getDataPage().setWidget(0, pageSize+2, next);
			adminView.getDataPage().setWidget(0, pageSize+3, last);
			adminView.getDataPage().getWidget(0, currentPage+1).removeStyleName("gwt-Label");
			adminView.getDataPage().getWidget(0, currentPage+1).addStyleName("admin-page-selected");
		}
	}
  }
}
