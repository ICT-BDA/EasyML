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
import eml.studio.client.ui.panel.CateAddPanel;
import eml.studio.client.ui.panel.CategoryPanel;
import eml.studio.client.ui.panel.DeletePanel;
import eml.studio.client.util.Constants;
import eml.studio.client.util.Pagination;
import eml.studio.client.util.TimeUtils;
import eml.studio.client.util.UUID;
import eml.studio.shared.model.Category;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
/**
 * Load Directory management page data
 */
public class CategoryLoader {
	private final AdminView adminView;
	private CategoryServiceAsync categoryService = GWT.create(CategoryService.class);
	private ProgramServiceAsync programService = GWT.create(ProgramService.class);
	private DatasetServiceAsync datasetService = GWT.create(DatasetService.class);
	private CateAddPanel progAddPanel = new CateAddPanel();			
	private CategoryPanel progCatePanel = new CategoryPanel("prog");	
	private CateAddPanel dataAddPanel = new CateAddPanel();
	private CategoryPanel dataCatePanel = new CategoryPanel("data");	
	private DeletePanel deletePanel = new DeletePanel();				
	private AlertPanel addAlert = new AlertPanel();					
	private AlertPanel deleteAlert = new AlertPanel();				
	private AlertPanel searchAlert = new AlertPanel();				
	private int currentPage = 1;										
	private int pageSize;
	private int lastPage;
	private int resultSize;
	private int resultStart;
	private int everyPageSize;
	private Grid page = new Grid();									
	private Pagination pagination;									
	private Category searchCategory;									
	private String searchName;
	private String searchPath;
	private boolean searchFlag = false; 


	public CategoryLoader(AdminView adminView) {
		this.adminView = adminView;
		bind();
	}

	public void go(){
		init();
	}

