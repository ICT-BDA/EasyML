/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client.mvp.presenter;

import bda.studio.client.event.LogoutEvent;
import bda.studio.client.mvp.AppController;
import bda.studio.client.mvp.view.AdminView;
import bda.studio.client.rpc.AccountService;
import bda.studio.client.rpc.AccountServiceAsync;
import bda.studio.client.rpc.CategoryService;
import bda.studio.client.rpc.CategoryServiceAsync;
import bda.studio.client.rpc.DatasetService;
import bda.studio.client.rpc.DatasetServiceAsync;
import bda.studio.client.rpc.ProgramService;
import bda.studio.client.rpc.ProgramServiceAsync;
import bda.studio.client.ui.panel.AlertPanel;
import bda.studio.client.ui.panel.CateAddPanel;
import bda.studio.client.ui.panel.CategoryPanel;
import bda.studio.client.util.Constants;
import bda.studio.client.util.TimeUtils;
import bda.studio.client.util.UUID;
import bda.studio.shared.model.Account;
import bda.studio.shared.model.Category;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
/**
 * Backstage management
 * 
 */
public class AdminPresenter implements Presenter {
  protected ProgramServiceAsync programService = GWT.create(ProgramService.class);
  protected DatasetServiceAsync datasetService = GWT.create(DatasetService.class);
  protected AccountServiceAsync accountService = GWT.create(AccountService.class);
  protected CategoryServiceAsync categoryService = GWT.create(CategoryService.class);
  protected CategoryPanel dataCatePanel = new CategoryPanel("data");
  protected CategoryPanel progCatePanel = new CategoryPanel("prog");
  protected CateAddPanel dataAddPanel = new CateAddPanel();
  protected CateAddPanel progAddPanel = new CateAddPanel();
  protected AlertPanel addAlert = new AlertPanel();
  final DateTimeFormat formatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
  int progCurrentPage = 1;
  int dataCurrentPage = 1;
  int userCurrentPage = 1;
  int cateCurrentPage = 1;
  ProgramLoader programLoader;
  DatasetLoader datasetLoader;
  AccountLoader accountLoader;
  CategoryLoader categoryLoader;
	
  private final HandlerManager eventBus;
  private final AdminView adminView;

  public AdminPresenter(HandlerManager eventBus, AdminView adminView) {
	    this.eventBus = eventBus;
	    this.adminView = adminView;
  }
  
  /**
   * Init data
   */
  public void init(){
	adminView.setAccount(AppController.username, AppController.email);
	
	if(AppController.email.equals("admin")){
		adminView.getWorkStage().getElement().getStyle().setDisplay(Display.NONE);
		adminView.getWorkStage().getElement().removeAttribute("href");
	}
	
	program();
	dataset();
	account();
	category();
  }
  
