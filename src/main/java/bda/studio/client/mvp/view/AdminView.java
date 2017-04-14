package bda.studio.client.mvp.view;

import bda.studio.client.util.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * Background management page layout
 * 
 */
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
  Grid progGrid;  
  
  @UiField  
  Grid dataGrid;
  
  @UiField  
  Grid userGrid;

  @UiField  
  HTMLPanel catePanel;

  @UiField  
  Anchor dataAdd;
  
  @UiField  
  Anchor progAdd;
  
  @UiField  
  Grid cateGrid;
  
  @UiField  
  Grid progPage;
  
  @UiField  
  Grid dataPage;
  
  @UiField  
  Grid userPage;

  @UiField  
  Grid catePage;
  
  @UiField
  HTMLPanel content;
  
  public AdminView() {
    initWidget(ourUiBinder.createAndBindUi(this));
    
	userProg.addStyleName("current");
	userProg.setText( Constants.headerUIMsg.progManagement() );
	userData.setText( Constants.headerUIMsg.dataManagement() );
	userList.setText( Constants.headerUIMsg.userManagement() );
	category.setText( Constants.headerUIMsg.cateManagement() );
	workStage.setText( Constants.headerUIMsg.workStage() );
	logout.setText( Constants.headerUIMsg.logout() );
	dataAdd.setText( Constants.adminUIMsg.dataAdd() );
	progAdd.setText( Constants.adminUIMsg.progAdd() );
    
	dataAdd.setTitle("add new category for dataset");
	progAdd.setTitle("add new category for program");
	
    progGrid.setVisible(true);
    progPage.setVisible(true);
    dataGrid.setVisible(false);
    dataPage.setVisible(false);
    userGrid.setVisible(false);
    userPage.setVisible(false);
    catePanel.setVisible(false);
    catePage.setVisible(false);
  }
  
  public void setAccount(String usernameAccount, String emailAccount){
	  if( usernameAccount == "" || usernameAccount == null){
		  userAnchor.setText(emailAccount);
	  }else{
		  userAnchor.setText(usernameAccount);
	  }
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
  
  public Grid getProgGrid(){
	  return progGrid;
  }
  
  public Grid getDataGrid(){
	  return dataGrid;
  }
  
  public Grid getUserGrid(){
	  return userGrid;
  }
  
  public HTMLPanel getCatePanel(){
	  return catePanel;
  }
  
  public Anchor getDataAdd(){
	  return dataAdd;
  }
  
  public Anchor getProgAdd(){
	  return progAdd;
  }
  
  public Grid getCateGrid(){
	  return cateGrid;
  }
  
  public Grid getProgPage(){
	  return progPage;
  }
  
  public Grid getDataPage(){
	  return dataPage;
  }
  
  public Grid getUserPage(){
	  return userPage;
  }
  
  public Grid getCatePage(){
	  return catePage;
  }
  
  public HTMLPanel getAdminPanel(){
	  return content;
  }
}