	/**
	 * Event bind
	 */
	private void bind(){
		
		addAlert.getClose().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				progAddPanel.hide();
				dataAddPanel.hide();
				History.fireCurrentHistoryState();
			}

		});

		addAlert.getConfirmBtn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				progAddPanel.hide();
				dataAddPanel.hide();
				History.fireCurrentHistoryState();
			}

		});

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

		adminView.getProgAdd().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				progAddPanel.show();
			}
		});

		adminView.getDataAdd().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				dataAddPanel.show();
			}
		});

		progAddPanel.getFatherBox().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				progCatePanel.show();

				Tree tree = progCatePanel.getCategoryTree();
				tree.addSelectionHandler(new SelectionHandler<TreeItem>(){
					@Override
					public void onSelection(SelectionEvent<TreeItem> event) {
						// TODO Auto-generated method stub
						Boolean state = event.getSelectedItem().getState();
						if(!state){
							progCatePanel.hide();
							String cate_str = event.getSelectedItem().getText();
							if(cate_str.equals("共享程序") || cate_str.toLowerCase().equals("shared program") 
									|| cate_str.startsWith("我的程序") || cate_str.toLowerCase().startsWith("my program") 
									|| cate_str.equals("系统程序") || cate_str.toLowerCase().equals("system program") ){
								progAddPanel.getFatherBox().setValue(cate_str);
							}else{
								String sysPath = progCatePanel.getSysTreeCateMap().get(event.getSelectedItem());
								String shrPath = progCatePanel.getShrTreeCateMap().get(event.getSelectedItem());
								String myPath = progCatePanel.getMyTreeCateMap().get(event.getSelectedItem());
								if(sysPath != null){
									progAddPanel.getFatherBox().setValue(sysPath);
								}else if(shrPath != null){
									progAddPanel.getFatherBox().setValue(shrPath);
								}else if(myPath != null){
									progAddPanel.getFatherBox().setValue(myPath);
								}else
									progAddPanel.getFatherBox().setValue(null);
							}
						}
					}
				});
			}

		});

		progAddPanel.getCancelBtn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				addAlert.hide();
				dataCatePanel.hide();
				progCatePanel.hide();
				dataAddPanel.hide();
				progAddPanel.hide();
				progAddPanel.getNameBox().setValue("");
				progAddPanel.getFatherBox().setValue("");
			}

		});

		progAddPanel.getClose().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				addAlert.hide();
				dataCatePanel.hide();
				progCatePanel.hide();
				dataAddPanel.hide();
				progAddPanel.hide();
				progAddPanel.getNameBox().setValue("");
				progAddPanel.getFatherBox().setValue("");
			}

		});

		progAddPanel.getConfirmBtn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String name = progAddPanel.getNameBox().getValue();
				String fathPath = progAddPanel.getFatherBox().getValue();

				String path = fathPath + ">" + name;
				if(name == "null" || "".equals(name)){
					progAddPanel.getNameErrorLabel().setText( Constants.adminUIMsg.alert1() );
					progAddPanel.getNameErrorLabel().setVisible(true);
					progAddPanel.getFathErrorLabel().setVisible(false);
				}else if(fathPath == "null" || "".equals(fathPath)){
					progAddPanel.getFathErrorLabel().setText( Constants.adminUIMsg.alert2() );
					progAddPanel.getNameErrorLabel().setVisible(false);
					progAddPanel.getFathErrorLabel().setVisible(true);
				}else{
					Category category = new Category();
					final String id = UUID.randomUUID();
					category.setId(id);
					category.setName(name);
					category.setType("prog");
					category.setPath(path);
					category.setHaschild(false);
					category.setCreatetime(TimeUtils.timeNow());
					categoryService.insertCategory(category, new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							addAlert.setContent(caught.getMessage());
							addAlert.show();
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							if(result.equals("success")){
								addAlert.setContent( Constants.adminUIMsg.alert3() );
								addAlert.show();
							}else if(result.startsWith("该目录")){
								addAlert.setContent(result);
								addAlert.show();
							}else if(result.endsWith("insert failed")){
								addAlert.setContent( Constants.adminUIMsg.alert4() );
								addAlert.show();
							}else{
								addAlert.setContent( Constants.adminUIMsg.alert5() );
								addAlert.show();
							}
						}	
					});
				}
			}

		});

		dataAddPanel.getFatherBox().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				dataCatePanel.show();

				Tree tree = dataCatePanel.getCategoryTree();
				tree.addSelectionHandler(new SelectionHandler<TreeItem>(){
					@Override
					public void onSelection(SelectionEvent<TreeItem> event) {
						// TODO Auto-generated method stub
						Boolean state = event.getSelectedItem().getState();
						if(!state){
							dataCatePanel.hide();
							String cate_str = event.getSelectedItem().getText();
							if(cate_str.equals("共享数据") || cate_str.toLowerCase().equals("shared data") 
									|| cate_str.startsWith("我的数据") || cate_str.toLowerCase().startsWith("my data") 
									|| cate_str.equals("系统数据") || cate_str.toLowerCase().equals("system data") ){
								dataAddPanel.getFatherBox().setValue(cate_str);
							}else{
								String sysPath = dataCatePanel.getSysTreeCateMap().get(event.getSelectedItem());
								String shrPath = dataCatePanel.getShrTreeCateMap().get(event.getSelectedItem());
								String myPath = dataCatePanel.getMyTreeCateMap().get(event.getSelectedItem());
								if(sysPath != null){
									dataAddPanel.getFatherBox().setValue(sysPath);
								}else if(shrPath != null){
									dataAddPanel.getFatherBox().setValue(shrPath);
								}else if(myPath != null){
									dataAddPanel.getFatherBox().setValue(myPath);
								}else
									dataAddPanel.getFatherBox().setValue(null);
							}
						}
					}
				});
			}

		});

		dataAddPanel.getCancelBtn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				addAlert.hide();
				dataCatePanel.hide();
				progCatePanel.hide();
				progAddPanel.hide();
				dataAddPanel.hide();
				dataAddPanel.getNameBox().setValue("");
				dataAddPanel.getFatherBox().setValue("");
			}

		});

		dataAddPanel.getClose().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				addAlert.hide();
				dataCatePanel.hide();
				progCatePanel.hide();
				progAddPanel.hide();
				dataAddPanel.hide();
				dataAddPanel.getNameBox().setValue("");
				dataAddPanel.getFatherBox().setValue("");
			}

		});

		dataAddPanel.getConfirmBtn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String name = dataAddPanel.getNameBox().getValue();
				String fathPath = dataAddPanel.getFatherBox().getValue();

				String path = fathPath + ">" + name;
				if(name == "null" || "".equals(name)){
					dataAddPanel.getNameErrorLabel().setText( Constants.adminUIMsg.alert1() );
					dataAddPanel.getNameErrorLabel().setVisible(true);
					dataAddPanel.getFathErrorLabel().setVisible(false);
				}else if(fathPath == "null" || "".equals(fathPath)){
					dataAddPanel.getFathErrorLabel().setText( Constants.adminUIMsg.alert2() );
					dataAddPanel.getNameErrorLabel().setVisible(false);
					dataAddPanel.getFathErrorLabel().setVisible(true);
				}else{
					Category category = new Category();
					final String id = UUID.randomUUID();
					category.setId(id);
					category.setName(name);
					category.setType("data");
					category.setPath(path);
					category.setHaschild(false);
					category.setCreatetime(TimeUtils.timeNow());
					categoryService.insertCategory(category, new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							addAlert.setContent(caught.getMessage());
							addAlert.show();
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							if(result.equals("success")){
								addAlert.setContent( Constants.adminUIMsg.alert3() );
								addAlert.show();
							}else if(result.startsWith("该目录")){
								addAlert.setContent(result);
								addAlert.show();
							}else if(result.endsWith("insert failed")){
								addAlert.setContent( Constants.adminUIMsg.alert4() );
								addAlert.show();
							}else{
								addAlert.setContent( Constants.adminUIMsg.alert5() );
								addAlert.show();
							}
						}	
					});
				}
			}

		});

		adminView.getCateSearch().addDomHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode()== 13){
					adminView.getCateSearchBtn().click();
				}
			}
		}, KeyUpEvent.getType());

		adminView.getCateSearchBtn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Clear last search account object information before a new search
				searchCategory = new Category();
				searchFlag = true;

				//Get search information and save to searchAccount object
				searchName = adminView.getCateName().getValue();
				searchPath = adminView.getCatePath().getValue();

				if(searchName != ""){
					searchCategory.setName(searchName);
				}else{
					searchCategory.setName(null);
				}
				if(searchPath != ""){
					searchCategory.setPath(searchPath);
				}else{
					searchCategory.setPath(null);
				}

				//If all the input is empty, prompt user enter input information. If is not , then begin searching
				if(searchName == "" && searchPath == ""){
					searchAlert.setContent( Constants.adminUIMsg.searchNoInput() );
					searchAlert.show();
				}else{
					searchResult();
				}
			}

		});


		page.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				int index = page.getCellForEvent(event).getCellIndex();
				String crtPageText = page.getText(0, index);
				currentPage = pagination.getCurrentPage(currentPage, crtPageText, lastPage);
				pagination.reload(currentPage);
				if(searchFlag){				//Show search results
					reload();
				}else						//Show all results
					load();
			}

		});
	}

	/**
	 * Initialization
	 */
	private void init(){
		progCatePanel.getChange().setVisible(false);
		progAddPanel.setTitle( Constants.adminUIMsg.addCate() );	
		progCatePanel.setTitle( Constants.adminUIMsg.chooseCate() );
		dataCatePanel.getChange().setVisible(false);
		dataAddPanel.setTitle( Constants.adminUIMsg.addCate() ); 
		dataCatePanel.setTitle( Constants.adminUIMsg.chooseCate() );
		currentPage = 1;

		clearInput();													
		adminView.getCatePage().clear();								
		page.addStyleName("admin-page");
		adminView.getCatePage().add(page);

		categoryService.getSize(new AsyncCallback<Integer>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				caught.getMessage();
			}

			@Override
			public void onSuccess(Integer result) {
				// TODO Auto-generated method stub
				//Caculate page-related information, initialize and load paging controls
				resultSize = result;
				pageSize = (int)Math.ceil((double)result/13);
				lastPage = pageSize;
				pagination = new Pagination(page, pageSize, Pagination.PageType.LARGE);
				pagination.load();
				//Load user data
				load();
			}

		});
	}

	/**
	 * Load data into the page
	 */
	private void load() {
		resultStart = (currentPage-1)*13;
		if(currentPage == lastPage){
			everyPageSize = resultSize - resultStart;
		}else{
			everyPageSize = 13;
		}

		adminView.getCateGrid().resize(everyPageSize+1, 4);
		adminView.getCateGrid().setText(0, 0, "Name");
		adminView.getCateGrid().setText(0, 1, "Path");
		adminView.getCateGrid().setText(0, 2, "Create Time");
		adminView.getCateGrid().setText(0, 3, "Operation");

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
	 * Load search result and reset paging control 
	 */	
	private void searchResult(){
		adminView.getCatePage().clear();							//Clear paging area and reload
		adminView.getCatePage().add(page);
		categoryService.search(searchCategory, 0, 0, new AsyncCallback<List<Category>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				searchAlert.setContent(caught.getMessage());
				searchAlert.show();
			}

			@Override
			public void onSuccess(List<Category> result) {
				// TODO Auto-generated method stub
				if(result.size() == 0){	
					String newPath = changeCategory(searchCategory.getPath());
					if(newPath != null){
						searchCategory.setPath(newPath);
						categoryService.search(searchCategory, 0, 0, new AsyncCallback<List<Category>>(){

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								searchAlert.setContent(caught.getMessage());
								searchAlert.show();
							}

							@Override
							public void onSuccess(List<Category> result) {
								// TODO Auto-generated method stub
								if(result.size() == 0){					
									searchAlert.setContent( Constants.adminUIMsg.searchNoResult() );
									searchAlert.show();
								}else{									
									resultSize = result.size();			
									pageSize = (int)Math.ceil((double)result.size()/13);
									lastPage = pageSize;
									currentPage = 1;
									pagination = new Pagination(page, pageSize, Pagination.PageType.LARGE);
									pagination.load();
									reload();						
								}

							}

						});
					}else{											
						searchAlert.setContent( Constants.adminUIMsg.searchNoResult() );
						searchAlert.show();
					}
				}else{												
					resultSize = result.size();						
					pageSize = (int)Math.ceil((double)result.size()/13);
					lastPage = pageSize;
					currentPage = 1;
					pagination = new Pagination(page, pageSize, Pagination.PageType.LARGE);
					pagination.load();
					reload();										
				}
			}

		});
	}

	/**
	 * Reload search result to the page
	 */
	private void reload(){
		resultStart = (currentPage-1)*13;
		if(currentPage == lastPage){
			everyPageSize = resultSize - resultStart;
		}else{
			everyPageSize = 13;
		}

		adminView.getCateGrid().resize(everyPageSize+1, 4);
		adminView.getCateGrid().setText(0, 0, "Name");
		adminView.getCateGrid().setText(0, 1, "Path");
		adminView.getCateGrid().setText(0, 2, "Create Time");
		adminView.getCateGrid().setText(0, 3, "Operation");

		categoryService.search(searchCategory, resultStart, everyPageSize, new AsyncCallback<List<Category>>(){

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
	 * Directory path in English conversion
	 * My program or data、 system program or data、 shared program or data is saved in database in English or Chinese
	 * When search some information, it need to search by both English and Chinese to get the accurate results 
	 * 
	 * @return newPath Converted directory path
	 */
	private String changeCategory(String catePath){
		String newPath = null;
		if(catePath != null){
			String arr[] = catePath.split(">");
			String tmp = arr[0];
			if(tmp.equals("我的程序")){
				newPath = "My Program";
				for(int i=1;i<arr.length;i++){
					newPath = newPath+arr[i];
				}
			}else if(tmp.equals("系统程序")){
				newPath = "System Program";
				for(int i=1;i<arr.length;i++){
					newPath = newPath+arr[i];
				}
			}else if(tmp.equals("共享程序")){
				newPath = "Shared Program";
				for(int i=1;i<arr.length;i++){
					newPath = newPath+arr[i];
				}
			}else if(tmp.toLowerCase().equals("my program")){
				newPath = "我的程序";
				for(int i=1;i<arr.length;i++){
					newPath = newPath+arr[i];
				}
			}else if(tmp.toLowerCase().equals("system program")){
				newPath = "系统程序";
				for(int i=1;i<arr.length;i++){
					newPath = newPath+arr[i];
				}
			}else if(tmp.toLowerCase().equals("shared program")){
				newPath = "共享程序";
				for(int i=1;i<arr.length;i++){
					newPath = newPath+arr[i];
				}
			}else if(tmp.equals("我的数据")){
				newPath = "My Data";
				for(int i=1;i<arr.length;i++){
					newPath = newPath+arr[i];
				}
			}else if(tmp.equals("系统数据")){
				newPath = "System Data";
				for(int i=1;i<arr.length;i++){
					newPath = newPath+arr[i];
				}
			}else if(tmp.equals("共享数据")){
				newPath = "Shared Data";
				for(int i=1;i<arr.length;i++){
					newPath = newPath+arr[i];
				}
			}else if(tmp.toLowerCase().equals("my data")){
				newPath = "我的数据";
				for(int i=1;i<arr.length;i++){
					newPath = newPath+arr[i];
				}
			}else if(tmp.toLowerCase().equals("system data")){
				newPath = "系统数据";
				for(int i=1;i<arr.length;i++){
					newPath = newPath+arr[i];
				}
			}else if(tmp.toLowerCase().equals("shared data")){
				newPath = "共享数据";
				for(int i=1;i<arr.length;i++){
					newPath = newPath+arr[i];
				}
			}
		}else
			newPath = null;
		return newPath;
	}

	/**
	 * Clear input area
	 */
	private void clearInput(){
		adminView.getCateName().setValue(null);
		adminView.getCatePath().setValue(null);
	}

	public boolean isSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(boolean searchFlag) {
		this.searchFlag = searchFlag;
	}
}
