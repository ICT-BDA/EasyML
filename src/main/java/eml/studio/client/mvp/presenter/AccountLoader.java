/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.presenter;

import java.util.List;
import eml.studio.client.mvp.AppController;
import eml.studio.client.mvp.view.AdminView;
import eml.studio.client.rpc.AccountService;
import eml.studio.client.rpc.AccountServiceAsync;
import eml.studio.client.ui.panel.AlertPanel;
import eml.studio.client.ui.panel.DeletePanel;
import eml.studio.client.util.Constants;
import eml.studio.shared.model.Account;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 *  Loaded user management page data
 * 
 */
public class AccountLoader {
  private final AdminView adminView;
  protected AccountServiceAsync accountService = GWT.create(AccountService.class);
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
  Account account;
  
  public AccountLoader(AdminView adminView, int currentPage, int lastPage, int resultSize, Account account) {
	this.adminView = adminView;
	this.currentPage = currentPage;
	this.lastPage = lastPage;
	this.resultSize = resultSize;
	this.account = account;
  }
  
  /**
	* Init data
	*/
  public void init() {
	//Prompt box and close button
	alertPanel.getClose().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			History.fireCurrentHistoryState();
		}
			
	});

	//Prompt box and confirmation button
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

	//Reset the table row number and set the header
	adminView.getUserGrid().resize(everyPageSize+1, 7);
	adminView.getUserGrid().setText(0, 0, "Email");
	adminView.getUserGrid().setText(0, 1, "Username");
	adminView.getUserGrid().setText(0, 2, "Company");
	adminView.getUserGrid().setText(0, 3, "Position");
	adminView.getUserGrid().setText(0, 4, "Join Date");
	adminView.getUserGrid().setText(0, 5, "Power");
	adminView.getUserGrid().setText(0, 6, "Operation");

	//Get the user information and fill in the form
	accountService.findValid(account, resultStart, everyPageSize, new AsyncCallback<List<Account>>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			alertPanel.setContent(caught.getMessage());
			alertPanel.show();
		}

		@Override
		public void onSuccess(List<Account> result) {
			// TODO Auto-generated method stub
			for(int i=0;i<everyPageSize;i++){
				adminView.getUserGrid().setText(i + 1, 0, result.get(i).getEmail());
				adminView.getUserGrid().setText(i + 1, 1, result.get(i).getUsername());
				adminView.getUserGrid().setText(i + 1, 2, result.get(i).getCompany());
				adminView.getUserGrid().setText(i + 1, 3, result.get(i).getPosition());
				adminView.getUserGrid().setText(i + 1, 4, formatter.format(result.get(i).getCreatetime()));
				final HorizontalPanel powerField = new HorizontalPanel();
				final CheckBox cb1 = new CheckBox();
				final CheckBox cb2 = new CheckBox();
				final CheckBox cb3 = new CheckBox();
				Label lb1 = new Label( Constants.adminUIMsg.power1() );
				Label lb2 = new Label( Constants.adminUIMsg.power2() );
				Label lb3 = new Label( Constants.adminUIMsg.power3() );
				lb1.getElement().getStyle().setMarginTop(1, Unit.PX);
				lb2.getElement().getStyle().setMarginTop(1, Unit.PX);
				lb3.getElement().getStyle().setMarginTop(1, Unit.PX);
				cb1.getElement().getStyle().setMarginLeft(5, Unit.PX);
				cb2.getElement().getStyle().setMarginLeft(5, Unit.PX);
				cb3.getElement().getStyle().setMarginLeft(5, Unit.PX);
				powerField.add(cb1);
				powerField.add(lb1);
				powerField.add(cb2);
				powerField.add(lb2);
				powerField.add(cb3);
				powerField.add(lb3);
				String arr[] = result.get(i).getPower().split("");
				if(arr[1].equals("1")){
					cb1.setValue(true);
				}
				if(arr[2].equals("1")){
					cb2.setValue(true);
				}
				if(arr[3].equals("1")){
					cb3.setValue(true);
				}
				cb1.setEnabled(false);
				cb2.setEnabled(false);
				cb3.setEnabled(false);
				adminView.getUserGrid().setWidget(i + 1, 5, powerField);
				final String userEmail = result.get(i).getEmail();
				final Label editUser = new Label();
				editUser.setTitle( Constants.adminUIMsg.editPower() );
				editUser.addStyleName("admin-user-edit");
				editUser.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						cb1.setEnabled(true);
						cb2.setEnabled(true);
						cb3.setEnabled(true);
						editUser.addClickHandler(new ClickHandler(){

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								String newPower = "";
								if(cb1.getValue()){
									newPower = newPower + "1";
								}else{
									newPower = newPower + "0";
								}
								if(cb2.getValue()){
									newPower = newPower + "1";
								}else{
									newPower = newPower + "0";
								}
								if(cb3.getValue()){
									newPower = newPower + "1";
								}else{
									newPower = newPower + "0";
								}
								Account account = new Account();
								account.setEmail(userEmail);
								account.setPower(newPower);
								accountService.updatePower(account, new AsyncCallback<Account>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										alertPanel.setContent(caught.getMessage());
										alertPanel.show();
									}

									@Override
									public void onSuccess(Account result) {
										// TODO Auto-generated method stub
										if (result != null) {
											alertPanel.setContent( Constants.adminUIMsg.powerSuccess() );
											alertPanel.show();
										}
									}

								});
							}
								
						});
					}

				});
				Label deleteUser = new Label();
				deleteUser.setTitle( Constants.adminUIMsg.deleteUser() );
				deleteUser.addStyleName("admin-user-delete");
				deleteUser.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						deletePanel.setContent( Constants.adminUIMsg.userDelete1() + userEmail + Constants.adminUIMsg.userDelete2() );
						deletePanel.show();
						deletePanel.getConfirmBtn().addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Account account = new Account();
								account.setEmail(userEmail);
								accountService.deleteAccount(account, new AsyncCallback<String>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										deletePanel.hide();
										alertPanel.setContent(caught.getMessage());
										alertPanel.show();
									}

									@Override
									public void onSuccess(String result) {
										// TODO Auto-generated method stub
										if (result.equals("success")) {
											deletePanel.hide();
											alertPanel.setContent( Constants.adminUIMsg.userSuccess() );
											alertPanel.show();
										} else {
											deletePanel.hide();
											alertPanel.setContent(result);
											alertPanel.show();
										}
									}
								});
							}

						});
					}

				});
				HorizontalPanel operate = new HorizontalPanel();
				operate.addStyleName("admin-user");
				operate.add(editUser);
				operate.add(deleteUser);
				if(!userEmail.equals(AppController.email)){
					adminView.getUserGrid().setWidget(i + 1, 6, operate);
				}
			}
		}
	});
  }
  
  /**
	* Load the paging for the first time
   *
   * @param pageSize
	*/
  public void pageLoader(int pageSize){
	if(pageSize>20){
		headStart = 1;
		tailStart = lastPage - 9;
		adminView.getUserPage().resize(1, 25);
		adminView.getUserPage().setWidget(0, 0, first);
		adminView.getUserPage().setWidget(0, 1, prev);
		for(int count=2;count<12;count++){
			adminView.getUserPage().setWidget(0, count, new Label(headStart+""));
			headStart++;
		}
		adminView.getUserPage().setText(0, 12, "...");
		for(int count=13;count<23;count++){
			adminView.getUserPage().setWidget(0, count, new Label(tailStart+""));
			tailStart++;
		}
		adminView.getUserPage().setWidget(0, 23, next);
		adminView.getUserPage().setWidget(0, 24, last);
	}else{
		adminView.getUserPage().resize(1, pageSize+4);
		adminView.getUserPage().setWidget(0, 0, first);
		adminView.getUserPage().setWidget(0, 1, prev);
		for(int count=2;count<pageSize+2;count++){
			adminView.getUserPage().setWidget(0, count, new Label((count-1)+""));
		}
		adminView.getUserPage().setWidget(0, pageSize+2, next);
		adminView.getUserPage().setWidget(0, pageSize+3, last);
	}
	adminView.getUserPage().getWidget(0, 2).removeStyleName("gwt-Label");
	adminView.getUserPage().getWidget(0, 2).addStyleName("admin-page-selected");
  }
  
  /**
	* Reload the paging
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
			adminView.getUserPage().clear();
			adminView.getUserPage().resize(1, 25);
			adminView.getUserPage().setWidget(0, 0, first);
			adminView.getUserPage().setWidget(0, 1, prev);
			adminView.getUserPage().setText(0, 2, "...");
			for(int count=3;count<22;count++){
				adminView.getUserPage().setWidget(0, count, new Label(headStart+""));
				headStart++;
			}
			adminView.getUserPage().setText(0, 22, "...");
			adminView.getUserPage().setWidget(0, 23, next);
			adminView.getUserPage().setWidget(0, 24, last);
			int stylePage = (currentPage - Integer.parseInt(adminView.getUserPage().getText(0, 3)))+3;
			adminView.getUserPage().getWidget(0, stylePage).removeStyleName("gwt-Label");
			adminView.getUserPage().getWidget(0, stylePage).addStyleName("admin-page-selected");
		}else{
			adminView.getUserPage().clear();
			adminView.getUserPage().resize(1, 25);
			adminView.getUserPage().setWidget(0, 0, first);
			adminView.getUserPage().setWidget(0, 1, prev);
			for(int count=2;count<12;count++){
				adminView.getUserPage().setWidget(0, count, new Label(headStart+""));
				headStart++;
			}
			adminView.getUserPage().setText(0, 12, "...");
			for(int count=13;count<23;count++){
				adminView.getUserPage().setWidget(0, count, new Label(tailStart+""));
				tailStart++;
			}
			adminView.getUserPage().setWidget(0, 23, next);
			adminView.getUserPage().setWidget(0, 24, last);
			if(currentPage>=Integer.parseInt(adminView.getUserPage().getText(0, 2))&&currentPage<=Integer.parseInt(adminView.getUserPage().getText(0, 11))){
				adminView.getUserPage().getWidget(0, currentPage+1).removeStyleName("gwt-Label");
				adminView.getUserPage().getWidget(0, currentPage+1).addStyleName("admin-page-selected");
			}else if(currentPage>=Integer.parseInt(adminView.getUserPage().getText(0, 13))&&currentPage<=Integer.parseInt(adminView.getUserPage().getText(0, 22))){
				int stylePage = (currentPage - Integer.parseInt(adminView.getUserPage().getText(0, 13)))+13;
				adminView.getUserPage().getWidget(0, stylePage).removeStyleName("gwt-Label");
				adminView.getUserPage().getWidget(0, stylePage).addStyleName("admin-page-selected");
			}
		}
	}else{
		if(currentPage > 0 && currentPage < (pageSize + 1)){
			adminView.getUserPage().clear();
			adminView.getUserPage().resize(1, pageSize+4);
			adminView.getUserPage().setWidget(0, 0, first);
			adminView.getUserPage().setWidget(0, 1, prev);
			for(int count=2;count<pageSize+2;count++){
				adminView.getUserPage().setWidget(0, count, new Label((count-1)+""));
			}
			adminView.getUserPage().setWidget(0, pageSize+2, next);
			adminView.getUserPage().setWidget(0, pageSize+3, last);
			adminView.getUserPage().getWidget(0, currentPage+1).removeStyleName("gwt-Label");
			adminView.getUserPage().getWidget(0, currentPage+1).addStyleName("admin-page-selected");
		}
	}
  }
}
