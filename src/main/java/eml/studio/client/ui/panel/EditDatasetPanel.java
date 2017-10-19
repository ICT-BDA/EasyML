/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import eml.studio.client.controller.DBController;
import eml.studio.client.mvp.AppController;
import eml.studio.client.ui.panel.component.DescribeGrid;
import eml.studio.client.ui.tree.DatasetLeaf;
import eml.studio.client.ui.tree.DatasetTree;
import eml.studio.client.util.Constants;
import eml.studio.client.util.TimeUtils;
import eml.studio.shared.model.Category;
import eml.studio.shared.model.Dataset;
import eml.studio.client.rpc.CategoryService;
import eml.studio.client.rpc.CategoryServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

/**
 *Edit DataSet information panel
 */
public class EditDatasetPanel extends BasePanel {
	private DBController dbController = new DBController();
	private CategoryServiceAsync categorySrv = GWT.create(CategoryService.class);
	private DatasetTree tree;
	private DatasetLeaf node;
	protected String owner = null;
	protected Button updatebt = new Button(Constants.studioUIMsg.submit());
	protected Dataset dataset;
	public EditDatasetPanel(String emailAccount,final DatasetTree tree,final DatasetLeaf node) {
		super();
		owner = emailAccount;
		labarr = new String[][]{
				{"Name", "Category", "DataType", "Version", "CreateDate", "Owner", "Description"},
				{Constants.studioUIMsg.dataName(), Constants.studioUIMsg.dataCategory(), 
					Constants.studioUIMsg.dataType(), Constants.studioUIMsg.dataVersion(),
					Constants.studioUIMsg.dataCreateTime(), Constants.studioUIMsg.dataOwner(), 
					Constants.studioUIMsg.dataDescription()},
					{"textbox", "tree", "listbox", "textbox", "textbox", "textbox", "textarea"},
					{"true", "true", "true", "true", "false", "false", "true"},
					{"", Constants.studioUIMsg.chooseCategory(), "General/CSV/TSV", "0.1", 
						TimeUtils.timeNow(), AppController.email, ""},
						{"left", "left", "left", "left", "left", "left", "right"}
		};
		this.tree = tree;
		this.node = node;
	}

	@Override
	public void init(){
		grid = new DescribeGrid(labarr, "data");
		verticalPanel.add(grid);
		grid.addStyleName("bda-descgrid-data");
		updatebt.setStyleName("bda-fileupdate-btn");
		verticalPanel.add(updatebt);
		updatebt.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dbController.submitEditDataset2DB(EditDatasetPanel.this,tree,node,dataset ,grid);
			}
		});
	}

	public Button getUpdatebt () {
		return updatebt;
	}
	public void setUpdatebt (Button updatebt){
		this.updatebt = updatebt;
	}
	public void show(Dataset dataset){
		this.dataset = dataset;
		final String values[] = DBController.getDatasetPanelValue(dataset,false);
		final String category = dataset.getCategory();
		if("我的数据".equals(category) || "my data".equals(category.toLowerCase())
				|| "共享数据".equals(category) || "shared data".equals(category.toLowerCase())
				|| "系统数据".equals(category) || "system data".equals(category.toLowerCase()) ){
			values[1] = category;
			EditDatasetPanel.this.setValues(values);
			EditDatasetPanel.this.init();
			EditDatasetPanel.this.center();
			EditDatasetPanel.this.show();
		}else{
			categorySrv.getCategory(category, new AsyncCallback<Category>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					values[1] = category;
					EditDatasetPanel.this.setValues(values);
					EditDatasetPanel.this.init();
					EditDatasetPanel.this.center();
					EditDatasetPanel.this.show();
				}

				@Override
				public void onSuccess(Category result) {
					// TODO Auto-generated method stub
					if("".equals(result.getPath())){
						values[1] = result.getName();
					}else
						values[1] = result.getPath();

					EditDatasetPanel.this.setValues( values );
					EditDatasetPanel.this.init();
					EditDatasetPanel.this.center();
					EditDatasetPanel.this.show();
				}

			});
		}
	}
}


