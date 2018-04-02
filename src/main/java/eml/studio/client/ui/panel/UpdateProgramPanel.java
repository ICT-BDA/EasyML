/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import eml.studio.client.mvp.AppController;
import eml.studio.client.rpc.CategoryService;
import eml.studio.client.rpc.CategoryServiceAsync;
import eml.studio.client.ui.panel.component.DescribeGrid;
import eml.studio.client.controller.DBController;
import eml.studio.client.util.Constants;
import eml.studio.client.util.TimeUtils;
import eml.studio.client.util.UUID;
import eml.studio.client.ui.panel.Uploader.UploadFileModule;
import eml.studio.client.ui.tree.ProgramLeaf;
import eml.studio.client.ui.tree.ProgramTree;
import eml.studio.client.ui.tree.ProgramTreeLoader;
import eml.studio.client.ui.widget.command.CommandParseException;
import eml.studio.shared.model.Category;
import eml.studio.shared.model.Program;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.moxieapps.gwt.uploader.client.events.UploadCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteHandler;

/**
 * Update Program Panel
 */
public class UpdateProgramPanel extends BasePanel {

	private DBController dbController = new DBController();
	private CategoryServiceAsync categorySrv = GWT.create(CategoryService.class);
	private ProgramTree tree;
	private ProgramLeaf node;
	protected UploadFileModule uploaderPanel = null;
	private Program program = new Program();
	public UpdateProgramPanel(final ProgramTree tree,final ProgramLeaf node) {
		super();
		labarr = new String[][]{
				{"Name","Category","Type","Programable","Isdeterministic","Version","CreateDate",
					"Owner","Description","CommandLine","TensorflowMode"},
					{Constants.studioUIMsg.moduleName(), Constants.studioUIMsg.moduleCategory()
						, Constants.studioUIMsg.moduleType(), Constants.studioUIMsg.programable(),
						Constants.studioUIMsg.moduleDeterminacy(),Constants.studioUIMsg.moduleVersion(),
						Constants.studioUIMsg.moduleCreateTime(), Constants.studioUIMsg.moduleOwner(),
						Constants.studioUIMsg.dataDescription(),Constants.studioUIMsg.moduleCMDFormat(),Constants.studioUIMsg.tensorflowMode()},//Displayed label
						{"textbox","tree","listbox","listbox","listbox","textbox","textbox",
							"textbox","textarea","textarea","listbox"},
							{"true","true","true","true","true","true","false","false","true","true","true"},
							{"", Constants.studioUIMsg.chooseCategory(), 
								Constants.studioUIMsg.standalone() + "/" + Constants.studioUIMsg.distributed()+"/ETL"+"/Tensorflow",// "Singleton or distribute",
								Constants.studioUIMsg.no()+"/" + Constants.studioUIMsg.yes(),
								Constants.studioUIMsg.no()+"/" + Constants.studioUIMsg.yes(),
								"0.1", TimeUtils.timeNow(), AppController.email, "", "",Constants.studioUIMsg.standalone()+"/"+Constants.studioUIMsg.modelDistributed()+"/"+Constants.studioUIMsg.dataDistributed()},
								{"left","left","left","left","left","left","left","left","left","right","right"}
		};
		this.tree = tree;
		this.node = node;
	}

	@Override
	public void init() {
		grid = new DescribeGrid(labarr, "prog");
		verticalPanel.add(grid);
		initFileUploader();
		grid.addStyleName("bda-descgrid-model");
	}

	public void initFileUploader(){
		uploaderPanel = new UploadFileModule();
		uploaderPanel.setUpLoadProgram(program);
		verticalPanel.add(uploaderPanel);
		uploaderPanel.getUploader().setUploadCompleteHandler(new UploadCompleteHandler() {
			@Override
			public boolean onUploadComplete(UploadCompleteEvent uploadCompleteEvent) {
				uploaderPanel.getCancelButtons().get(uploadCompleteEvent.getFile().getId()).removeFromParent();
				uploaderPanel.getUploader().cancelUpload(uploaderPanel.getFileQueuedId(), false);
				ProgramLeaf node = new ProgramLeaf(uploaderPanel.getUpLoadProgram());
				ProgramTreeLoader.addContextMenu(tree, node);
				ProgramTreeLoader.addProgramLeaf(tree, node,AppController.email);
				Window.alert("Uploaded Success！");
				UpdateProgramPanel.this.hide();
				UpdateProgramPanel.this.clean();
				return true;
			}
		});
		uploaderPanel.getStartbt().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				JSONObject params = new JSONObject();
				if (!uploaderPanel.getIsFileQueued()) {
					Window.alert("Please choose a file to upload, can not be empty");
					return;
				}
				if (!uploaderPanel.getFileQueuedName().endsWith(".zip")) {
					uploaderPanel.getUploader().cancelUpload(uploaderPanel.getFileQueuedId(), false);
					uploaderPanel.getProgressBars().clear();
					Window.alert("The file must be in the standard zip format, please re-select the file");
					return;
				}
				uploaderPanel.setNewFileUUID(UUID.randomUUID());
				params.put("Fileuuid", new JSONString(uploaderPanel.getNewFileUUID()));
				uploaderPanel.getUploader().setOption("post_params", params).setPostParams(params);
				try {
					if (dbController.submitUpdateProgram2DB(uploaderPanel,tree,node,program,grid)) {
						uploaderPanel.getUploader().startUpload();
						uploaderPanel.getStartbt().setEnabled(false);
					}
				} catch (CommandParseException e) {
					Window.alert(e.getMessage());
				}
			}
		});
	}


	public UploadFileModule getUploaderPanel() {
		return uploaderPanel;
	}

	public void setUploaderPanel(UploadFileModule uploaderPanel) {
		this.uploaderPanel = uploaderPanel;
	}

	public void show(Program program){
		this.program = program;
		final String values[] = DBController.getProgramPanelValue(program,false);
		final String category = program.getCategory();
		if("我的程序".equals(category) || "my program".equals(category.toLowerCase())
				|| "共享程序".equals(category) || "shared program".equals(category.toLowerCase())
				|| "系统程序".equals(category) || "system program".equals(category.toLowerCase()) ){
			values[1] = category;
			UpdateProgramPanel.this.setValues(values);
			UpdateProgramPanel.this.init();
			UpdateProgramPanel.this.center();
			UpdateProgramPanel.this.show();
		}else{
			categorySrv.getCategory(category, new AsyncCallback<Category>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					values[1] = category;
					UpdateProgramPanel.this.setValues(values);
					UpdateProgramPanel.this.init();
					UpdateProgramPanel.this.center();
					UpdateProgramPanel.this.show();
				}

				@Override
				public void onSuccess(Category result) {
					// TODO Auto-generated method stub
					if("".equals(result.getPath())){
						values[1] = result.getName();
					}else
						values[1] = result.getPath();

					UpdateProgramPanel.this.setValues( values );
					UpdateProgramPanel.this.init();
					UpdateProgramPanel.this.center();
					UpdateProgramPanel.this.show();
				}

			});
		}
	}
}
