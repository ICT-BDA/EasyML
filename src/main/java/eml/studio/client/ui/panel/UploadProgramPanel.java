/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import java.util.logging.Logger;

import eml.studio.client.controller.DBController;
import eml.studio.client.mvp.AppController;
import eml.studio.client.ui.panel.Uploader.UploadFileModule;
import eml.studio.client.ui.panel.component.DescribeGrid;
import eml.studio.client.ui.tree.ProgramTreeLoader;
import eml.studio.client.ui.widget.command.CommandParseException;
import eml.studio.client.util.Constants;
import eml.studio.shared.model.Program;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteHandler;
import eml.studio.client.mvp.presenter.MonitorPresenter;
import eml.studio.client.ui.tree.ProgramLeaf;
import eml.studio.client.util.TimeUtils;
import eml.studio.client.util.UUID;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

/**
 * Upload Program Panel
 */
public class UploadProgramPanel extends BasePanel {
	private static Logger logger = Logger.getLogger( UploadProgramPanel.class.getName() );
	private DBController dbController = new DBController();
	private final MonitorPresenter presenter;
	protected UploadFileModule uploaderPanel = null;
	private Program program = new Program();
	public UploadProgramPanel(String emailAccount,MonitorPresenter presenter) {
		this.presenter = presenter;
		labarr = new String[][]{
				{"Name","Category","Type","Programable","Isdeterministic","Version","CreateDate",
					"Owner","Description","CommandLine","TensorflowMode"},
					{Constants.studioUIMsg.moduleName(), Constants.studioUIMsg.moduleCategory()
						, Constants.studioUIMsg.moduleType(), Constants.studioUIMsg.programable(),
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
		init();
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
				uploaderPanel.getCancelButtons().get(uploadCompleteEvent.getFile().getId())
				.removeFromParent();
				uploaderPanel.getUploader().cancelUpload(uploaderPanel.getFileQueuedId(), false);

				Window.alert("Uploaded successfully！");
				UploadProgramPanel.this.hide();
				UploadProgramPanel.this.clean();
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
					if (dbController.submitUploadProgram2DB(presenter,presenter.getView(), uploaderPanel,program,grid)) {
						uploaderPanel.getUploader().startUpload();
						uploaderPanel.getStartbt().setEnabled(false);
					}
				} catch (CommandParseException e) {
					logger.info(e.getMessage());
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

	public void setProgram(Program program){
		this.program = program;
		this.setValues(DBController.getProgramPanelValue(program,true));
	}
}
