/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel.Uploader;

import java.util.LinkedHashMap;
import java.util.Map;

import org.moxieapps.gwt.uploader.client.Uploader;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileDialogStartEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogStartHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueueErrorEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueueErrorHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadErrorEvent;
import org.moxieapps.gwt.uploader.client.events.UploadErrorHandler;
import org.moxieapps.gwt.uploader.client.events.UploadProgressEvent;
import org.moxieapps.gwt.uploader.client.events.UploadProgressHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.widgetideas.client.ProgressBar;

/**
 * File uploader for dataset or program
 */
public class FileUploader extends VerticalPanel {

	final Map<String, ProgressBar> progressBars = new LinkedHashMap<String, ProgressBar>();
	final Map<String, Button> cancelButtons = new LinkedHashMap<String, Button>();

	protected final Button cancelButton = new Button("Cancel");
	protected HorizontalPanel progressBarAndButtonPanel = new HorizontalPanel();
	protected HorizontalPanel horizontalPanel = new HorizontalPanel();
	protected final VerticalPanel progressBarPanel = new VerticalPanel();
	protected CancelProgressBarTextFormatter cancelProgressBarTextFormatter = new CancelProgressBarTextFormatter();
	protected final Image dropFilesLabel = new Image("studio/img/droparea.png");
	protected final Uploader uploader = new Uploader();
	protected String fileQueuedName = null;
	protected String fileQueuedId = null;
	protected Boolean isFileQueued = false;
	protected String newFileUUID = null;

	public FileUploader() {
		init();
	}

	public void init() {

		uploader.setButtonImageURL("studio/img/uploadimg.png").setButtonWidth(32)
		.setButtonHeight(32)
		.setButtonCursor(Uploader.Cursor.HAND);

		horizontalPanel.setStyleName("bda-fileupload-bottom-hpanel");
		horizontalPanel.setSpacing(10);
		horizontalPanel.add(uploader);
		if (Uploader.isAjaxUploadWithProgressEventsSupported()) {

			dropFilesLabel.getElement().getStyle().setCursor(Cursor.POINTER);
			dropFilesLabel.setSize("32px", "32px");
			dropFilesLabel.setTitle("File dragable upload area");
		}
		horizontalPanel.add(dropFilesLabel);
		horizontalPanel.add(progressBarPanel);
		horizontalPanel.setCellVerticalAlignment(progressBarPanel,
				HasVerticalAlignment.ALIGN_MIDDLE);
		this.add(horizontalPanel);
		initFacet();
	}

