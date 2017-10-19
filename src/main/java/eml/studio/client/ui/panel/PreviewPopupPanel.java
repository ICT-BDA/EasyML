/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import java.util.logging.Logger;
import eml.studio.client.rpc.DatasetService;
import eml.studio.client.rpc.DatasetServiceAsync;
import eml.studio.client.ui.tree.PopupRetDirLeaf;
import eml.studio.client.ui.tree.PopupRetDirTree;
import eml.studio.client.ui.tree.PopupRetDirTreeLoad;
import eml.studio.shared.model.Dataset;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.orange.links.client.menu.ContextMenu;

/**
 * Result preview popup box
 * 
 */
public class PreviewPopupPanel extends PopupPanel {

	protected static DatasetServiceAsync datasetSrv = GWT.create(DatasetService.class);
	protected static Logger logger = Logger.getLogger(PopupRetDirTreeLoad.class.getName());
	private static final double MAX_DOWNLOAD_SIZE = 1000000; //Maximum file size (in kb)

	final Label desc = new Label("File information");
	private Label fileLabel = new Label();
	private TextArea fileTextArea = new TextArea();
	private PopupRetDirTree resultDirTree = null;
	private Anchor uploadSubmitButton = new Anchor("Download");
	private Button savebtn = new Button("Save");
	private Button refreshBtn = new Button("Refresh");
	private HTML closeButton = new HTML("X");
	private Dataset dataset = new Dataset();
	private String sourceUrl = null;
	private TabLayoutPanel tabPanel = new TabLayoutPanel(2.5, Unit.EM);
	HorizontalPanel dirPanel = null;
	private Label fileSizeLabel = new Label();


	public PreviewPopupPanel(String path) {
		init(path);
		bind(path);
		ContextMenu.disableBrowserContextMenu(this.asWidget().getElement());
	}