  /**
   * Event binding
   */
  public void bind() {
	dataCatePanel.setTitle( Constants.adminUIMsg.chooseCate() );
	progCatePanel.setTitle( Constants.adminUIMsg.chooseCate() );
	progAddPanel.setTitle( Constants.adminUIMsg.addCate() );
	dataAddPanel.setTitle( Constants.adminUIMsg.addCate() ); 
	  
	//Program management
	adminView.getUserProg().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			adminView.getProgGrid().setVisible(true);
			adminView.getDataGrid().setVisible(false);
			adminView.getUserGrid().setVisible(false);
			adminView.getCatePanel().setVisible(false);
			adminView.getProgPage().setVisible(true);
			adminView.getDataPage().setVisible(false);
			adminView.getUserPage().setVisible(false);
			adminView.getCatePage().setVisible(false);
			adminView.getUserProg().addStyleName("current");
			adminView.getUserData().removeStyleName("current");
			adminView.getUserList().removeStyleName("current");
			adminView.getCategory().removeStyleName("current");
		}
			    	
	});

	//Data management
	adminView.getUserData().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			adminView.getProgGrid().setVisible(false);
			adminView.getDataGrid().setVisible(true);
			adminView.getUserGrid().setVisible(false);
			adminView.getCatePanel().setVisible(false);
			adminView.getProgPage().setVisible(false);
			adminView.getDataPage().setVisible(true);
			adminView.getUserPage().setVisible(false);
			adminView.getCatePage().setVisible(false);
			adminView.getUserProg().removeStyleName("current");
			adminView.getUserData().addStyleName("current");
			adminView.getUserList().removeStyleName("current");
			adminView.getCategory().removeStyleName("current");
		}
			    	
	});

	//User Management
	adminView.getUserList().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			adminView.getProgGrid().setVisible(false);
			adminView.getDataGrid().setVisible(false);
			adminView.getUserGrid().setVisible(true);
			adminView.getCatePanel().setVisible(false);
			adminView.getProgPage().setVisible(false);
			adminView.getDataPage().setVisible(false);
			adminView.getUserPage().setVisible(true);
			adminView.getCatePage().setVisible(false);
			adminView.getUserProg().removeStyleName("current");
			adminView.getUserData().removeStyleName("current");
			adminView.getUserList().addStyleName("current");
			adminView.getCategory().removeStyleName("current");
		}
			    	
	});
			
	//Catagory management
	adminView.getCategory().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			adminView.getProgGrid().setVisible(false);
			adminView.getDataGrid().setVisible(false);
			adminView.getUserGrid().setVisible(false);
			adminView.getCatePanel().setVisible(true);
			adminView.getProgPage().setVisible(false);
			adminView.getDataPage().setVisible(false);
			adminView.getUserPage().setVisible(false);
			adminView.getCatePage().setVisible(true);
			adminView.getUserProg().removeStyleName("current");
			adminView.getUserData().removeStyleName("current");
			adminView.getUserList().removeStyleName("current");
			adminView.getCategory().addStyleName("current");
		}
			    	
	});
	
	//work stage
	adminView.getWorkStage().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			History.newItem("monitor");
		}
			    	
	});
	
	//Personal information
	adminView.getUserAnchor().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			if(!AppController.email.equals("admin")){
				History.newItem("account");
			}
		}
			    	
	});
	
	adminView.getLogout().addClickHandler(new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			eventBus.fireEvent(new LogoutEvent());
		}

	});
	
	//Add directory - program
	adminView.getProgAdd().addClickHandler(new ClickHandler(){
					
		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			progAddPanel.show();
		}
	});
			
	//Add directory - data
	adminView.getDataAdd().addClickHandler(new ClickHandler(){
						
		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			dataAddPanel.show();
		}
	});
	
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
  }
  
  /**
	* Load program
	*/
  public void program(){
	programService.getSize(new AsyncCallback<Integer>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			caught.getMessage();
		}

		@Override
		public void onSuccess(Integer result) {
			// TODO Auto-generated method stub
			
			//Calculate page numbers, load paging and data
			final int resultSize = result;
			final int pageSize = (int)Math.ceil((double)result/13);
			final int lastPage = pageSize;
			programLoader = new ProgramLoader(adminView, progCurrentPage, lastPage, resultSize);
			programLoader.pageLoader(pageSize);
			programLoader.init();
			
			//Page click response, reload page and data/
			adminView.getProgPage().addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					int index = adminView.getProgPage().getCellForEvent(event).getCellIndex();
					String pageText = adminView.getProgPage().getText(0, index);
					progCurrentPage = countPage(progCurrentPage, pageText, lastPage);
					programLoader = new ProgramLoader(adminView, progCurrentPage, lastPage, resultSize);
					programLoader.pageReloader(pageSize);
					programLoader.init();
				}
				
			});
		}
	});
  }
  
  /**
	* Load dataset
	*/
  public void dataset(){
	datasetService.getSize(new AsyncCallback<Integer>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			caught.getMessage();
		}

		@Override
		public void onSuccess(Integer result) {
			// TODO Auto-generated method stub
			
			//Calculate page numbers, load paging and data
			final int resultSize = result;
			final int pageSize = (int)Math.ceil((double)result/13);
			final int lastPage = pageSize;
			datasetLoader = new DatasetLoader(adminView, dataCurrentPage, lastPage, resultSize);
			datasetLoader.pageLoader(pageSize);
			datasetLoader.init();
			
			//Page click response, reload page and data/
			adminView.getDataPage().addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					int index = adminView.getDataPage().getCellForEvent(event).getCellIndex();
					String pageText = adminView.getDataPage().getText(0, index);
					dataCurrentPage = countPage(dataCurrentPage, pageText, lastPage);
					datasetLoader = new DatasetLoader(adminView, dataCurrentPage, lastPage, resultSize);
					datasetLoader.pageReloader(pageSize);
					datasetLoader.init();
				}
					
			});
		}
	});
  }

  /**
	* Load account
	*/
  public void account(){
	final Account account = new Account();
	account.setEmail(AppController.email);
	accountService.getSize(account, new AsyncCallback<Integer>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			caught.getMessage();
		}

		@Override
		public void onSuccess(Integer result) {
			// TODO Auto-generated method stub
			
			//Calculate page numbers, load paging and data
			final int resultSize = result;
			final int pageSize = (int)Math.ceil((double)result/13);
			final int lastPage = pageSize;
			accountLoader = new AccountLoader(adminView, userCurrentPage, lastPage, resultSize,account);
			accountLoader.pageLoader(pageSize);
			accountLoader.init();
			
			//Page click response, reload page and data
			adminView.getUserPage().addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					int index = adminView.getUserPage().getCellForEvent(event).getCellIndex();
					String pageText = adminView.getUserPage().getText(0, index);
					userCurrentPage = countPage(userCurrentPage, pageText, lastPage);
					accountLoader = new AccountLoader(adminView, userCurrentPage, lastPage, resultSize,account);
					accountLoader.pageReloader(pageSize);
					accountLoader.init();
				}
					
			});
		}
	});
  }
  
  /**
	* Load category
	*/
  public void category(){
	  categoryService.getSize(new AsyncCallback<Integer>(){

		@Override
		public void onFailure(Throwable caught) {
			caught.getMessage();
		}

		@Override
		public void onSuccess(Integer result) {
			//Calculate page numbers, load paging and data
			final int resultSize = result;
			final int pageSize = (int)Math.ceil((double)result/13);
			final int lastPage = pageSize;
			categoryLoader = new CategoryLoader(adminView, cateCurrentPage, lastPage, resultSize);
			categoryLoader.pageLoader(pageSize);
			categoryLoader.init();
			
			//Page click response, reload page and data/
			adminView.getCatePage().addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					int index = adminView.getCatePage().getCellForEvent(event).getCellIndex();
					String pageText = adminView.getCatePage().getText(0, index);
					cateCurrentPage = countPage(cateCurrentPage, pageText, lastPage);
					categoryLoader = new CategoryLoader(adminView, cateCurrentPage, lastPage, resultSize);
					categoryLoader.pageReloader(pageSize);
					categoryLoader.init();
				}
					
			});
		}
		  
	  });
  }
  
  /**
	* Calculate the current page number
	* @param current Current page number
	* @param pagetext Clickpage number
	* @param lastpage Last page number
	* @return current New current page number
	*/
  private static int countPage(int current, String pagetext, int lastpage){
	  if(pagetext.equals( Constants.adminUIMsg.firstPage() )){
		  current = 1;
		}else if(pagetext.equals( Constants.adminUIMsg.prevPage() )){
			if(current > 1){
				current = current - 1;
			}else{
				current = 1;
			}
		}else if(pagetext.equals( Constants.adminUIMsg.nextPage() )){
			if(current < lastpage){
				current = current + 1;
			}else{
				current = lastpage;
			}
		}else if(pagetext.equals( Constants.adminUIMsg.lastPage() )){
			current = lastpage;
		}else{
			current = Integer.parseInt(pagetext);
		}
	  return current;
  }
  
  @Override
  public void go(HasWidgets container) {

    bind();
    container.clear();
    container.add(adminView.asWidget());
    init();
  }
}