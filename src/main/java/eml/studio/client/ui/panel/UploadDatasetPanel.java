/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import java.util.logging.Logger;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteHandler;
import eml.studio.client.controller.DBController;
import eml.studio.client.mvp.AppController;
import eml.studio.client.mvp.presenter.MonitorPresenter;
import eml.studio.client.ui.panel.component.DescribeGrid;
import eml.studio.client.ui.panel.Uploader.UploadFileModule;
import eml.studio.client.ui.tree.DatasetLeaf;
import eml.studio.client.ui.tree.DatasetTreeLoader;
import eml.studio.client.ui.widget.command.CommandParseException;
import eml.studio.client.util.Constants;
import eml.studio.client.util.TimeUtils;
import eml.studio.client.util.UUID;
import eml.studio.shared.model.Dataset;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

/**
 * Upload Data Set Panel
 */
public class UploadDatasetPanel extends BasePanel{
	private static Logger logger = Logger.getLogger(UploadDatasetPanel.class.getName());
	private DBController dbController = new DBController();
	private final MonitorPresenter presenter;
	protected UploadFileModule uploaderPanel;
	private Dataset dataset = new Dataset();

	public UploadDatasetPanel(String emailAccount,MonitorPresenter presenter) {
		super();
		this.presenter = presenter;
		labarr = new String[][]{
				{"Name","Category","DataType","Version","CreateDate","Owner","Description"},
				{Constants.studioUIMsg.dataName(), Constants.studioUIMsg.dataCategory(), 
					Constants.studioUIMsg.dataType(), Constants.studioUIMsg.dataVersion(),
					Constants.studioUIMsg.dataCreateTime(), Constants.studioUIMsg.dataOwner(), 
					Constants.studioUIMsg.dataDescription()},
					{"textbox","tree","listbox","textbox","textbox","textbox","textarea"},
					{"true","true","true","true","false","false","true"},
					{"", Constants.studioUIMsg.chooseCategory(), "General/CSV/TSV", "0.1", 
						TimeUtils.timeNow(), AppController.email, ""},
						{"left","left","left","left","left","left","right"}
		};
		owner = emailAccount;
		init();
	}

	@Override
	public void init() {
		grid = new DescribeGrid(labarr, "data");
		verticalPanel.add(grid);
		initFileUploader();
		grid.addStyleName("bda-descgrid-model");

	}

	public void initFileUploader(){
		uploaderPanel = new UploadFileModule();
		uploaderPanel.setUpLoadDataset(dataset);
		verticalPanel.add(uploaderPanel);
		uploaderPanel.getUploader().setUploadCompleteHandler(new UploadCompleteHandler() {
			@Override
			public boolean onUploadComplete(UploadCompleteEvent uploadCompleteEvent) {
				uploaderPanel.getCancelButtons().get(uploadCompleteEvent.getFile().getId())
				.removeFromParent();
				Window.alert("Uploaded successfully！");
				UploadDatasetPanel.this.hide();
				UploadDatasetPanel.this.clean();
				// Add code here so that the UploadModelPanel is hidden
				uploaderPanel.getUploader().cancelUpload(uploaderPanel.getFileQueuedId(), false);
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
				if (uploaderPanel.getFileQueuedName().endsWith(".zip")) {
					uploaderPanel.getUploader().cancelUpload(uploaderPanel.getFileQueuedId(), false);
					uploaderPanel.getProgressBars().clear();
					Window.alert("Data can not be zip-formatted files, please re-select the file");
					return;
				}
				uploaderPanel.setNewFileUUID(UUID.randomUUID());
				params.put("Datauuid", new JSONString(uploaderPanel.getNewFileUUID()));
				uploaderPanel.getUploader().setOption("post_params", params).setPostParams(params);
				try {
					if (dbController.submitUploadDataset2DB(presenter,presenter.getView(),uploaderPanel,dataset,grid)) {
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

	public void setDataset(Dataset dataset){
		this.dataset = dataset;
		this.setValues( DBController.getDatasetPanelValue(dataset,true) );
	}
}