	/**
	 * Initialize
	 * 
	 * @param path  Result root address
	 */
	protected void init(String path) {
		this.setSize("650px", "400px");
		this.setGlassEnabled(true);
		this.setModal(true);
		closeButton.setSize("10px", "10px");
		closeButton.setStyleName("closebtn");
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PreviewPopupPanel.this.hide();
			}
		});
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(closeButton);
		verticalPanel.setCellHeight(closeButton, "13px");
		verticalPanel.setStyleName("vpanel");
		desc.setStyleName("popupTitle");
		verticalPanel.add(desc);
		verticalPanel.setCellHeight(desc, "30px");

		HorizontalPanel hPanel = new HorizontalPanel();
		savebtn.setStyleName("popupsavebtn");
		savebtn.setVisible(false);
		refreshBtn.setStyleName("popuprefreshbtn");
		refreshBtn.setVisible(false);
		uploadSubmitButton.setVisible(false);
		hPanel.add(uploadSubmitButton);
		hPanel.add(savebtn);
		hPanel.add(refreshBtn);
		hPanel.setCellVerticalAlignment(uploadSubmitButton,
				HasVerticalAlignment.ALIGN_BOTTOM);

		tabPanel.getElement().getStyle().setMarginBottom(10.0, Unit.PX);
		tabPanel.setSize("650px", "355px");
		dirPanel = new HorizontalPanel();
		resultDirTree = PopupRetDirTreeLoad.load(path);
		resultDirTree.getRoot().setState(false);
		ScrollPanel dirScrollPanel = new ScrollPanel();
		dirScrollPanel.add(resultDirTree);
		dirScrollPanel.setAlwaysShowScrollBars(true);
		dirScrollPanel.setSize("300px", "320px");

		VerticalPanel fileViewPanel = new VerticalPanel();
		fileLabel.setText("Please select a file to view!");
		fileLabel.setStyleName("popupFileSelectName");
		fileViewPanel.add(fileLabel);
		fileTextArea.setStyleName("popupMsg");
		fileTextArea.setSize("342px", "298px");
		fileTextArea.getElement().setAttribute("wrap", "off");    
		fileTextArea.setReadOnly(true);
		fileViewPanel.add(fileTextArea);
		dirPanel.add(dirScrollPanel);
		dirPanel.add(fileViewPanel);
		tabPanel.add(dirPanel,"Results directory");

		AbsolutePanel bottomPanel = new AbsolutePanel(); 
		bottomPanel.setSize("650px", "360px");
		bottomPanel.add(tabPanel, 0, 0);
		bottomPanel.add(hPanel,460,3);

		fileSizeLabel.setStyleName("popupFileSelectName");
		verticalPanel.add(bottomPanel); 
		verticalPanel.add(fileSizeLabel);
		this.add(verticalPanel);
		this.setStyleName("loading_container");
	}

	/**
	 * Control event binding
	 * 
	 * @param path  Result root address path
	 */
	public void bind(final String path){
		uploadSubmitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				Window.alert("Start calculating the file size");
				datasetSrv.getFileSize(path, new AsyncCallback<Double>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						logger.warning("Failed to get download file size!");
					}

					@Override
					public void onSuccess(Double size) {
						// TODO Auto-generated method stub
						if(size<MAX_DOWNLOAD_SIZE )
						{
							Window.alert("Start downloading the file");
							String url = GWT.getModuleBaseURL().split("EMLStudio")[0]
									+ "EMLStudioMonitor/filedownload?filename=" + sourceUrl;
							Window.open(url, "_blank", "status=0,toolbar=0,menubar=0,location=0");
						}
						else
							Window.alert("Download file over limit size (1g), can not download the file!");
					}

				});

			}
		});
		savebtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				final SaveDatasetPanel saveDatasetPanel = new SaveDatasetPanel(PreviewPopupPanel.this);
				saveDatasetPanel.setDataset(dataset);
				saveDatasetPanel.init();
				saveDatasetPanel.show();
				PreviewPopupPanel.this.hide();
				saveDatasetPanel.center();
			}
		});

		resultDirTree.addSelectionHandler(new SelectionHandler<TreeItem>(){
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				// TODO Auto-generated method stub
				TreeItem item = event.getSelectedItem();
				if (item instanceof PopupRetDirLeaf) {
					PopupRetDirLeaf leaf = (PopupRetDirLeaf)item;
					fileLabel.setText(leaf.getText());
					PopupRetDirTreeLoad.wrapTreeNode(leaf);
					String filePath = leaf.getPath();
					//Only the root directory for data loading tips
					if(leaf.getName().equals(resultDirTree.getRoot().getName()) && leaf.getChildCount() == resultDirTree.getRoot().getChildCount())
						fileTextArea.setText("The data is loading, please be patient ...");
					datasetSrv.previewFile(filePath, 100,new AsyncCallback<String>(){
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							logger.warning("File preview failed, the directory did not produce results!");
						}
						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							fileTextArea.setText(result);
						}
					});

					//The root directory does not file size statistics,
					// because of its slow statistics
					if(!(leaf.getName().equals(resultDirTree.getRoot().getName()) && leaf.getChildCount() == resultDirTree.getRoot().getChildCount()))
					{
						datasetSrv.getFileSize(filePath, new AsyncCallback<Double>(){
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								logger.warning("Failed to get file size!");
							}
							@Override
							public void onSuccess(Double result) {
								// TODO Auto-generated method stub
								fileSizeLabel.setVisible(true);
								fileSizeLabel.setText("TotalSize:"+result+"kb");
							}
						});
					}
					else
					{
						fileSizeLabel.setVisible(false);
					}
				}
			}}
				);

		refreshBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				fileLabel.setText("");
				fileSizeLabel.setText("");
				fileTextArea.setText("");
				resultDirTree.clear();
				PopupRetDirTreeLoad.reBuild(resultDirTree,path);
			}
		});

	}

	public Anchor getUploadSubmitButton() {
		return uploadSubmitButton;
	}

	public void setUploadSubmitButton(Anchor uploadSubmitButton) {
		this.uploadSubmitButton = uploadSubmitButton;
	}

	public Label getDesc() {
		return desc;
	}

	public Button getSavebtn() {
		return savebtn;
	}

	public void setSavebtn(Button savebtn) {
		this.savebtn = savebtn;
	}

	public HTML getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(HTML closeButton) {
		this.closeButton = closeButton;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	@Override
	public void center(){
		super.center();
	}

	/**
	 * @return the refreshBtn
	 */
	public Button getRefreshBtn() {
		return refreshBtn;
	}

	/**
	 * @param refreshBtn the refreshBtn to set
	 */
	public void setRefreshBtn(Button refreshBtn) {
		this.refreshBtn = refreshBtn;
	}

	/**
	 * @return the fileTextArea
	 */
	public TextArea getFileTextArea() {
		return fileTextArea;
	}

	/**
	 * @param fileTextArea the fileTextArea to set
	 */
	public void setFileTextArea(TextArea fileTextArea) {
		this.fileTextArea = fileTextArea;
	}

	/**
	 * @return the fileLabel
	 */
	public Label getFileLabel() {
		return fileLabel;
	}

	/**
	 * @param fileLabel the fileLabel to set
	 */
	public void setFileLabel(Label fileLabel) {
		this.fileLabel = fileLabel;
	}

	public TabLayoutPanel getTabPanel() {
		return tabPanel;
	}

	public void setTabPanel(TabLayoutPanel tabPanel) {
		this.tabPanel = tabPanel;
	}

	/**
	 * @return the resultDirTree
	 */
	public PopupRetDirTree getResultDirTree() {
		return resultDirTree;
	}

	/**
	 * @param resultDirTree the resultDirTree to set
	 */
	public void setResultDirTree(PopupRetDirTree resultDirTree) {
		this.resultDirTree = resultDirTree;
	}

	/**
	 * @return the fileSizeLabel
	 */
	public Label getFileSizeLabel() {
		return fileSizeLabel;
	}

	/**
	 * @param fileSizeLabel the fileSizeLabel to set
	 */
	public void setFileSizeLabel(Label fileSizeLabel) {
		this.fileSizeLabel = fileSizeLabel;
	}
}
