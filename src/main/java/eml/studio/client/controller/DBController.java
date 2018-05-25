/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import eml.studio.client.mvp.AppController;
import eml.studio.client.mvp.presenter.MonitorPresenter.View;
import eml.studio.client.mvp.presenter.Presenter;
import eml.studio.client.rpc.AesService;
import eml.studio.client.rpc.AesServiceAsync;
import eml.studio.client.rpc.CategoryService;
import eml.studio.client.rpc.CategoryServiceAsync;
import eml.studio.client.rpc.DatasetService;
import eml.studio.client.rpc.DatasetServiceAsync;
import eml.studio.client.rpc.ProgramService;
import eml.studio.client.rpc.ProgramServiceAsync;
import eml.studio.client.ui.binding.DatasetBinder;
import eml.studio.client.ui.panel.component.DescribeGrid;
import eml.studio.client.ui.binding.ProgramBinder;
import eml.studio.client.ui.panel.DataTypeSelectPanel;
import eml.studio.client.ui.panel.EditDatasetPanel;
import eml.studio.client.ui.panel.EditProgramPanel;
import eml.studio.client.ui.panel.PreviewPopupPanel;
import eml.studio.client.ui.panel.SaveDatasetPanel;
import eml.studio.client.ui.panel.Uploader.UploadFileModule;
import eml.studio.client.ui.tree.DatasetLeaf;
import eml.studio.client.ui.tree.DatasetTree;
import eml.studio.client.ui.tree.DatasetTreeLoader;
import eml.studio.client.ui.tree.ProgramLeaf;
import eml.studio.client.ui.tree.ProgramTree;
import eml.studio.client.ui.tree.ProgramTreeLoader;
import eml.studio.client.ui.widget.command.CommandParseException;
import eml.studio.client.ui.widget.command.CommandParser;
import eml.studio.client.ui.widget.shape.OutNodeShape;
import eml.studio.client.util.Constants;
import eml.studio.shared.model.Dataset;
import eml.studio.shared.model.Program;
import eml.studio.shared.util.DatasetType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The database interaction controller related to 
 * the program and dataset.
 * 
 */
public class DBController {
	public static ProgramServiceAsync programSrv = GWT.create(ProgramService.class);
	public static DatasetServiceAsync datasetSrv = GWT.create(DatasetService.class);
	public static CategoryServiceAsync categorySrv = GWT.create(CategoryService.class);
	public static AesServiceAsync aesSrv = GWT.create(AesService.class);
	public static ProgramBinder programBinder = GWT.create(ProgramBinder.class);
	public static DatasetBinder datasetBinder = GWT.create(DatasetBinder.class);
	private static Logger logger = Logger.getLogger(DBController.class.getName());

