/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.util;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

/**
 * Base pagination control for paging mechanism
 */
public class Pagination {
	private Grid grid;
	private int pageSize;           // Page size
	private int headStart;          //The first start bit of the first half page(except first&prev)
	private int tailStart;          //The first start bit of the second half page
	private int lastPage;           //maximum page
	private int pageType;
	/*
    Pagination type examples:
    10: both 10 pages in front and back, maximum number of pages is 20 and total number of paging is 25.
    5: both 5 pages in front and back, maximum number of pages is 10 and total number of paging is 15.
    2: both 2 pages in front and back, maximum number of pages is 4 and total number of paging is 9.
	 */
	private final Label first = new Label( Constants.adminUIMsg.firstPage() );
	private final Label prev = new Label( Constants.adminUIMsg.prevPage() );
	private final Label next = new Label( Constants.adminUIMsg.nextPage() );
	private final Label last = new Label( Constants.adminUIMsg.lastPage() );

	public enum PageType{ 
		SMALL(2), MIDDLE(5), LARGE(10);

		private int type;

		private PageType(int type){
			this.type = type;
		}
		public void setType(int type){
			this.type = type;
		}
		public int getType(){
			return type;
		}
	};

	public Pagination(Grid grid, int pageSize, PageType pageType){
		this.grid = grid;
		this.pageSize = pageSize;
		this.lastPage = pageSize;
		this.pageType = pageType.getType();
	}

	/**
	 * Load the first page
	 */
	 public void load(){
		if(pageSize>pageType*2){
			headStart = 1;
			tailStart = lastPage - (pageType - 1);
			grid.resize(1, (pageType*2+5));
			grid.setWidget(0, 0, first);
			grid.setWidget(0, 1, prev);
			for(int count=2;count<(pageType+2);count++){
				grid.setWidget(0, count, new Label(headStart+""));
				headStart++;
			}
			grid.setText(0, (pageType+2), "...");
			for(int count=(pageType+3);count<(pageType*2+3);count++){
				grid.setWidget(0, count, new Label(tailStart+""));
				tailStart++;
			}
			grid.setWidget(0, (pageType*2+3), next);
			grid.setWidget(0, (pageType*2+4), last);
		}else{
			grid.resize(1, pageSize + 4);
			grid.setWidget(0, 0, first);
			grid.setWidget(0, 1, prev);
			for(int count=2;count<pageSize+2;count++){
				grid.setWidget(0, count, new Label((count-1)+""));
			}
			grid.setWidget(0, pageSize+2, next);
			grid.setWidget(0, pageSize+3, last);
		}
		grid.getWidget(0, 2).removeStyleName("gwt-Label");
		grid.getWidget(0, 2).addStyleName("admin-page-selected");
	 }

	 /**
	  * After click on one page number, calculate and reload page numbers
	  * 
	  * @param currentPage
	  */
	 public void reload(int currentPage){
		 if(pageSize > pageType*2){
			 if(currentPage == 1 || currentPage == lastPage){
				 headStart = 1;
				 tailStart = lastPage - (pageType - 1);
			 }else{
				 if(currentPage > (pageType - 1) && (currentPage+pageType - 2)<lastPage){
					 headStart = currentPage - (pageType - 1);
					 tailStart = 0;
				 }else{
					 headStart = 1;
					 tailStart = lastPage - (pageType - 1);
				 }
			 }
			 if(tailStart == 0){
				 grid.clear();
				 grid.resize(1, (pageType*2+5));
				 grid.setWidget(0, 0, first);
				 grid.setWidget(0, 1, prev);
				 grid.setText(0, 2, "...");
				 for(int count=3;count<(pageType*2+2);count++){
					 grid.setWidget(0, count, new Label(headStart+""));
					 headStart++;
				 }
				 grid.setText(0, (pageType*2+2), "...");
				 grid.setWidget(0, (pageType*2+3), next);
				 grid.setWidget(0, (pageType*2+4), last);
				 int stylePage = (currentPage - Integer.parseInt(grid.getText(0, 3)))+3;
				 grid.getWidget(0, stylePage).removeStyleName("gwt-Label");
				 grid.getWidget(0, stylePage).addStyleName("admin-page-selected");
			 }else{
				 grid.clear();
				 grid.resize(1, (pageType*2+5));
				 grid.setWidget(0, 0, first);
				 grid.setWidget(0, 1, prev);
				 for(int count=2;count<(pageType+2);count++){
					 grid.setWidget(0, count, new Label(headStart+""));
					 headStart++;
				 }
				 grid.setText(0, (pageType+2), "...");
				 for(int count=(pageType+3);count<(pageType*2+3);count++){
					 grid.setWidget(0, count, new Label(tailStart+""));
					 tailStart++;
				 }
				 grid.setWidget(0, (pageType*2+3), next);
				 grid.setWidget(0, (pageType*2+4), last);
				 if(currentPage>=Integer.parseInt(grid.getText(0, 2))&&currentPage<=Integer.parseInt(grid.getText(0, (pageType+1)))){
					 grid.getWidget(0, currentPage+1).removeStyleName("gwt-Label");
					 grid.getWidget(0, currentPage+1).addStyleName("admin-page-selected");
				 }else if(currentPage>=Integer.parseInt(grid.getText(0, (pageType+3)))&&currentPage<=Integer.parseInt(grid.getText(0, (pageType*2+2)))){
					 int stylePage = (currentPage - Integer.parseInt(grid.getText(0, (pageType+3))))+(pageType+3);
					 grid.getWidget(0, stylePage).removeStyleName("gwt-Label");
					 grid.getWidget(0, stylePage).addStyleName("admin-page-selected");
				 }
			 }
		 }else{
			 if(currentPage > 0 && currentPage < (pageSize + 1)){
				 grid.resize(1, pageSize+4);
				 grid.setWidget(0, 0, first);
				 grid.setWidget(0, 1, prev);
				 for(int count=2;count<pageSize+2;count++){
					 grid.setWidget(0, count, new Label((count-1)+""));
				 }
				 grid.setWidget(0, pageSize+2, next);
				 grid.setWidget(0, pageSize+3, last);
				 grid.getWidget(0, currentPage+1).removeStyleName("gwt-Label");
				 grid.getWidget(0, currentPage+1).addStyleName("admin-page-selected");
			 }
		 }
	 }

	 /**
	  * Acquire the current page number and update  the variable 'current' according to the resulting page information 'crtPageText' by click
	  * 
	  * @param current
	  * @param crtPageText
	  * @param lastpage
	  * @return
	  */
	 public int getCurrentPage(int current, String crtPageText, int lastpage){
		 if(crtPageText.equals( Constants.adminUIMsg.firstPage() )){         //Click the first page button
			 current = 1;
		 }else if(crtPageText.equals( Constants.adminUIMsg.prevPage() )){    //Click the previous page button
			 if(current > 1){
				 current = current - 1;
			 }else{
				 current = 1;
			 }
		 }else if(crtPageText.equals( Constants.adminUIMsg.nextPage() )){    //Click the next page button
			 if(current < lastpage){
				 current = current + 1;
			 }else{
				 current = lastpage;
			 }
		 }else if(crtPageText.equals( Constants.adminUIMsg.lastPage() )){    //Click the last page button
			 current = lastpage;
		 }else{                                                              //Click the normal page button
			 current = Integer.parseInt(crtPageText);
		 }
		 return current;
	 }
}
