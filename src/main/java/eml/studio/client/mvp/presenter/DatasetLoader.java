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
import eml.studio.client.util.Pagination;
import eml.studio.shared.model.Category;
import eml.studio.shared.model.Dataset;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 *  Load dataset management data
 */
public class DatasetLoader {
	private final AdminView adminView;
	private DatasetServiceAsync datasetService = GWT.create(DatasetService.class);
	private CategoryServiceAsync categoryService = GWT.create(CategoryService.class);
	private DeletePanel deletePanel = new DeletePanel();				
	private AlertPanel alertPanel = new AlertPanel();					
	private AlertPanel searchAlert = new AlertPanel();				
	private final DateTimeFormat formatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	private int currentPage = 1;											
	private int pageSize;
	private int lastPage;
	private int resultSize;
	private int resultStart;
	private int everyPageSize;
	private Grid page = new Grid();										
	private Pagination pagination;										
	private Dataset searchDataset;										
	private String searchName;
	private String searchPath;
	private String searchStart = null;
	private String searchEnd = null;
	private String searchOwner;
	private boolean searchFlag = false;	


	public DatasetLoader(AdminView adminView) {
		this.adminView = adminView;
		bind();
	}

	public void go(){
		init();
	}

	/**
	 * Event binding
	 */
	private void bind(){
		
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

		adminView.getDataSearch().addDomHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode()== 13){
					adminView.getDataSearchBtn().click();
				}
			}
		}, KeyUpEvent.getType());

		adminView.getDataSearchBtn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				searchDataset = new Dataset();
				searchFlag = true;

				searchName = adminView.getDataName().getValue();
				searchOwner = adminView.getDataOwner().getValue();
				searchPath = adminView.getDataCate().getValue();

				if(searchName != ""){
					searchDataset.setName(searchName);
				}else{
					searchDataset.setName(null);
				}
				if(searchOwner != ""){
					searchDataset.setOwner(searchOwner);
				}else{
					searchDataset.setOwner(null);
				}

				if( adminView.getDataStartDate().getValue() != null ){
					searchStart = formatter.format( adminView.getDataStartDate().getValue() );
				}else{
					searchStart = null;
				}
				if( adminView.getDataEndDate().getValue() != null ){
					searchEnd = formatter.format( adminView.getDataEndDate().getValue() );
				}else{
					searchEnd = null;
				}

				if(searchName == "" && searchPath == "" && searchOwner == "" && searchStart == null && searchEnd == null){
					searchAlert.setContent( Constants.adminUIMsg.searchNoInput() );
					searchAlert.show();
				}else{
					if(searchPath == ""){
						searchResult();
					}else{
						Category category = new Category();
						category.setPath(searchPath);
						categoryService.getCategory(category, "", new AsyncCallback<List<Category>>(){
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
									Category tc = new Category();
									tc.setPath(changeCategory(searchPath));
									categoryService.getCategory(tc, "", new AsyncCallback<List<Category>>(){

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											searchAlert.setContent(caught.getMessage());
											searchAlert.show();
										}

										@Override
										public void onSuccess(List<Category> result) {
											if(result.size() == 0){				
												searchAlert.setContent( Constants.adminUIMsg.searchCateError() );
												searchAlert.show();
											}else{								
												searchDataset.setCategory( result.get(0).getId() );
												searchResult();
											}
										}
									});
								}else{							
									searchDataset.setCategory( result.get(0).getId() );
									searchResult();
								}
							}
						});
					}
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
				if(searchFlag){				
					reload();
				}else						
					load();
			}

		});
	}

	/**
	 * Initialization
	 */
	private void init(){	
		clearInput();												
		adminView.getDataPage().clear();							
		page.addStyleName("admin-page");
		adminView.getDataPage().add(page);
		currentPage = 1;

		datasetService.getSize(new AsyncCallback<Integer>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				caught.getMessage();
			}

			@Override
			public void onSuccess(Integer result) {
				// TODO Auto-generated method stub
				resultSize = result;
				pageSize = (int)Math.ceil((double)result/13);
				lastPage = pageSize;
				pagination = new Pagination(page, pageSize, Pagination.PageType.LARGE);
				pagination.load();
				
				load();
			}
		});
	}

	/**
	 * Loading data to the page
	 */
	private void load() {
		resultStart = (currentPage-1)*13;
		if(currentPage == lastPage){
			everyPageSize = resultSize - resultStart;
		}else{
			everyPageSize = 13;
		}

		adminView.getDataGrid().resize(everyPageSize+1, 7);
		adminView.getDataGrid().setText(0, 0, "Name");
		adminView.getDataGrid().setText(0, 1, "Category");
		adminView.getDataGrid().setText(0, 2, "Description");
		adminView.getDataGrid().setText(0, 3, "Version");
		adminView.getDataGrid().setText(0, 4, "CreateDate");
		adminView.getDataGrid().setText(0, 5, "Owner");
		adminView.getDataGrid().setText(0, 6, "Operation");

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
						categoryService.getCategory(categoryId, new AsyncCallback<Category>(){

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
	 * Load search result and reset paging control 
	 */	
	private void searchResult(){
		adminView.getDataPage().clear();							
		adminView.getDataPage().add(page);
		datasetService.search(searchDataset, searchStart, searchEnd, 0, 0, new AsyncCallback<List<Dataset>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				searchAlert.setContent(caught.getMessage());
				searchAlert.show();
			}

			@Override
			public void onSuccess(List<Dataset> result) {
				// TODO Auto-generated method stub
				if(result.size() == 0){
					searchAlert.setContent( Constants.adminUIMsg.searchNoResult());
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
	}

	/**
	 * Reload search result to the page
	 */
	private void reload(){
		resultStart = (currentPage-1)*13;
		if(currentPage == lastPage){					//Every page show 13 records, if the last page  has less than 13 records, it show real records
			everyPageSize = resultSize - resultStart;
		}else{
			everyPageSize = 13;
		}

		adminView.getDataGrid().resize(everyPageSize+1, 7);
		adminView.getDataGrid().setText(0, 0, "Name");
		adminView.getDataGrid().setText(0, 1, "Category");
		adminView.getDataGrid().setText(0, 2, "Description");
		adminView.getDataGrid().setText(0, 3, "Version");
		adminView.getDataGrid().setText(0, 4, "CreateDate");
		adminView.getDataGrid().setText(0, 5, "Owner");
		adminView.getDataGrid().setText(0, 6, "Operation");

		datasetService.search(searchDataset,searchStart,searchEnd,resultStart, everyPageSize, new AsyncCallback<List<Dataset>>(){

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
						categoryService.getCategory(categoryId, new AsyncCallback<Category>(){

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
	 * 目录路径中英文转换
	 * 我的数据、系统数据、共享数据在库表中是中英文混合保存的
	 * 在查询相关信息时需要查询两遍（中英各一遍）才能获取到准确的结果
	 * @return newPath 转换后的目录路径
	 */
	private String changeCategory(String catePath){
		String arr[] = catePath.split(">");
		String tmp = arr[0];
		String newPath = null;
		if(tmp.equals("我的数据")){
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
		return newPath;
	}

	/**
	 * 清空输入框
	 */
	private void clearInput(){
		adminView.getDataName().setValue(null);
		adminView.getDataCate().setValue(null);
		adminView.getDataStartDate().setValue(null);
		adminView.getDataEndDate().setValue(null);
		adminView.getDataOwner().setValue(null);
	}

	public boolean isSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(boolean searchFlag) {
		this.searchFlag = searchFlag;
	}
}
