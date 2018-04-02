/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import eml.studio.client.controller.DBController;
import eml.studio.client.mvp.AppController;
import eml.studio.client.rpc.CategoryService;
import eml.studio.client.rpc.CategoryServiceAsync;
import eml.studio.client.ui.panel.component.DescribeGrid;
import eml.studio.client.ui.tree.ProgramLeaf;
import eml.studio.client.ui.tree.ProgramTree;
import eml.studio.client.util.Constants;
import eml.studio.client.util.TimeUtils;
import eml.studio.shared.model.Program;
import eml.studio.shared.model.Category;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

/**
 * Edit Program panel
 *
 */
public class EditProgramPanel extends BasePanel{
	private DBController dbController = new DBController();
	private CategoryServiceAsync categorySrv = GWT.create(CategoryService.class);
	private ProgramTree tree;
	private ProgramLeaf node;
	protected Button updatebt = new Button(Constants.studioUIMsg.submit());
	protected Program program = null;

	public EditProgramPanel(String emailAccount,final ProgramTree tree,final ProgramLeaf node) {
		super();
		labarr = new String[][]{
				{"Name","Category","Type","Programable","Isdeterministic","Version","CreateDate",
					"Owner","Description","CommandLine","TensorflowMode"},
					{Constants.studioUIMsg.moduleName(), Constants.studioUIMsg.moduleCategory(), 
						Constants.studioUIMsg.moduleType(), Constants.studioUIMsg.programable(),
						Constants.studioUIMsg.moduleDeterminacy(),Constants.studioUIMsg.moduleVersion(),
						Constants.studioUIMsg.moduleCreateTime(), Constants.studioUIMsg.moduleOwner(),
						Constants.studioUIMsg.dataDescription(),Constants.studioUIMsg.moduleCMDFormat(),Constants.studioUIMsg.tensorflowMode()},
						{"textbox","tree","listbox","listbox","listbox","textbox","textbox",
							"textbox","textarea","textarea","listbox"},
							{"true","true","true","true","true","true","false","false","true","true","true"},
							{"", Constants.studioUIMsg.chooseCategory(),
								Constants.studioUIMsg.standalone() + "/" + Constants.studioUIMsg.distributed()+"/ETL"+"/Tensorflow",
								Constants.studioUIMsg.no()+"/" + Constants.studioUIMsg.yes(),
								Constants.studioUIMsg.no()+"/" + Constants.studioUIMsg.yes(),
								"0.1", TimeUtils.timeNow(), AppController.email, "", "",Constants.studioUIMsg.standalone()+"/"+Constants.studioUIMsg.modelDistributed()+"/"+Constants.studioUIMsg.dataDistributed()},
								{"left","left","left","left","left","left","left","left","left","right","right"}
		};
		owner = emailAccount;
		this.tree = tree;
		this.node = node;
	}
	@Override
	public void init() {
		grid = new DescribeGrid(labarr, "prog");
		verticalPanel.add(grid);
		grid.addStyleName("bda-descgrid-model");
		updatebt.setStyleName("bda-fileupdate-btn");
		verticalPanel.add(updatebt);
		updatebt.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dbController.submitEditProgram2DB(EditProgramPanel.this,tree,node, program,grid);
			}
		});
	}

	public Button getUpdatebt() {
		return updatebt;
	}
	public void setUpdatebt(Button updatebt) {
		this.updatebt = updatebt;
	}
	public Program getProgram() {
		return program;
	}
	public void show(Program program){
		this.program = program;
		final String values[] =DBController.getProgramPanelValue(program,false);
		final String category = program.getCategory();
		if("我的程序".equals(category) || "my program".equals(category.toLowerCase())
				|| "共享程序".equals(category) || "shared program".equals(category.toLowerCase())
				|| "系统程序".equals(category) || "system program".equals(category.toLowerCase()) ){
			values[1] = category;
			EditProgramPanel.this.setValues(values);
			EditProgramPanel.this.init();
			EditProgramPanel.this.center();
			EditProgramPanel.this.show();
		}else{
			categorySrv.getCategory(category, new AsyncCallback<Category>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					values[1] = category;
					EditProgramPanel.this.setValues(values);
					EditProgramPanel.this.init();
					EditProgramPanel.this.center();
					EditProgramPanel.this.show();
				}

				@Override
				public void onSuccess(Category result) {
					// TODO Auto-generated method stub
					if("".equals(result.getPath())){
						values[1] = result.getName();
					}else
						values[1] = result.getPath();

					EditProgramPanel.this.setValues( values );
					EditProgramPanel.this.init();
					EditProgramPanel.this.center();
					EditProgramPanel.this.show();
				}

			});
		}
	}

}
