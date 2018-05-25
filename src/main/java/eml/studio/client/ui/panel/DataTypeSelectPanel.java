/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import eml.studio.client.rpc.AesService;
import eml.studio.client.rpc.AesServiceAsync;
import eml.studio.shared.model.Dataset;
import eml.studio.shared.util.DatasetType;

/**
 * Result data type selection popup panel( for data visiualization)
 *
 */
public class DataTypeSelectPanel extends PopupPanel{
	protected static Logger logger = Logger.getLogger(DataTypeSelectPanel.class.getName());
	protected static AesServiceAsync aesSrv = GWT.create(AesService.class);
	private final Label desc = new Label("Data Type Selection");
	private HTML closeButton = new HTML("X");
	private  ListBox typeListBox;
	private Dataset dataset ;
	private String filePath;
	private Button okBtn;
	private Button cancelBtn;

	public DataTypeSelectPanel(String path, Dataset dataset)
	{

		this.dataset = dataset;
		this.filePath = path;
		init();
		bind();
	}

	/**
	 * UI initialization
	 */
	public void init()
	{
		this.setSize("480px", "100px");

		//Dialog box title
		closeButton.setSize("10px", "10px");
		closeButton.setStyleName("closebtn");

		//Selection dialog
		HorizontalPanel typeListPanel = new HorizontalPanel();
		typeListPanel.setStyleName("popupDatatypeSelectPanel");
		typeListBox = new ListBox();
		typeListBox.setWidth("120px");
		typeListBox.addItem("----");
		typeListBox.addItem(DatasetType.GENERAL.getDesc());
		typeListBox.addItem(DatasetType.CSV.getDesc());
		typeListBox.addItem(DatasetType.TSV.getDesc());
		typeListBox.addItem(DatasetType.JSON.getDesc());
		if(dataset.getContenttype() == null || dataset.getContenttype() .equals(""))
			typeListBox.setSelectedIndex(0);
		else
		{
			for(int i = 0 ; i<typeListBox.getItemCount() ; i++)
			{
				if(typeListBox.getItemText(i).equals(dataset.getContenttype()))
				{
					typeListBox.setSelectedIndex(i);
					break;
				}
			}
		}
		Label selectLabel = new Label("Select data type: ");
		typeListPanel.add(selectLabel);
		typeListPanel.add(typeListBox);

		//Ok and cancel button
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setStyleName("popupDatatypeButtonPanel");
		okBtn = new Button("Ok");
		okBtn.setStyleName("button-style");
		cancelBtn = new Button("Cancel");
		cancelBtn.setStyleName("button-style");
		buttonPanel.add(okBtn);
		buttonPanel.add(cancelBtn);

		//Overall arrangement
		VerticalPanel topPanel = new VerticalPanel();
		topPanel.add(closeButton);
		topPanel.setCellHeight(closeButton, "13px");
		topPanel.setStyleName("vpanel");
		desc.setStyleName("popupDatatypeSelectTitle");
		topPanel.add(desc);
		topPanel.setCellHeight(desc, "30px");
		topPanel.add(typeListPanel);
		topPanel.add(buttonPanel);

		this.setGlassEnabled(true);
		this.setModal(true);
		this.add(topPanel);
		this.center();
		this.setStyleName("loading-panel");
	}

	/**
	 * Event initialization
	 */
	public void bind()
	{
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				DataTypeSelectPanel.this.hide();
			}
		});

		okBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				DataTypeSelectPanel.this.hide();
				if(dataset.getContenttype() == null)
					Window.alert("Data type is undefined, please select data type first!");
				else if(dataset.getContenttype().equals(DatasetType.GENERAL.getDesc()))
					Window.alert("Visualization only support data for json、tsv or csv!");
				else
				{
					List<String> conts = new ArrayList<String>();
					conts.add(filePath);
					conts.add(dataset.getContenttype());
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

		cancelBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				DataTypeSelectPanel.this.hide();
			}

		});

		typeListBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				int index = typeListBox.getSelectedIndex();
				if(index != 0)
					dataset.setContenttype(typeListBox.getItemText(index));
				else
					dataset.setContenttype(null);
			}
		});
	}

	/**
	 * Get popup panel title label
	 * 
	 * @return
	 */
	public Label getDescLabel()
	{
		return desc;
	}
}