	/**
	 * Submit the uploaded program information to the database.
	 * 
	 * @param presenter
	 * @param fileUploader
	 * @param program
	 * @param grid
	 * @return  If submit is success
	 * @throws CommandParseException
	 */
	public boolean submitUploadProgram2DB(final Presenter presenter,  final View view, final UploadFileModule fileUploader,
			Program program, final DescribeGrid grid)
					throws CommandParseException {
		String name = grid.getText("Name");
		if (name == null || "".equals(name)) {
			Window.alert("Name could not be empty！");
			return false;
		}
		String category = grid.getText("Category");
		if(category == null || "".equals(category) || category.equals("Choose Category")){
			Window.alert("Category could not be empty！");
			return false;
		}
		String cmdline = grid.getText("CommandLine");
		if (cmdline == null || "".equals(cmdline)) {
			Window.alert("CommandLine could not be empty！");
			return false;
		} else {
			String programable = grid.getText("Programable");
			if( !Constants.studioUIMsg.yes().equals( programable ) )
				CommandParser.parse(cmdline);
		}
		logger.info("grid"+grid.ProgramToString());
		programBinder.sync(grid, program);
		fileUploader.setUpLoadProgram(program);
		logger.info("programm"+grid.asProgram(program).toString());
		programSrv.upload(grid.asProgram(program), fileUploader.getNewFileUUID(),
				new AsyncCallback<Program>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.info(caught.getMessage());
			}

			@Override
			public void onSuccess(Program result) {
				fileUploader.setUpLoadProgram(result);
				ProgramLeaf node = new ProgramLeaf(result);
				ProgramTreeLoader.addContextMenu( view.getProgramTree(),
						node);
				ProgramTreeLoader.addProgramLeaf( view.getProgramTree(),
						node,AppController.email);
				logger.info("Program insert to DB:"+result.getCategory());
			}
		});
		return true;
	}

	/**
	 * Submit the uploaded dataSet  information to the database.
	 * 
	 * @param fileUploader
	 * @param grid
	 * @return  If submit is success
	 * @throws CommandParseException
	 */
	public boolean submitUploadDataset2DB(final Presenter presenter,final View view, final UploadFileModule fileUploader
			,Dataset dataset, DescribeGrid grid)
					throws CommandParseException {
		String name = grid.getText("Name");
		if (name == null || "".equals(name)) {
			Window.alert("Name could not be empty！");
			return false;
		}
		String category = grid.getText("Category");
		if(category == null || "".equals(category) || category.equals("Choose Category")){
			Window.alert("Category could not be empty！");
			return false;
		}
		datasetBinder.sync(grid, dataset);
		fileUploader.setUpLoadDataset(dataset);
		datasetSrv.upload(grid.asDataset(dataset), fileUploader.getNewFileUUID(),
				new AsyncCallback<Dataset>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(Dataset result) {
				fileUploader.setUpLoadDataset(result);
				DatasetLeaf node = new DatasetLeaf(result);
				DatasetTreeLoader.addContextMenu( view.getDatasetTree()
						,node);
				DatasetTreeLoader.addDatasetLeaf( view.getDatasetTree(),
						node,AppController.email);
				logger.info("Dataset insert to DB:"+result.getCategory());
			}
		});
		return true;
	}

	/**
	 * Upgrade the program information to the database.
	 * 
	 * @param fileUploader
	 * @param tree
	 * @param node
	 * @param program
	 * @param grid
	 * @return  If submit is success
	 * @throws CommandParseException
	 */
	public boolean submitUpdateProgram2DB(final UploadFileModule fileUploader,final ProgramTree tree, final ProgramLeaf node,
			final Program program, final DescribeGrid grid )
					throws CommandParseException {
		String name = grid.getText("Name");
		if (name == null || "".equals(name)) {
			Window.alert("Name could not be empty！");
			return false;
		}
		String category = grid.getText("Category");
		if(category == null || "".equals(category) || category.equals("Choose Category")){
			Window.alert("Category could not be empty！");
			return false;
		}
		String cmdline = grid.getText("CommandLine");
		if (cmdline == null || "".equals(cmdline)) {
			Window.alert("CommandLine could not be empty！");
			return false;
		} else {
			if( !program.getProgramable() )
				CommandParser.parse(cmdline);
		}
		programBinder.sync(grid, program);
		fileUploader.setUpLoadProgram(program);
		programSrv.upload(grid.asProgram(program), fileUploader.getNewFileUUID(),
				new AsyncCallback<Program>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(Program result) {
				fileUploader.setUpLoadProgram(result);
			}
		});
		programSrv.upgrade(program.getId(), fileUploader.getNewFileUUID(),
				new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(Void result) {
				node.delete();
			}
		}
				);
		return true;
	}

	/**
	 * Upgrade the dataSet information to the database.
	 * 
	 * @param fileUploader
	 * @param tree
	 * @param node
	 * @param dataset
	 * @param grid
	 * @return If submit is success
	 * @throws CommandParseException
	 */
	public boolean submitUpdateDataset2DB(final UploadFileModule fileUploader,final DatasetTree tree, final DatasetLeaf node,
			final Dataset dataset, final DescribeGrid grid)
					throws CommandParseException {
		String name = grid.getText("Name");
		if (name == null || "".equals(name)) {
			Window.alert("Name could not be empty！");
			return false;
		}
		String category = grid.getText("Category");
		if(category == null || "".equals(category) || category.equals("Choose Category")){
			Window.alert("Category could not be empty！");
			return false;
		}
		datasetBinder.sync(grid, dataset);
		fileUploader.setUpLoadDataset(dataset);
		datasetSrv.upload(grid.asDataset(dataset), fileUploader.getNewFileUUID(),
				new AsyncCallback<Dataset>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(Dataset result) {
				fileUploader.setUpLoadDataset(result);
			}
		});
		datasetSrv.upgrade(dataset.getId(), fileUploader.getNewFileUUID(),
				new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(Void result) {
				node.delete();
			}
		}
				);
		return true;
	}

	/**
	 * Update the program information to the database.
	 * 
	 * @param panel
	 * @param tree
	 * @param node
	 * @param program
	 * @param grid
	 * @return If submit is success
	 */
	public boolean submitEditProgram2DB( final EditProgramPanel panel,final ProgramTree tree,final ProgramLeaf node,
			final Program program,final DescribeGrid grid) {
		String name = grid.getText("Name");
		if (name == null || "".equals(name)) {
			Window.alert("Name could not be empty！");
			return false;
		}
		String category = grid.getText("Category");
		if(category == null || "".equals(category) || category.equals("Choose Category")){
			Window.alert("Category could not be empty！");
			return false;
		}
		String cmdline = grid.getText("CommandLine");
		if (cmdline == null || "".equals(cmdline)) {
			Window.alert(" CommandLine could not be empty！");
			return false;
		}
		programBinder.sync(grid, program);
		programSrv.edit(grid.asProgram(program),
				new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.info(caught.getMessage());
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				node.delete();
				ProgramLeaf node = new ProgramLeaf(grid.asProgram(program));
				ProgramTreeLoader.addContextMenu(tree, node);
				ProgramTreeLoader.addProgramLeaf(tree, node,AppController.email);
				Window.alert("Edit success");
				panel.hide();
				History.fireCurrentHistoryState();
			}
		});
		return true;
	}

	/**
	 * Update the dataSet information to the database.
	 * 
	 * @param panel
	 * @param tree
	 * @param node
	 * @param dataset
	 * @param grid
	 * @return If submit is success
	 */
	public boolean submitEditDataset2DB( final EditDatasetPanel panel,final DatasetTree tree, final DatasetLeaf node
			, final Dataset dataset, final DescribeGrid grid) {
		String name = grid.getText("Name");
		if (name == null || "".equals(name)) {
			Window.alert("Name could not be empty！");
			return false;
		}
		String category = grid.getText("Category");
		if(category == null || "".equals(category) || category.equals("Choose Category")){
			Window.alert("Category could not be empty！");
			return false;
		}
		datasetBinder.sync(grid, dataset);
		datasetSrv.edit(grid.asDataset(dataset), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.info(caught.getMessage());
			}
			@Override
			public void onSuccess(Void result) {
				node.delete();
				DatasetLeaf node = new DatasetLeaf(grid.asDataset(dataset));
				DatasetTreeLoader.addContextMenu(tree,
						node);
				DatasetTreeLoader.addDatasetLeaf(tree, node,AppController.email);
				Window.alert("Edit success！");
				panel.hide();
				History.fireCurrentHistoryState();
			}
		});
		return true;
	}

	/**
	 * Save dataset to the database.
	 * 
	 * @param panel
	 * @param saveDatasetPanel
	 * @param dataset
	 * @param grid
	 * @return If submit is success
	 */
	public boolean submitSaveDataset2DB(final PreviewPopupPanel panel, final SaveDatasetPanel saveDatasetPanel
			, final Dataset dataset, final DescribeGrid grid) {
		String name = grid.getText("Name");

		if (name == null || "".equals(name)) {
			Window.alert("Name could not be empty！");
			return false;
		}
		String category = grid.getText("Category");
		if(category == null || "".equals(category) || category.equals("Choose Category")){
			Window.alert("Category could not be empty！");
			return false;
		}
		datasetBinder.sync(grid, dataset);
		if (dataset == null) return false;
		datasetSrv.save(grid.asDataset(dataset), dataset.getPath(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.info(caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				Window.alert("save data set succeeded!");
				saveDatasetPanel.hide();
				panel.show();
				panel.center();
			}

		});
		return true;
	}

	/**
	 * Preview output dataset from database.
	 * 
	 * @param previewPopupPanel  
	 * @param path Dataset hdfs path
	 * @param shape  Output dataset node
	 */
	public void priviewDataset(final PreviewPopupPanel previewPopupPanel, String path, final OutNodeShape shape){
		datasetSrv.loadFile(path, new AsyncCallback<Dataset>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.info(caught.getMessage());
			}

			@Override
			public void onSuccess(Dataset result) {
				logger.info("load data set succeed!" + result);
				previewPopupPanel.setDataset(result);
				if (result == null)
					return;
				previewPopupPanel.getSavebtn().setVisible(true);
				previewPopupPanel.getRefreshBtn().setVisible(true);
				previewPopupPanel.getUploadSubmitButton().setVisible(true);
				previewPopupPanel.getDesc().setText("File information - " + result.getName());
			}

		});
	}

	/**
	 * Get program panel show values .
	 * 
	 * @param program
	 * @param isUpdate
	 * @return  Program panel show value array
	 */
	public static String[] getProgramPanelValue(final Program program, boolean isUpdate) {

		final String[] values = new String[10];
		int i = 0;
		values[i++] = program.getName();

		//for category
		values[i++] = null;
		String TypeString = program.getType();
		logger.info(TypeString.toLowerCase());
		if ("单机".equals(TypeString.toLowerCase()) || "standalone".equals(TypeString.toLowerCase()))
			values[i] = Constants.studioUIMsg.standalone() + "/" + Constants.studioUIMsg.distributed()+"/ETL"+"/Tensorflow";
		if ("spark".equals(TypeString) || "分布式".equals(TypeString) ||TypeString.equals("distributed")||TypeString.toLowerCase().equals("distributed")) {
			values[i] = Constants.studioUIMsg.distributed() + "/" + Constants.studioUIMsg.standalone() + "/ETL"+"/Tensorflow";
		}
		if ("etl".equals(TypeString))
			values[i] = "ETL/"+Constants.studioUIMsg.distributed() + "/" + Constants.studioUIMsg.standalone()+"/Tensorflow";
		if("tensorflow".equals(TypeString))
			values[i] = "Tensorflow/"+Constants.studioUIMsg.distributed() + "/" + Constants.studioUIMsg.standalone()+"/ETL";

		i ++;

		if( program.getProgramable() ) values[i] = Constants.studioUIMsg.yes() + "/" + Constants.studioUIMsg.no();
		else values[i] = Constants.studioUIMsg.no() + "/" + Constants.studioUIMsg.yes();
		i ++;

		boolean deterministic = program.getIsdeterministic();
		if (deterministic) values[i] = Constants.studioUIMsg.yes() + "/" + Constants.studioUIMsg.no();
		else values[i] = Constants.studioUIMsg.no() + "/" + Constants.studioUIMsg.yes();
		i ++;

		double version = 0;
		if ((!program.getVersion().contains("n"))&&isUpdate){
			version = Double.parseDouble(program.getVersion()) + 0.1;
		}else version = Double.parseDouble(program.getVersion());

		values[i] = NumberFormat.getFormat("#0.0").format(version);
		i ++;

		Date dateNow = new Date();
		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat("yyyy-MM-dd KK:mm:ss a");
		values[i] = dateFormat.format(dateNow);
		i ++;

		values[i++] = AppController.email;
		values[i++] = program.getDescription();
		values[i++] = program.getCommandline();

		String tensorflowMode = program.getTensorflowMode();
		if("单机".equals(tensorflowMode) || "standalone".equals(tensorflowMode))
			values[i] = Constants.studioUIMsg.standalone()+"/"+Constants.studioUIMsg.modelDistributed()+"/"+Constants.studioUIMsg.dataDistributed();
		else if("模型分布".equals(tensorflowMode) || "model distributed".equals(tensorflowMode))
			values[i] = Constants.studioUIMsg.modelDistributed()+"/"+Constants.studioUIMsg.standalone()+"/"+Constants.studioUIMsg.dataDistributed();
		else if("数据分布".equals(tensorflowMode) || "data distributed".equals(tensorflowMode))
			values[i] = Constants.studioUIMsg.dataDistributed()+"/"+Constants.studioUIMsg.standalone()+"/"+Constants.studioUIMsg.modelDistributed();
		else 
			values[i]= Constants.studioUIMsg.standalone()+"/"+Constants.studioUIMsg.modelDistributed()+"/"+Constants.studioUIMsg.dataDistributed();

		return values;
	}

	/**
	 * Get dataset panel show values .
	 * 
	 * @param dataset
	 * @param isUpdate
	 * @return  Data panel show value array
	 */
	public static String[] getDatasetPanelValue(final Dataset dataset, boolean isUpdate) {
		String[] values = new String[7];
		Date dateNow = new Date();
		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat("yyyy-MM-dd KK:mm:ss a");
		values[4] = dateFormat.format(dateNow);
		double version = 0;
		if ((!dataset.getVersion().contains("n"))&&isUpdate){
			version = Double.parseDouble(dataset.getVersion()) + 0.1;
		}else version = Double.parseDouble(dataset.getVersion());     
		values[3] = NumberFormat.
				getFormat("#0.0").format(version);

		values[1] = null;
		String TypeString = dataset.getContenttype();
		if (TypeString == null || TypeString.equals("") || "General".equals(TypeString)) values[2] = "General/CSV/TSV/JSON";
		else if ("TSV".equals(TypeString)) values[2] = "TSV/General/CSV/JSON";
		else if ("CSV".equals(TypeString)) values[2] = "CSV/General/TSV/JSON";
		else values[2] = "JSON/General/TSV/CSV";
		values[0] = dataset.getName();
		values[5] = AppController.email;
		values[6] = dataset.getDescription();
		return values;
	}

	/**
	 * Show file preview popup panel.
	 * 
	 * @param path  File hdfs path 
	 * @param fileId  File id
	 */
	public static void showPreviewPopup(final String path,String fileId){

		//Download dataset from corresponding dataset module
		if(path.contains("null"))
		{
			Window.alert("No results have been produced yet!");
			return;
		}
		final PreviewPopupPanel previewPopup = new PreviewPopupPanel(path);
		datasetSrv.loadFile(path, new AsyncCallback<Dataset>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Loading data failed！");
				logger.info(caught.getMessage());
			}
			@Override
			public void onSuccess(Dataset result) {
				if (result == null)
					return;
				previewPopup.setDataset(result);
				previewPopup.getSavebtn().setVisible(true);
				previewPopup.getRefreshBtn().setVisible(true);
				previewPopup.getUploadSubmitButton().setVisible(true);
				previewPopup.getDesc().setText("File information - " + result.getName());
			}
		});
		// Set the current data's resource path
		previewPopup.setSourceUrl( path );
		previewPopup.center();
	}

	/**
	 * Data Visualization
	 * 
	 * @param path  file path
	 * @param fileId  fileId
	 */
	public static void showDataVisualPopup(final String path,String fileId){
		//Data module visualization
		if(path.contains("null"))
		{
			Window.alert("The results have not been produced yet!");
			return;
		}

		datasetSrv.loadFile(path, new AsyncCallback<Dataset>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Loading data failed！");
				logger.info(caught.getMessage());
			}
			@Override
			public void onSuccess(Dataset result) {
				if (result == null)
					return;
				if(result.getContenttype() == null)
				{
					Window.alert("Data type is not defined, please select the data type first！");
					DataTypeSelectPanel dataSelectPanel = new DataTypeSelectPanel(path,result);
					dataSelectPanel.getDescLabel().setText("Data Type Selection - " + result.getName());
					dataSelectPanel.center();
				}
				else if(result.getContenttype().equals(DatasetType.GENERAL.getDesc()))
					Window.alert("Visualization only support json、tsv、csv data type! ");
				else
				{
					List<String> conts = new ArrayList<String>();
					conts.add(path);
					conts.add(result.getContenttype());
					aesSrv.aesEncrypt(conts,  new AsyncCallback<List<String>>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							logger.info(caught.getMessage());
						}

						@Override
						public void onSuccess(List<String> result) {
							// TODO Auto-generated method stub
							Window.open("visualization.html?"+"path="+result.get(0)+"&type="+result.get(1), "BDA Visualization", "");
						}
						
					});
				}
			}
		});
	}
}
