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
import eml.studio.client.rpc.ProgramService;
import eml.studio.client.rpc.ProgramServiceAsync;
import eml.studio.client.ui.panel.AlertPanel;
import eml.studio.client.ui.panel.DeletePanel;
import eml.studio.client.util.Constants;
import eml.studio.shared.model.Category;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Load Directory management page data
 * 
 */
public class CategoryLoader {
  private final AdminView adminView;
  protected CategoryServiceAsync categoryService = GWT.create(CategoryService.class);
  protected ProgramServiceAsync programService = GWT.create(ProgramService.class);
  protected DatasetServiceAsync datasetService = GWT.create(DatasetService.class);
  protected DeletePanel deletePanel = new DeletePanel();
  protected AlertPanel deleteAlert = new AlertPanel();
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
  
  public CategoryLoader(AdminView adminView, int currentPage, int lastPage, int resultSize) {
	this.adminView = adminView;
	this.currentPage = currentPage;
	this.lastPage = lastPage;
	this.resultSize = resultSize;
  }
  
  /**
	* Load data
	*/
  public void init() {
	deleteAlert.getClose().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			History.fireCurrentHistoryState();
		}
			
	});
	
	deleteAlert.getConfirmBtn().addClickHandler(new ClickHandler(){

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

	//Set the header
	adminView.getCateGrid().resize(everyPageSize+1, 4);
	adminView.getCateGrid().setText(0, 0, "Name");
	adminView.getCateGrid().setText(0, 1, "Path");
	adminView.getCateGrid().setText(0, 2, "Create Time");
	adminView.getCateGrid().setText(0, 3, "Operation");

	//Get category info
	categoryService.getCategory(resultStart, everyPageSize, new AsyncCallback<List<Category>>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			deleteAlert.setContent(caught.getMessage());
			deleteAlert.show();
		}

		@Override
		public void onSuccess(List<Category> result) {
			// TODO Auto-generated method stub
			for(int i=0;i<everyPageSize;i++){
				adminView.getCateGrid().setText(i + 1, 0, result.get(i).getName());
				if(result.get(i).getPath().isEmpty()){
					adminView.getCateGrid().setText(i + 1, 1, "无");
				}else{
					adminView.getCateGrid().setText(i + 1, 1, result.get(i).getPath());
				}
				adminView.getCateGrid().setText(i + 1, 2, result.get(i).getCreatetime());
				final String cateName = result.get(i).getName();
				final String cateType = result.get(i).getType();
				final String catePath = result.get(i).getPath();
				final String fathId = result.get(i).getFatherid();
				Label deleteCate = new Label();
				deleteCate.setTitle( Constants.adminUIMsg.deleteCate() );
				deleteCate.addStyleName("admin-user-delete");
				deleteCate.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						deletePanel.setContent( Constants.adminUIMsg.delete1() + cateName + Constants.adminUIMsg.delete2() );
						deletePanel.show();
						deletePanel.getConfirmBtn().addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Category category = new Category();
								category.setType(cateType);
								categoryService.getCategory(category, "and path = '"+catePath+"' or path like '"+catePath+"%'", new AsyncCallback<List<Category>>(){

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										deletePanel.hide();
										deleteAlert.setContent(caught.getMessage());
										deleteAlert.show();
									}

									@Override
									public void onSuccess(List<Category> result) {
										// TODO Auto-generated method stub
										if(result != null){
											for(Category category : result){
												Category delete = new Category();
												delete.setId(category.getId());
												final String cateType = category.getType();
												final String oldCate = category.getId();
												categoryService.deleteCategory(delete, new AsyncCallback<Category>(){

													@Override
													public void onFailure(
															Throwable caught) {
														// TODO Auto-generated method stub
														deletePanel.hide();
														deleteAlert.setContent(caught.getMessage());
														deleteAlert.show();
													}

													@Override
													public void onSuccess(Category result) {
														// TODO Auto-generated method stub
														if(result == null){
															if(cateType.equals("data")){
																datasetService.editCategory(oldCate, fathId, new AsyncCallback<String>(){

																	@Override
																	public void onFailure(
																			Throwable caught) {
																		// TODO Auto-generated method stub
																		caught.getMessage();
																	}

																	@Override
																	public void onSuccess(String result) {
																		// TODO Auto-generated method stub
																		if(result == "success"){
																			deletePanel.hide();
																			deleteAlert.setContent( Constants.adminUIMsg.delete3() );
																			deleteAlert.show();
																		}else{
																			deletePanel.hide();
																			deleteAlert.setContent(result);
																			deleteAlert.show();
																		}
																	}
																	
																});
															}else{
																programService.editCategory(oldCate, fathId, new AsyncCallback<String>(){
	
																	@Override
																	public void onFailure(Throwable caught) {
																		// TODO Auto-generated method stub
																		caught.getMessage();
																	}
	
																	@Override
																	public void onSuccess(String result) {
																		// TODO Auto-generated method stub
																		if(result == "success"){
																			deletePanel.hide();
																			deleteAlert.setContent( Constants.adminUIMsg.delete3() );
																			deleteAlert.show();
																		}else{
																			deletePanel.hide();
																			deleteAlert.setContent(result);
																			deleteAlert.show();
																		}
																	}
																					
																});
															}
														}
													}
												});
											}
										}else{
											deletePanel.hide();
											deleteAlert.setContent( Constants.adminUIMsg.delete4() );
											deleteAlert.show();
										}
									}
													
								});
							}
						});
					}
				});
				HorizontalPanel operate = new HorizontalPanel();
				operate.addStyleName("admin-user");
				operate.add(deleteCate);
				adminView.getCateGrid().setWidget(i + 1, 3, operate);
			}
		}
	});
  }
  
  /**
	* Frist load page loader
   *
   * @param pageSize
	*/
  public void pageLoader(int pageSize){
	if(pageSize>20){
		headStart = 1;
		tailStart = lastPage - 9;
		adminView.getCatePage().resize(1, 25);
		adminView.getCatePage().setWidget(0, 0, first);
		adminView.getCatePage().setWidget(0, 1, prev);
		for(int count=2;count<12;count++){
			adminView.getCatePage().setWidget(0, count, new Label(headStart+""));
			headStart++;
		}
		adminView.getCatePage().setText(0, 12, "...");
		for(int count=13;count<23;count++){
			adminView.getCatePage().setWidget(0, count, new Label(tailStart+""));
			tailStart++;
		}
		adminView.getCatePage().setWidget(0, 23, next);
		adminView.getCatePage().setWidget(0, 24, last);
	}else{
		adminView.getCatePage().resize(1, pageSize+4);
		adminView.getCatePage().setWidget(0, 0, first);
		adminView.getCatePage().setWidget(0, 1, prev);
		for(int count=2;count<pageSize+2;count++){
			adminView.getCatePage().setWidget(0, count, new Label((count-1)+""));
		}
		adminView.getCatePage().setWidget(0, pageSize+2, next);
		adminView.getCatePage().setWidget(0, pageSize+3, last);
	}
	adminView.getCatePage().getWidget(0, 2).removeStyleName("gwt-Label");
	adminView.getCatePage().getWidget(0, 2).addStyleName("admin-page-selected");
  }

  /**
	* Reload page loader
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
			adminView.getCatePage().clear();
			adminView.getCatePage().resize(1, 25);
			adminView.getCatePage().setWidget(0, 0, first);
			adminView.getCatePage().setWidget(0, 1, prev);
			adminView.getCatePage().setText(0, 2, "...");
			for(int count=3;count<22;count++){
				adminView.getCatePage().setWidget(0, count, new Label(headStart+""));
				headStart++;
			}
			adminView.getCatePage().setText(0, 22, "...");
			adminView.getCatePage().setWidget(0, 23, next);
			adminView.getCatePage().setWidget(0, 24, last);
			int stylePage = (currentPage - Integer.parseInt(adminView.getCatePage().getText(0, 3)))+3;
			adminView.getCatePage().getWidget(0, stylePage).removeStyleName("gwt-Label");
			adminView.getCatePage().getWidget(0, stylePage).addStyleName("admin-page-selected");
		}else{
			adminView.getCatePage().clear();
			adminView.getCatePage().resize(1, 25);
			adminView.getCatePage().setWidget(0, 0, first);
			adminView.getCatePage().setWidget(0, 1, prev);
			for(int count=2;count<12;count++){
				adminView.getCatePage().setWidget(0, count, new Label(headStart+""));
				headStart++;
			}
			adminView.getCatePage().setText(0, 12, "...");
			for(int count=13;count<23;count++){
				adminView.getCatePage().setWidget(0, count, new Label(tailStart+""));
				tailStart++;
			}
			adminView.getCatePage().setWidget(0, 23, next);
			adminView.getCatePage().setWidget(0, 24, last);
			if(currentPage>=Integer.parseInt(adminView.getCatePage().getText(0, 2))&&currentPage<=Integer.parseInt(adminView.getCatePage().getText(0, 11))){
				adminView.getCatePage().getWidget(0, currentPage+1).removeStyleName("gwt-Label");
				adminView.getCatePage().getWidget(0, currentPage+1).addStyleName("admin-page-selected");
			}else if(currentPage>=Integer.parseInt(adminView.getCatePage().getText(0, 13))&&currentPage<=Integer.parseInt(adminView.getCatePage().getText(0, 22))){
				int stylePage = (currentPage - Integer.parseInt(adminView.getCatePage().getText(0, 13)))+13;
				adminView.getCatePage().getWidget(0, stylePage).removeStyleName("gwt-Label");
				adminView.getCatePage().getWidget(0, stylePage).addStyleName("admin-page-selected");
			}
		}
	}else{
		if(currentPage > 0 && currentPage < (pageSize + 1)){
			adminView.getCatePage().clear();
			adminView.getCatePage().resize(1, pageSize+4);
			adminView.getCatePage().setWidget(0, 0, first);
			adminView.getCatePage().setWidget(0, 1, prev);
			for(int count=2;count<pageSize+2;count++){
				adminView.getCatePage().setWidget(0, count, new Label((count-1)+""));
			}
			adminView.getCatePage().setWidget(0, pageSize+2, next);
			adminView.getCatePage().setWidget(0, pageSize+3, last);
			adminView.getCatePage().getWidget(0, currentPage+1).removeStyleName("gwt-Label");
			adminView.getCatePage().getWidget(0, currentPage+1).addStyleName("admin-page-selected");
		}
	}
  }
}