	private void initFacet(){
		this.getUploader()
		.setUploadURL(GWT.getModuleBaseURL() + "fileupload")
		.setFileSizeLimit("20 GB")
		.setButtonAction(Uploader.ButtonAction.SELECT_FILES)
		.setFileQueueLimit(2)
		.setFileQueuedHandler(new FileQueuedHandler() {

			@Override
			public boolean onFileQueued(final FileQueuedEvent fileQueuedEvent) {
				// Create a Progress Bar for this file
				if (FileUploader.this.getFileQueuedId() != null)
					FileUploader.this.getUploader().cancelUpload(FileUploader.this.getFileQueuedId(), false);// delete the last
				FileUploader.this.getProgressBarPanel().clear();
				FileUploader.this.getProgressBarAndButtonPanel().clear();
				// choose file
				FileUploader.this.setIsFileQueued(true);
				final ProgressBar progressBar = new ProgressBar(0.0, 1.0, 0.0,
						FileUploader.this.getCancelProgressBarTextFormatter());
				progressBar.setTitle(fileQueuedEvent.getFile().getName());
				FileUploader.this.setFileQueuedName(fileQueuedEvent.getFile().getName());
				FileUploader.this.setFileQueuedId(fileQueuedEvent.getFile().getId());
				progressBar.addStyleName("bda-fileupload-progressbar");
				progressBar.setTextVisible(true);
				FileUploader.this.getProgressBars().put(fileQueuedEvent.getFile().getId(), progressBar);

				// Add Cancel Button Image
				FileUploader.this.getCancelButton().setStyleName("cancelButton");
				FileUploader.this.getCancelButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						FileUploader.this.getUploader().cancelUpload(fileQueuedEvent.getFile().getId(), false);
						FileUploader.this.getProgressBars().get(fileQueuedEvent.getFile().getId())
						.setProgress(-1.0d);
						FileUploader.this.getCancelButton().removeFromParent();
						FileUploader.this.setIsFileQueued(false);
					}
				});
				FileUploader.this.getCancelButtons().put(fileQueuedEvent.getFile().getId(), FileUploader.this.getCancelButton());
				// Add the Bar and Button to the interface
				progressBarAndButtonPanel.setSpacing(5);
				Label tit = new Label(fileQueuedEvent.getFile().getName());
				// tit.setWidth("100px");
				progressBarAndButtonPanel.add(tit);
				progressBarAndButtonPanel.setCellVerticalAlignment(tit,
						HasVerticalAlignment.ALIGN_MIDDLE);
				progressBarAndButtonPanel.add(progressBar);
				progressBarAndButtonPanel.setCellVerticalAlignment(progressBar,
						HasVerticalAlignment.ALIGN_MIDDLE);
				progressBarAndButtonPanel.add(FileUploader.this.getCancelButton());
				progressBarAndButtonPanel.setCellVerticalAlignment(FileUploader.this.getCancelButton(),
						HasVerticalAlignment.ALIGN_MIDDLE);
				progressBarPanel.add(FileUploader.this.getProgressBarAndButtonPanel());
				progressBarPanel.setCellVerticalAlignment(
						progressBarAndButtonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
				return true;
			}
		}).setUploadProgressHandler(new UploadProgressHandler() {
			@Override
			public boolean onUploadProgress(
					UploadProgressEvent uploadProgressEvent) {
				ProgressBar progressBar = FileUploader.this.getProgressBars().get(uploadProgressEvent
						.getFile().getId());
				progressBar.setProgress((double) uploadProgressEvent
						.getBytesComplete() / uploadProgressEvent.getBytesTotal());

				return true;
			}
		}).setFileDialogStartHandler(new FileDialogStartHandler() {
			@Override
			public boolean onFileDialogStartEvent(
					FileDialogStartEvent fileDialogStartEvent) {
				if (FileUploader.this.getUploader().getStats().getUploadsInProgress() <= 0) {
					// Clear the uploads that have completed, if none are in process
					FileUploader.this.getProgressBarPanel().clear();
					FileUploader.this.getProgressBars().clear();
					FileUploader.this.getCancelButtons().clear();
				}
				return true;
			}
		}).setFileDialogCompleteHandler(new FileDialogCompleteHandler() {
			@Override
			public boolean onFileDialogComplete(
					FileDialogCompleteEvent fileDialogCompleteEvent) {
				if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
					if (FileUploader.this.getUploader().getStats().getUploadsInProgress() <= 0) {
						// uploader.startUpload();
					}
				}
				return true;
			}
		}).setFileQueueErrorHandler(new FileQueueErrorHandler() {
			@Override
			public boolean onFileQueueError(
					FileQueueErrorEvent fileQueueErrorEvent) {
				Window.alert("Upload of file "
						+ fileQueueErrorEvent.getFile().getName() + " failed due to ["
						+ fileQueueErrorEvent.getErrorCode().toString() + "]: "
						+ fileQueueErrorEvent.getMessage());
				return true;
			}
		}).setUploadErrorHandler(new UploadErrorHandler() {
			@Override
			public boolean onUploadError(UploadErrorEvent uploadErrorEvent) {
				FileUploader.this.getCancelButtons().get(uploadErrorEvent.getFile().getId())
				.removeFromParent();
				Window.alert("Upload of file "
						+ uploadErrorEvent.getFile().getName() + " failed due to ["
						+ uploadErrorEvent.getErrorCode().toString() + "]: "
						+ uploadErrorEvent.getMessage());
				return true;
			}
		});
		FileUploader.this.getDropFilesLabel().addDragOverHandler(new DragOverHandler() {
			@Override
			public void onDragOver(DragOverEvent event) {
				if (!FileUploader.this.getUploader().getButtonDisabled()) {
					//uploader.getDropFilesLabel().addStyleName("dropFilesLabelHover");
				}
			}
		});
		FileUploader.this.getDropFilesLabel().addDragLeaveHandler(new DragLeaveHandler() {
			@Override
			public void onDragLeave(DragLeaveEvent event) {
				//uploader.getDropFilesLabel().removeStyleName("dropFilesLabelHover");
			}
		});
		FileUploader.this.getDropFilesLabel().addDropHandler(new DropHandler() {
			@Override
			public void onDrop(DropEvent event) {
				//uploader.getDropFilesLabel().removeStyleName("dropFilesLabelHover");

				if (FileUploader.this.getUploader().getStats().getUploadsInProgress() <= 0) {
					FileUploader.this.getProgressBarPanel().clear();
					FileUploader.this.getProgressBars().clear();
					FileUploader.this.getCancelButtons().clear();
				}

				FileUploader.this.getUploader().addFilesToQueue(Uploader.getDroppedFiles(event
						.getNativeEvent()));
				event.preventDefault();
			}
		});
	}

	public class CancelProgressBarTextFormatter extends
	ProgressBar.TextFormatter {
		@Override
		protected String getText(ProgressBar bar, double curProgress) {
			if (curProgress < 0) {
				return "Cancelled";
			}
			return ((int) (100 * bar.getPercent())) + "%";
		}
	}

	public String getFileQueuedName() {
		return fileQueuedName;
	}

	public void setFileQueuedName(String fileQueuedName) {
		this.fileQueuedName = fileQueuedName;
	}

	public String getFileQueuedId() {
		return fileQueuedId;
	}

	public void setFileQueuedId(String fileQueuedId) {
		this.fileQueuedId = fileQueuedId;
	}

	public Boolean getIsFileQueued() {
		return isFileQueued;
	}

	public void setIsFileQueued(Boolean isFileQueued) {
		this.isFileQueued = isFileQueued;
	}

	public Map<String, Button> getCancelButtons() {
		return cancelButtons;
	}

	public Uploader getUploader() {
		return uploader;
	}

	public String getNewFileUUID() {
		return newFileUUID;
	}

	public void setNewFileUUID(String newFileUUID) {
		this.newFileUUID = newFileUUID;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public HorizontalPanel getProgressBarAndButtonPanel() {
		return progressBarAndButtonPanel;
	}

	public void setProgressBarAndButtonPanel(HorizontalPanel progressBarAndButtonPanel) {
		this.progressBarAndButtonPanel = progressBarAndButtonPanel;
	}

	public HorizontalPanel getHorizontalPanel() {
		return horizontalPanel;
	}

	public void setHorizontalPanel(HorizontalPanel horizontalPanel) {
		this.horizontalPanel = horizontalPanel;
	}

	public Map<String, ProgressBar> getProgressBars() {
		return progressBars;
	}

	public CancelProgressBarTextFormatter getCancelProgressBarTextFormatter() {
		return cancelProgressBarTextFormatter;
	}

	public void setCancelProgressBarTextFormatter(CancelProgressBarTextFormatter cancelProgressBarTextFormatter) {
		this.cancelProgressBarTextFormatter = cancelProgressBarTextFormatter;
	}

	public VerticalPanel getProgressBarPanel() {
		return progressBarPanel;
	}

	public Image getDropFilesLabel() {
		return dropFilesLabel;
	}
}
