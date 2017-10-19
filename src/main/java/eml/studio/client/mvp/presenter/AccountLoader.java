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
import eml.studio.client.util.Pagination;
import eml.studio.shared.model.Account;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * User  management in backstage manegement 
 */
public class AccountLoader {
	private final AdminView adminView;
	private AccountServiceAsync accountService = GWT.create(AccountService.class);
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
	private Grid page = new Grid();								//Pagination control area
	private Pagination pagination;							
	private Account searchAccount;								
	private String searchEmail;
	private String searchName;
	private String searchCompany;
	private String searchPosition;
	private int searchPower;

	//true: search result; false: all result; use to distinguish the paging control response from data load function: load or reload
	private boolean searchFlag = false; 

	public AccountLoader(AdminView adminView) {
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

		//Enter button event for searching
		adminView.getUserSearch().addDomHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode()== 13){
					adminView.getUserSearchBtn().click();
				}
			}
		}, KeyUpEvent.getType());

		//Button event for searching
		adminView.getUserSearchBtn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//Clear last search account object information before a new search
				searchAccount = new Account();
				searchFlag = true;

				//Get search information and save to searchAccount object
				searchEmail = adminView.getUserEmail().getValue();
				searchName = adminView.getUserName().getValue();
				searchCompany = adminView.getUserCmp().getValue();
				searchPosition = adminView.getUserPst().getValue();
				searchPower = adminView.getUserPower().getSelectedIndex();

				if(searchEmail != ""){
					searchAccount.setEmail(searchEmail);
				}else{
					searchAccount.setEmail(null);
				}
				if(searchName != ""){
					searchAccount.setUsername(searchName);
				}else{
					searchAccount.setUsername(null);
				}
				if(searchCompany != ""){
					searchAccount.setCompany(searchCompany);
				}else{
					searchAccount.setCompany(null);
				}
				if(searchPosition != ""){
					searchAccount.setPosition(searchPosition);
				}else{
					searchAccount.setPosition(null);
				}
				searchAccount.setPower(Integer.toString(searchPower));

				//If all the input is empty, prompt user enter input information. If is not , then begin searching
				if(searchEmail == "" && searchName == "" && searchCompany == "" && searchPosition == "" && searchPower == 0){
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
		clearInput();											
		adminView.getUserPage().clear();		
		page.addStyleName("admin-page");
		adminView.getUserPage().add(page);
		currentPage = 1;

		Account currentAccount = new Account();
		currentAccount.setEmail(Cookies.getCookie("bdaemail"));
		accountService.getSize(currentAccount, new AsyncCallback<Integer>(){

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

		adminView.getUserGrid().resize(everyPageSize+1, 7);
		adminView.getUserGrid().setText(0, 0, "Email");
		adminView.getUserGrid().setText(0, 1, "Username");
		adminView.getUserGrid().setText(0, 2, "Company");
		adminView.getUserGrid().setText(0, 3, "Position");
		adminView.getUserGrid().setText(0, 4, "Join Date");
		adminView.getUserGrid().setText(0, 5, "Power");
		adminView.getUserGrid().setText(0, 6, "Operation");

		Account currentAccount = new Account();
		currentAccount.setEmail(Cookies.getCookie("bdaemail"));
		accountService.findValid(currentAccount, resultStart, everyPageSize, new AsyncCallback<List<Account>>(){

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
	 * Load search result and reset paging control 
	 */	
	private void searchResult(){
		adminView.getUserPage().clear();					//Clear paging area and reload
		adminView.getUserPage().add(page);
		Account currentAccount = new Account();
		currentAccount.setEmail(Cookies.getCookie("bdaemail"));

		accountService.search(currentAccount, searchAccount, 0, 0, new AsyncCallback<List<Account>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				searchAlert.setContent(caught.getMessage());
				searchAlert.show();
			}

			@Override
			public void onSuccess(List<Account> result) {
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
	}

	/**
	 * Reload search result
	 */
	private void reload(){
		resultStart = (currentPage-1)*13;
		if(currentPage == lastPage){
			everyPageSize = resultSize - resultStart;
		}else{
			everyPageSize = 13;
		}

		adminView.getUserGrid().resize(everyPageSize+1, 7);
		adminView.getUserGrid().setText(0, 0, "Email");
		adminView.getUserGrid().setText(0, 1, "Username");
		adminView.getUserGrid().setText(0, 2, "Company");
		adminView.getUserGrid().setText(0, 3, "Position");
		adminView.getUserGrid().setText(0, 4, "Join Date");
		adminView.getUserGrid().setText(0, 5, "Power");
		adminView.getUserGrid().setText(0, 6, "Operation");

		Account currentAccount = new Account();
		currentAccount.setEmail(Cookies.getCookie("bdaemail"));

		accountService.search(currentAccount, searchAccount, resultStart, everyPageSize, new AsyncCallback<List<Account>>(){

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
	 * Clear input area
	 */
	private void clearInput(){
		adminView.getUserEmail().setValue(null);
		adminView.getUserName().setValue(null);
		adminView.getUserCmp().setValue(null);
		adminView.getUserPst().setValue(null);
		adminView.getUserPower().setItemSelected(0, true);
	}

	public boolean isSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(boolean searchFlag) {
		this.searchFlag = searchFlag;
	}
}
