/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import java.util.Map;
import eml.studio.client.ui.tree.DatasetCateTreeLoader;
import eml.studio.client.ui.tree.DatasetCategoryTree;
import eml.studio.client.ui.tree.ProgramCategoryTree;
import eml.studio.client.ui.tree.ProgramCateTreeLoader;
import eml.studio.client.util.Constants;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

/**
 * Select directory panel
 */
public class CategoryPanel extends PopupPanel {
	private String type;
	private Label title = new Label(Constants.adminUIMsg.chooseCate());
	private Label change = new Label();
	private Label close = new Label();
	private VerticalPanel vPanel = new VerticalPanel();
	private ScrollPanel sPanel = new ScrollPanel();
	private DatasetCategoryTree dataTree= new DatasetCategoryTree();
	private ProgramCategoryTree progTree= new ProgramCategoryTree();
	private HorizontalPanel mask = new HorizontalPanel();
	private VerticalPanel root = new VerticalPanel();
	
	public CategoryPanel(String type) {
		this.type = type;
	    HorizontalPanel alertHeader = new HorizontalPanel();
	    title.addStyleName("cateAddBoxTitle");
	    close.addStyleName("alertClose");
	    change.addStyleName("change");
	    change.setTitle( Constants.adminUIMsg.change());
	    alertHeader.add(title);
	    alertHeader.add(close);
	    alertHeader.addStyleName("alertTitleBG");
	    
	    if(type.equals("data")){
	    	dataTree = DatasetCateTreeLoader.load();
	    	dataTree.getElement().getStyle().setMarginTop(10, Unit.PX);
	    	dataTree.getElement().getStyle().setMarginLeft(20, Unit.PX);
	    	sPanel.add(dataTree);
	    }else{
	    	progTree = ProgramCateTreeLoader.load();
	    	progTree.getElement().getStyle().setMarginTop(10, Unit.PX);
	    	progTree.getElement().getStyle().setMarginLeft(20, Unit.PX);
	    	sPanel.add(progTree);
	    }
		vPanel.add(alertHeader);
		vPanel.add(sPanel);
	    sPanel.addStyleName("min_height_250");

	    vPanel.addStyleName("alertBox");
	    mask.addStyleName("alertBack");
	    
	    bind();
	    root.add(mask);
	    root.add(vPanel);
	    this.add(root);
	    this.removeStyleName("gwt-PopupPanel");
	}
	
	/**
	 * Event binding
	 */
	public void bind(){
		close.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				CategoryPanel.this.hide();
			}
		});
	}
	
	public Label getChange(){
		return change;
	}
	
	public Label getClose(){
		return close;
	}
	
	public HorizontalPanel getMask(){
		return mask;
	}
	
	public Tree getCategoryTree(){
		if(this.type.equals("data")){
			return dataTree;
		}else
			return progTree;
	}
	
	public Map<TreeItem, String> getSysTreeCateMap(){
		if(this.type.equals("data")){
			return dataTree.getSysTreeCateMap();
		}else
			return progTree.getSysTreeCateMap();
	}
	
	public Map<TreeItem, String> getShrTreeCateMap(){
		if(this.type.equals("data")){
			return dataTree.getShrTreeCateMap();
		}else
			return progTree.getShrTreeCateMap();
	}
	
	public Map<TreeItem, String> getMyTreeCateMap(){
		if(this.type.equals("data")){
			return dataTree.getMyTreeCateMap();
		}else
			return progTree.getMyTreeCateMap();
	}
}
