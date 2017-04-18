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
import eml.studio.client.rpc.ProgramService;
import eml.studio.client.rpc.ProgramServiceAsync;
import eml.studio.client.ui.panel.AlertPanel;
import eml.studio.client.ui.panel.DeletePanel;
import eml.studio.client.util.Constants;
import eml.studio.shared.model.Category;
import eml.studio.shared.model.Program;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Load the program manages the page data
 * 
 */
public class ProgramLoader {
  private final AdminView adminView;
  protected ProgramServiceAsync programService = GWT.create(ProgramService.class);
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
  
  public ProgramLoader(AdminView adminView, int currentPage, int lastPage, int resultSize) {
	this.adminView = adminView;
	this.currentPage = currentPage;
	this.lastPage = lastPage;
	this.resultSize = resultSize;
  }
  
  /**
	* Init
	*/
  public void init() {
	//close button
	alertPanel.getClose().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			History.fireCurrentHistoryState();
		}
					
	});
		
	//confirm button
	alertPanel.getConfirmBtn().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			History.fireCurrentHistoryState();
		}
					
	});
		
	//Calculate the number of records per page
	resultStart = (currentPage-1)*13;
	if(currentPage == lastPage){
		everyPageSize = resultSize - resultStart;
	}else{
		everyPageSize = 13;
	}

	//Reset the table row number to set the header
	adminView.getProgGrid().resize(everyPageSize+1, 6);
	adminView.getProgGrid().setText(0, 0, "Name");
	adminView.getProgGrid().setText(0, 1, "Category");
	adminView.getProgGrid().setText(0, 2, "Description");
	adminView.getProgGrid().setText(0, 3, "CreateDate");
	adminView.getProgGrid().setText(0, 4, "Owner");
	adminView.getProgGrid().setText(0, 5, "Operation");

	//Get the user information to fill in the form
	programService.loadPart(resultStart, everyPageSize, new AsyncCallback<List<Program>>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			alertPanel.setContent(caught.getMessage());
			alertPanel.show();
		}

		@Override
		public void onSuccess(List<Program> result) {
			// TODO Auto-generated method stub
			for(int i=0;i<everyPageSize;i++){
				adminView.getProgGrid().setText(i+1, 0, result.get(i).getName());
				final String categoryId = result.get(i).getCategory();
				final int a = i;
				if(categoryId.length() == 0){
					adminView.getProgGrid().setText(a+1, 1, "————");
				}else if(("我的程序").equals(categoryId) || ("my program").equals(categoryId.toLowerCase())
					|| ("共享程序").equals(categoryId) || ("shared program").equals(categoryId.toLowerCase())
					|| ("系统程序").equals(categoryId) || ("system program").equals(categoryId.toLowerCase()) ){
					adminView.getProgGrid().setText(a+1, 1, categoryId);
				}else{
					cateogryService.getCategory(categoryId, new AsyncCallback<Category>(){
	
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							adminView.getProgGrid().setText(a+1, 1, categoryId);
						}
	
						@Override
						public void onSuccess(Category result) {
							// TODO Auto-generated method stub
							if(result != null){
								adminView.getProgGrid().setText(a+1, 1, result.getPath());
							}else
								adminView.getProgGrid().setText(a+1, 1, "————");
						}
						
					});
				}
				if(result.get(i).getDescription().length() > 50){
					adminView.getProgGrid().setText(i+1, 2, result.get(i).getDescription().substring(0, 49)+"...");
				}else{
					adminView.getProgGrid().setText(i+1, 2, result.get(i).getDescription());
				}
				adminView.getProgGrid().setText(i+1, 3, result.get(i).getCreatedate());
				adminView.getProgGrid().setText(i+1, 4, result.get(i).getOwner());
				final String progId = result.get(i).getId();
				final String progName = result.get(i).getName();
				Label deleteProg = new Label();
				deleteProg.setTitle( Constants.adminUIMsg.deleteProg() );
				deleteProg.addStyleName("admin-user-delete");
				deleteProg.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						deletePanel.setContent( Constants.adminUIMsg.progDelete1() + progName + Constants.adminUIMsg.progDelete2() );
						deletePanel.show();
						deletePanel.getConfirmBtn().addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Program program = new Program();
								program.setId(progId);
								programService.delete(progId, new AsyncCallback<Void>(){

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
										alertPanel.setContent( Constants.adminUIMsg.progSuccess() );
										alertPanel.show();
									}
									
								});
							}

						});
					}

				});
				HorizontalPanel operate = new HorizontalPanel();
				operate.addStyleName("admin-user");
				operate.add(deleteProg);
				adminView.getProgGrid().setWidget(i + 1, 5, operate);
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
		adminView.getProgPage().resize(1, 25);
		adminView.getProgPage().setWidget(0, 0, first);
		adminView.getProgPage().setWidget(0, 1, prev);
		for(int count=2;count<12;count++){
			adminView.getProgPage().setWidget(0, count, new Label(headStart+""));
			headStart++;
		}
		adminView.getProgPage().setText(0, 12, "...");
		for(int count=13;count<23;count++){
			adminView.getProgPage().setWidget(0, count, new Label(tailStart+""));
			tailStart++;
		}
		adminView.getProgPage().setWidget(0, 23, next);
		adminView.getProgPage().setWidget(0, 24, last);
	}else{
		adminView.getProgPage().resize(1, pageSize+4);
		adminView.getProgPage().setWidget(0, 0, first);
		adminView.getProgPage().setWidget(0, 1, prev);
		for(int count=2;count<pageSize+2;count++){
			adminView.getProgPage().setWidget(0, count, new Label((count-1)+""));
		}
		adminView.getProgPage().setWidget(0, pageSize+2, next);
		adminView.getProgPage().setWidget(0, pageSize+3, last);
	}
	adminView.getProgPage().getWidget(0, 2).removeStyleName("gwt-Label");
	adminView.getProgPage().getWidget(0, 2).addStyleName("admin-page-selected");
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
			adminView.getProgPage().clear();
			adminView.getProgPage().resize(1, 25);
			adminView.getProgPage().setWidget(0, 0, first);
			adminView.getProgPage().setWidget(0, 1, prev);
			adminView.getProgPage().setText(0, 2, "...");
			for(int count=3;count<22;count++){
				adminView.getProgPage().setWidget(0, count, new Label(headStart+""));
				headStart++;
			}
			adminView.getProgPage().setText(0, 22, "...");
			adminView.getProgPage().setWidget(0, 23, next);
			adminView.getProgPage().setWidget(0, 24, last);
			int stylePage = (currentPage - Integer.parseInt(adminView.getProgPage().getText(0, 3)))+3;
			adminView.getProgPage().getWidget(0, stylePage).removeStyleName("gwt-Label");
			adminView.getProgPage().getWidget(0, stylePage).addStyleName("admin-page-selected");
		}else{
			adminView.getProgPage().clear();
			adminView.getProgPage().resize(1, 25);
			adminView.getProgPage().setWidget(0, 0, first);
			adminView.getProgPage().setWidget(0, 1, prev);
			for(int count=2;count<12;count++){
				adminView.getProgPage().setWidget(0, count, new Label(headStart+""));
				headStart++;
			}
			adminView.getProgPage().setText(0, 12, "...");
			for(int count=13;count<23;count++){
				adminView.getProgPage().setWidget(0, count, new Label(tailStart+""));
				tailStart++;
			}
			adminView.getProgPage().setWidget(0, 23, next);
			adminView.getProgPage().setWidget(0, 24, last);
			if(currentPage>=Integer.parseInt(adminView.getProgPage().getText(0, 2))&&currentPage<=Integer.parseInt(adminView.getProgPage().getText(0, 11))){
				adminView.getProgPage().getWidget(0, currentPage+1).removeStyleName("gwt-Label");
				adminView.getProgPage().getWidget(0, currentPage+1).addStyleName("admin-page-selected");
			}else if(currentPage>=Integer.parseInt(adminView.getProgPage().getText(0, 13))&&currentPage<=Integer.parseInt(adminView.getProgPage().getText(0, 22))){
				int stylePage = (currentPage - Integer.parseInt(adminView.getProgPage().getText(0, 13)))+13;
				adminView.getProgPage().getWidget(0, stylePage).removeStyleName("gwt-Label");
				adminView.getProgPage().getWidget(0, stylePage).addStyleName("admin-page-selected");
			}
		}
	}else{
		if(currentPage > 0 && currentPage < (pageSize + 1)){
			adminView.getProgPage().resize(1, pageSize+4);
			adminView.getProgPage().setWidget(0, 0, first);
			adminView.getProgPage().setWidget(0, 1, prev);
			for(int count=2;count<pageSize+2;count++){
				adminView.getProgPage().setWidget(0, count, new Label((count-1)+""));
			}
			adminView.getProgPage().setWidget(0, pageSize+2, next);
			adminView.getProgPage().setWidget(0, pageSize+3, last);
			adminView.getProgPage().getWidget(0, currentPage+1).removeStyleName("gwt-Label");
			adminView.getProgPage().getWidget(0, currentPage+1).addStyleName("admin-page-selected");
		}
	}
  }
}
