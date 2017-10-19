/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.view;

import eml.studio.client.ui.panel.CategoryPanel;
import eml.studio.client.util.Constants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class AdminView extends Composite {

	interface AdminViewUiBinder extends UiBinder<Widget, AdminView> {
	}

	private static AdminViewUiBinder ourUiBinder = GWT.create(AdminViewUiBinder.class);

	@UiField
	Anchor userProg;

	@UiField
	Anchor userData;

	@UiField
	Anchor userList;

	@UiField
	Anchor category;

	@UiField
	Anchor workStage;

	@UiField
	Anchor userAnchor;

	@UiField
	Anchor logout;

	@UiField
	HTMLPanel progContent;

	@UiField
	HorizontalPanel progSearch;

	@UiField  
	Grid progGrid; 

	@UiField
	HTMLPanel progPage;

	@UiField
	HTMLPanel dataContent;

	@UiField
	HorizontalPanel dataSearch;

	@UiField  
	Grid dataGrid;

	@UiField  
	HTMLPanel dataPage;

	@UiField
	HTMLPanel userContent;

	@UiField
	HorizontalPanel userSearch;

	@UiField  
	Grid userGrid;

	@UiField  
	HTMLPanel userPage;

	@UiField
	HTMLPanel cateContent;

	@UiField
	HorizontalPanel cateSearch;

	@UiField  
	Grid cateGrid;

	@UiField  
	HTMLPanel catePage;

	@UiField
	HTMLPanel content;

	//Program Search Panel
	final CategoryPanel progCatePanel = new CategoryPanel("prog");
	final Label progNameLabel = new Label("Name");
	final Label progCateLabel = new Label("Category");
	final Label progStartDateLabel = new Label("StartDate");
	final Label progEndDateLabel = new Label("EndDate");
	final Label progOwnerLabel = new Label("Owner");
	final TextBox progName = new TextBox();
	final TextBox progCategory = new TextBox();
	final DateBox progStartDate = new DateBox();
	final DateBox progEndDate = new DateBox();
	final TextBox progOwner = new TextBox();
	final Button progSearchBtn = new Button("Search");
	//Dataset Search Panel
	final CategoryPanel dataCatePanel = new CategoryPanel("data");
	final Label dataNameLabel = new Label("Name");
	final Label dataCateLabel = new Label("Category");
	final Label dataStartDateLabel = new Label("StartDate");
	final Label dataEndDateLabel = new Label("EndDate");
	final Label dataOwnerLabel = new Label("Owner");
	final TextBox dataName = new TextBox();
	final TextBox dataCategory = new TextBox();
	final DateBox dataStartDate = new DateBox();
	final DateBox dataEndDate = new DateBox();
	final TextBox dataOwner = new TextBox();
	final Button dataSearchBtn = new Button("Search");
	//User Search Panel
	final Label userEmailLabel = new Label("Email");
	final Label userNameLabel = new Label("UserName");
	final Label userCmpLabel = new Label("Company");
	final Label userPstLabel = new Label("Position");
	final Label userPowerLabel = new Label("Power");
	final TextBox userEmail = new TextBox();
	final TextBox userName = new TextBox();
	final TextBox userCmp = new TextBox();
	final TextBox userPst = new TextBox();
	final ListBox userPower = new ListBox();
	final Button userSearchBtn = new Button("Search");
	//Category Search Panel
	final CategoryPanel progPanel = new CategoryPanel("prog");
	final CategoryPanel dataPanel = new CategoryPanel("data");
	final Label cateNameLabel = new Label("Name");
	final Label catePathLabel = new Label("Path");
	final TextBox cateName = new TextBox();
	final TextBox catePath = new TextBox();
	final Button cateSearchBtn = new Button("Search");
	final Label cateProgAdd = new Label( Constants.adminUIMsg.progAdd() );
	final Label cateDataAdd = new Label( Constants.adminUIMsg.dataAdd() );

	public AdminView() {
		initWidget(ourUiBinder.createAndBindUi(this));

		userProg.addStyleName("current");
		userProg.setText( Constants.headerUIMsg.progManagement() );
		userData.setText( Constants.headerUIMsg.dataManagement() );
		userList.setText( Constants.headerUIMsg.userManagement() );
		category.setText( Constants.headerUIMsg.cateManagement() );
		workStage.setText( Constants.headerUIMsg.workStage() );
		logout.setText( Constants.headerUIMsg.logout() );

		progContent.setVisible(true);
		dataContent.setVisible(false);
		userContent.setVisible(false);
		cateContent.setVisible(false);

		initProgSearch();
		initDataSearch();
		initUserSearch();
		initCateSearch();
	}

	public void setAccount(String usernameAccount, String emailAccount){
		if( usernameAccount == "" || usernameAccount == null){
			userAnchor.setText(emailAccount);
		}else{
			userAnchor.setText(usernameAccount);
		}
	}

	private void initProgSearch(){
		progCatePanel.getChange().setVisible(false);					//在程序搜索的category弹出框中不需要change按钮
		DateTimeFormat pickerFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
		progStartDate.setFormat(new DateBox.DefaultFormat(pickerFormat));
		progStartDate.addStyleName("run-history-datebox");
		progStartDate.getDatePicker().addStyleName("run-history-datepicker-popup");
		progEndDate.setFormat(new DateBox.DefaultFormat(pickerFormat));
		progEndDate.addStyleName("run-history-datebox");
		progEndDate.getDatePicker().addStyleName("run-history-datepicker-popup");
		progSearchBtn.removeStyleName("gwt-Button");
		progSearchBtn.addStyleName("searchBtn");

		progSearch.add(progNameLabel);
		progSearch.add(progName);
		progSearch.add(progCateLabel);
		progSearch.add(progCategory);
		progSearch.add(progStartDateLabel); 
		progSearch.add(progStartDate);
		progSearch.add(progEndDateLabel);
		progSearch.add(progEndDate);
		progSearch.add(progOwnerLabel);
		progSearch.add(progOwner);
		progSearch.add(progSearchBtn);

		progCategory.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
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
								progCategory.setValue(cate_str);
							}else{
								String sysPath = progCatePanel.getSysTreeCateMap().get(event.getSelectedItem());
								String shrPath = progCatePanel.getShrTreeCateMap().get(event.getSelectedItem());
								String myPath = progCatePanel.getMyTreeCateMap().get(event.getSelectedItem());
								if(sysPath != null){
									progCategory.setValue(sysPath);
								}else if(shrPath != null){
									progCategory.setValue(shrPath);
								}else if(myPath != null){
									progCategory.setValue(myPath);
								}else
									progCategory.setValue(null);
							}
						}
					}
				});
			}
		});
	}

	private void initDataSearch(){
		dataCatePanel.getChange().setVisible(false);					//在数据搜索的category弹出框中不需要change按钮
		DateTimeFormat pickerFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
		dataStartDate.setFormat(new DateBox.DefaultFormat(pickerFormat));
		dataStartDate.addStyleName("run-history-datebox");
		dataStartDate.getDatePicker().addStyleName("run-history-datepicker-popup");
		dataEndDate.setFormat(new DateBox.DefaultFormat(pickerFormat));
		dataEndDate.addStyleName("run-history-datebox");
		dataEndDate.getDatePicker().addStyleName("run-history-datepicker-popup");
		dataSearchBtn.removeStyleName("gwt-Button");
		dataSearchBtn.addStyleName("searchBtn");

		dataSearch.add(dataNameLabel);
		dataSearch.add(dataName);
		dataSearch.add(dataCateLabel);
		dataSearch.add(dataCategory);
		dataSearch.add(dataStartDateLabel); 
		dataSearch.add(dataStartDate);
		dataSearch.add(dataEndDateLabel);
		dataSearch.add(dataEndDate);
		dataSearch.add(dataOwnerLabel);
		dataSearch.add(dataOwner);
		dataSearch.add(dataSearchBtn);

		dataCategory.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
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
								dataCategory.setValue(cate_str);
							}else{
								String sysPath = dataCatePanel.getSysTreeCateMap().get(event.getSelectedItem());
								String shrPath = dataCatePanel.getShrTreeCateMap().get(event.getSelectedItem());
								String myPath = dataCatePanel.getMyTreeCateMap().get(event.getSelectedItem());
								if(sysPath != null){
									dataCategory.setValue(sysPath);
								}else if(shrPath != null){
									dataCategory.setValue(shrPath);
								}else if(myPath != null){
									dataCategory.setValue(myPath);
								}else
									dataCategory.setValue(null);
							}
						}
					}
				});
			}
		});
	}

	private void initUserSearch(){
		userSearchBtn.removeStyleName("gwt-Button");
		userSearchBtn.addStyleName("searchBtn");
		userPower.addItem("");
		userPower.addItem("Administration");
		userPower.addItem("Job edit");
		userPower.addItem("Upload prog/data");
		userPower.setSelectedIndex(0);
		userSearch.add(userEmailLabel);
		userSearch.add(userEmail);
		userSearch.add(userNameLabel);
		userSearch.add(userName);
		userSearch.add(userCmpLabel); 
		userSearch.add(userCmp);
		userSearch.add(userPstLabel);
		userSearch.add(userPst);
		userSearch.add(userPowerLabel);
		userSearch.add(userPower);
		userSearch.add(userSearchBtn);
	}

	private void initCateSearch(){
		HorizontalPanel add = new HorizontalPanel();
		cateProgAdd.setTitle("add new category for program");
		cateProgAdd.removeStyleName("gwt-Label");
		cateProgAdd.addStyleName("admin-cate-add");
		cateDataAdd.setTitle("add new category for dataset");
		cateDataAdd.removeStyleName("gwt-Label");
		cateDataAdd.addStyleName("admin-cate-add");
		add.add(cateProgAdd);
		add.add(cateDataAdd);
		add.addStyleName("admin-cate-add-style");
		cateSearchBtn.removeStyleName("gwt-Button");
		cateSearchBtn.addStyleName("searchBtn2");

		cateSearch.add(cateNameLabel);
		cateSearch.add(cateName);
		cateSearch.add(catePathLabel);
		cateSearch.add(catePath);
		cateSearch.add(cateSearchBtn);
		cateSearch.add(add);

		//默认显示程序目录选择面板
		catePath.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				progPanel.show();

				Tree tree = progPanel.getCategoryTree();
				tree.addSelectionHandler(new SelectionHandler<TreeItem>(){
					@Override
					public void onSelection(SelectionEvent<TreeItem> event) {
						// TODO Auto-generated method stub
						Boolean state = event.getSelectedItem().getState();
						if(!state){
							progPanel.hide();
							String cate_str = event.getSelectedItem().getText();
							if(cate_str.equals("共享程序") || cate_str.toLowerCase().equals("shared program") 
									|| cate_str.startsWith("我的程序") || cate_str.toLowerCase().startsWith("my program") 
									|| cate_str.equals("系统程序") || cate_str.toLowerCase().equals("system program") ){
								catePath.setValue(cate_str);
							}else{
								String sysPath = progPanel.getSysTreeCateMap().get(event.getSelectedItem());
								String shrPath = progPanel.getShrTreeCateMap().get(event.getSelectedItem());
								String myPath = progPanel.getMyTreeCateMap().get(event.getSelectedItem());
								if(sysPath != null){
									catePath.setValue(sysPath);
								}else if(shrPath != null){
									catePath.setValue(shrPath);
								}else if(myPath != null){
									catePath.setValue(myPath);
								}else
									catePath.setValue(null);
							}
						}
					}
				});
			}

		});

		progPanel.getChange().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				dataPanel.show();
				progPanel.hide();

				Tree tree = dataPanel.getCategoryTree();
				tree.addSelectionHandler(new SelectionHandler<TreeItem>(){
					@Override
					public void onSelection(SelectionEvent<TreeItem> event) {
						// TODO Auto-generated method stub
						Boolean state = event.getSelectedItem().getState();
						if(!state){
							dataPanel.hide();
							String cate_str = event.getSelectedItem().getText();
							if(cate_str.equals("共享数据") || cate_str.toLowerCase().equals("shared data") 
									|| cate_str.startsWith("我的数据") || cate_str.toLowerCase().startsWith("my data") 
									|| cate_str.equals("系统数据") || cate_str.toLowerCase().equals("system data") ){
								catePath.setValue(cate_str);
							}else{
								String sysPath = dataPanel.getSysTreeCateMap().get(event.getSelectedItem());
								String shrPath = dataPanel.getShrTreeCateMap().get(event.getSelectedItem());
								String myPath = dataPanel.getMyTreeCateMap().get(event.getSelectedItem());
								if(sysPath != null){
									catePath.setValue(sysPath);
								}else if(shrPath != null){
									catePath.setValue(shrPath);
								}else if(myPath != null){
									catePath.setValue(myPath);
								}else
									catePath.setValue(null);
							}
						}
					}
				});
			}

		});

		dataPanel.getChange().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				progPanel.show();
				dataPanel.hide();

				Tree tree = progPanel.getCategoryTree();
				tree.addSelectionHandler(new SelectionHandler<TreeItem>(){
					@Override
					public void onSelection(SelectionEvent<TreeItem> event) {
						// TODO Auto-generated method stub
						Boolean state = event.getSelectedItem().getState();
						if(!state){
							progPanel.hide();
							String cate_str = event.getSelectedItem().getText();
							if(cate_str.equals("共享程序") || cate_str.toLowerCase().equals("shared program") 
									|| cate_str.startsWith("我的程序") || cate_str.toLowerCase().startsWith("my program") 
									|| cate_str.equals("系统程序") || cate_str.toLowerCase().equals("system program") ){
								catePath.setValue(cate_str);
							}else{
								String sysPath = progPanel.getSysTreeCateMap().get(event.getSelectedItem());
								String shrPath = progPanel.getShrTreeCateMap().get(event.getSelectedItem());
								String myPath = progPanel.getMyTreeCateMap().get(event.getSelectedItem());
								if(sysPath != null){
									catePath.setValue(sysPath);
								}else if(shrPath != null){
									catePath.setValue(shrPath);
								}else if(myPath != null){
									catePath.setValue(myPath);
								}else
									catePath.setValue(null);
							}
						}
					}
				});
			}

		});
	}

	public Anchor getUserProg(){
		return userProg;
	} 
	public Anchor getUserData(){
		return userData;
	}  
	public Anchor getUserList(){
		return userList;
	}
	public Anchor getCategory(){
		return category;
	}
	public Anchor getWorkStage() {
		return workStage;
	}
	public Anchor getUserAnchor(){
		return userAnchor;
	}
	public Anchor getLogout() {
		return logout;
	}

	public HTMLPanel getProgContent(){
		return progContent;
	}
	public HorizontalPanel getProgSearch(){
		return progSearch;
	}
	public TextBox getProgName(){
		return progName;
	}
	public TextBox getProgCate(){
		return progCategory;
	}
	public DateBox getProgStartDate(){
		return progStartDate;
	}
	public DateBox getProgEndDate(){
		return progEndDate;
	}
	public TextBox getProgOwner(){
		return progOwner;
	}
	public Button getProgSearchBtn(){
		return progSearchBtn;
	}
	public Grid getProgGrid(){
		return progGrid;
	}
	public HTMLPanel getProgPage(){
		return progPage;
	}

	public HTMLPanel getDataContent(){
		return dataContent;
	}
	public HorizontalPanel getDataSearch(){
		return dataSearch;
	}
	public TextBox getDataName(){
		return dataName;
	}
	public TextBox getDataCate(){
		return dataCategory;
	}
	public DateBox getDataStartDate(){
		return dataStartDate;
	}
	public DateBox getDataEndDate(){
		return dataEndDate;
	}
	public TextBox getDataOwner(){
		return dataOwner;
	}
	public Button getDataSearchBtn(){
		return dataSearchBtn;
	}
	public Grid getDataGrid(){
		return dataGrid;
	}
	public HTMLPanel getDataPage(){
		return dataPage;
	}

	public HTMLPanel getUserContent(){
		return userContent;
	}
	public HorizontalPanel getUserSearch(){
		return userSearch;
	}
	public TextBox getUserEmail(){
		return userEmail;
	}
	public TextBox getUserName(){
		return userName;
	}
	public TextBox getUserCmp(){
		return userCmp;
	}
	public TextBox getUserPst(){
		return userPst;
	}
	public ListBox getUserPower(){
		return userPower;
	}
	public Button getUserSearchBtn(){
		return userSearchBtn;
	}
	public Grid getUserGrid(){
		return userGrid;
	}
	public HTMLPanel getUserPage(){
		return userPage;
	}

	public HTMLPanel getCateContent(){
		return cateContent;
	}
	public HorizontalPanel getCateSearch(){
		return cateSearch;
	}
	public TextBox getCateName(){
		return cateName;
	}
	public TextBox getCatePath(){
		return catePath;
	}
	public Button getCateSearchBtn(){
		return cateSearchBtn;
	}
	public Label getProgAdd(){
		return cateProgAdd;
	}
	public Label getDataAdd(){
		return cateDataAdd;
	}
	public Grid getCateGrid(){
		return cateGrid;
	}
	public HTMLPanel getCatePage(){
		return catePage;
	}

	public HTMLPanel getAdminPanel(){
		return content;
	}

}