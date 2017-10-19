/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import eml.studio.client.controller.DBController;
import eml.studio.client.mvp.AppController;
import eml.studio.client.ui.panel.component.DescribeGrid;
import eml.studio.client.util.Constants;
import eml.studio.client.util.TimeUtils;
import eml.studio.shared.model.Dataset;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Save Data Set Panel
 */
public class SaveDatasetPanel extends BasePanel{
	private DBController dbController = new DBController();
	protected Button savebtn = new Button(Constants.studioUIMsg.submit());
	protected Dataset dataset = new Dataset();
	private  PreviewPopupPanel panel;
	public SaveDatasetPanel (PreviewPopupPanel panel) {
		super();
		this.panel = panel;
		labarr = new String[][]{
				{"Name","Category","DataType","Version","CreateDate","Owner","Description"},//Fields
				{Constants.studioUIMsg.dataName(), Constants.studioUIMsg.dataCategory()
					, Constants.studioUIMsg.dataType(), Constants.studioUIMsg.dataVersion(),
					Constants.studioUIMsg.dataCreateTime(), Constants.studioUIMsg.dataOwner(), Constants.studioUIMsg.dataDescription()},//显示的标签
					{"textbox","listbox","listbox","textbox","textbox","textbox","textarea"},//The type of field box
					{"true","true","true","true","false","false","true"},
					{"", Constants.studioUIMsg.myData() + "/" + Constants.studioUIMsg.sharedData(),
						"General/CSV/TSV", "0.1", TimeUtils.timeNow(), AppController.email, ""},
						{"left","left","left","left","left","left","right"}
		};
	}
	@Override
	protected void init(){
		grid = new DescribeGrid(labarr, "data");
		verticalPanel.add(grid);
		grid.addStyleName("bda-descgrid-savedata");
		savebtn.setStyleName("bda-descgrid-savedata-submitbtn");
		SimplePanel simPanel = new SimplePanel();
		simPanel.add( savebtn );
		simPanel.setStyleName("bda-descgrid-savedata-simpanel");
		verticalPanel.add(simPanel);
		savebtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dbController.submitSaveDataset2DB(panel,SaveDatasetPanel.this, dataset,grid);
			}
		});
	}

	public Button getSavebtn() {
		return savebtn;
	}

	public void setSavebtn(Button savebtn) {
		this.savebtn = savebtn;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}
}