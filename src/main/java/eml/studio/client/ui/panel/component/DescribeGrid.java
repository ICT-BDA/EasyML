/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel.component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import eml.studio.client.controller.DBController;
import eml.studio.client.rpc.CategoryService;
import eml.studio.client.rpc.CategoryServiceAsync;
import eml.studio.client.util.Constants;
import eml.studio.client.ui.panel.AlertPanel;
import eml.studio.client.ui.panel.CategoryPanel;
import eml.studio.shared.model.Dataset;
import eml.studio.shared.model.Program;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.*;

/**
 * Describe Grid in DescCmdPanel
 */
public class DescribeGrid extends HorizontalPanel {
	protected static final Logger logger = Logger.getLogger(DescribeGrid.class.getName());
	protected static CategoryServiceAsync categorySrv = GWT.create(CategoryService.class);
	protected VerticalPanel leftPanel = new VerticalPanel();
	protected VerticalPanel rightPanel = new VerticalPanel();
	protected AlertPanel alertPanel = new AlertPanel();
	protected CategoryPanel categoryPanel;
	protected HorizontalPanel mask = new HorizontalPanel();
	protected Map<String, DescWidget> boxMap = new HashMap<String, DescWidget>();
	protected DBController dbController = new DBController();

	public String[][] labarr;
	String type;

	public DescribeGrid(String[][] labarr, String type) {
		categoryPanel = new CategoryPanel(type);
		categoryPanel.hide();
		mask.addStyleName("alertBack");
		mask.getElement().getStyle().setDisplay(Display.NONE);
		this.add(mask);

		this.setStyleName("bda-descgrid");
		this.labarr = labarr;
		this.type = type;
		this.add(leftPanel);
		this.add(rightPanel);
		leftPanel.setStyleName("bda-descgrid-left");
		rightPanel.setStyleName("bda-descgrid-right");
		init();
	}

	public void init() {
		int length = labarr[0].length;
		for (int j = 0; j < length; j++) {

			Widget widget = null;
			if("listbox".equals(labarr[2][j])){
				final DescListBox listBox = new DescListBox();
				String arr[] = labarr[4][j].split("/");
				for (String val : arr) {
					listBox.addItem(val);
				}
				listBox.setVisibleItemCount(1);
				listBox.setStyleName("bda-descgrid-listbox");
				listBox.setEnabled(Boolean.parseBoolean(labarr[3][j]));

				//listBox.setEnabled(false);

				widget = listBox;
			}else if ("tree".equals(labarr[2][j])) {
				final DescTextBox textbox = new DescTextBox();
				textbox.setValue(labarr[4][j]);
				textbox.setStyleName("bda-descgrid-textbox");
				textbox.getElement().getStyle().setCursor(Cursor.DEFAULT);

				if (Boolean.parseBoolean(labarr[3][j])) {

					textbox.addClickHandler( new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub

							categoryPanel.show();
							mask.getElement().getStyle().setDisplay(Display.INLINE);

							categoryPanel.getClose().addClickHandler(new ClickHandler(){

								@Override
								public void onClick(ClickEvent event) {
									// TODO Auto-generated method stub
									categoryPanel.hide();
									mask.getElement().getStyle().setDisplay(Display.NONE);
								}

							});
							Tree tree = categoryPanel.getCategoryTree();
							tree.addSelectionHandler(new SelectionHandler<TreeItem>(){
								@Override
								public void onSelection(SelectionEvent<TreeItem> event) {
									// TODO Auto-generated method stub
									Boolean state = event.getSelectedItem().getState();
									if(!state){
										categoryPanel.hide();
										mask.getElement().getStyle().setDisplay(Display.NONE);
										String cate_str = event.getSelectedItem().getText();
										if(cate_str.equals("共享程序") || cate_str.toLowerCase().equals("shared program") 
												|| cate_str.startsWith("我的程序") || cate_str.toLowerCase().startsWith("my program") 
												|| cate_str.equals("系统程序") || cate_str.toLowerCase().equals("system program")
												|| cate_str.equals("共享数据") || cate_str.toLowerCase().equals("shared data")
												|| cate_str.equals("我的数据") || cate_str.toLowerCase().equals("my data")
												|| cate_str.equals("系统数据") || cate_str.toLowerCase().equals("system data") ){
											textbox.setValue(cate_str);
										}else{
											String sysPath = categoryPanel.getSysTreeCateMap().get(event.getSelectedItem());
											String shrPath = categoryPanel.getShrTreeCateMap().get(event.getSelectedItem());
											String myPath = categoryPanel.getMyTreeCateMap().get(event.getSelectedItem());
											if(sysPath != null){
												textbox.setValue(sysPath);
											}else if(shrPath != null){
												textbox.setValue(shrPath);
											}else if(myPath != null){
												textbox.setValue(myPath);
											}else
												textbox.setValue(null);
										}
									}
								}
							});    				}

					});


				}
				else {
					textbox.setEnabled(false);
				}


				widget = textbox;
			} else if ("textbox".equals(labarr[2][j])) {
				DescTextBox textbox = new DescTextBox();
				textbox.setValue(labarr[4][j]);
				//textbox添加布尔判断
				textbox.setEnabled(Boolean.parseBoolean(labarr[3][j]));
				textbox.setStyleName("bda-descgrid-textbox");
				widget = textbox;
			} else if ("textarea".equals(labarr[2][j])) {
				DescTextArea textarea = new DescTextArea();
				textarea.setValue(labarr[4][j]);
				textarea.setEnabled(Boolean.parseBoolean(labarr[3][j]));
				textarea.setStyleName("bda-descgrid-textarea");
				if ("Description".equals(labarr[0][j])) {
					widget = textarea;
				}

				if ("CommandLine".equals(labarr[0][j])) {
					DescCmdPanel CmdPanel = new DescCmdPanel(textarea);
					textarea.setEnabled(Boolean.parseBoolean(labarr[3][j]));
					if (textarea.getText().toString() != "")
						widget = textarea;
					else {
						widget = CmdPanel;
					}

				}
			}
			HorizontalPanel hpanel = new HorizontalPanel();
			Label label = new Label(labarr[1][j]);
			hpanel.add(label);
			hpanel.add(widget);
			boxMap.put(labarr[0][j].toLowerCase(), (DescWidget) widget);
			if ("left".equals(labarr[5][j])) {
				leftPanel.add(hpanel);
			} else {
				rightPanel.add(hpanel);
			}

		}

		//Bind type box selection event 
		DescWidget type = boxMap.get("type");

		if(type instanceof DescListBox)
		{	
			final DescListBox typeBox = (DescListBox)type;
			final String selectedValue = typeBox.getItemText(typeBox.getSelectedIndex()); //Init tensorflow mode selection box
			if("Tensorflow".equals(selectedValue))
				rightPanel.getWidget(1).setVisible(true);
			else
				rightPanel.getWidget(1).setVisible(false);
			typeBox.addChangeHandler(new ChangeHandler(){

				@Override
				public void onChange(ChangeEvent event) {
					// TODO Auto-generated method stub
					if("Tensorflow".equals(typeBox.getItemText(typeBox.getSelectedIndex())))
					{
						rightPanel.getWidget(1).setVisible(true);
					}else{
						rightPanel.getWidget(1).setVisible(false);
					}
				}

			});

		}

	}

	public void setText(String labname, String value) {
		labname = labname.toLowerCase();
		DescWidget widget = boxMap.get(labname);

		if (widget == null) {
			logger.info(labname + ", widget is null");
			return;
		} else {
			logger.info(labname + "," + value);
		}

		widget.setValue(value);
	}

	public String getText(String labname) {
		labname = labname.toLowerCase();
		DescWidget widget = boxMap.get(labname);
		if (widget == null) {
			logger.info(labname + ", widget is null");
			return null;
		}

		return widget.getValue();
	}

	public String DatasetToString() {
		StringBuffer describe = new StringBuffer("<Dataset>\n");
		for (String str : labarr[0]) {
			describe.append("\t<" + str + ">");
			describe.append(getText(str.toLowerCase()));
			describe.append("</" + str + ">\n");
		}
		describe.append("</Dataset>");
		return describe.toString();
	}

	public String ProgramToString() {
		StringBuffer describe = new StringBuffer("<Program>\n");
		for (String str : labarr[0]) {
			describe.append("\t<" + str + ">");
			describe.append(getText(str.toLowerCase()));
			describe.append("</" + str + ">\n");
		}
		describe.append("</Program>");
		return describe.toString();
	}

	public Program asProgram(Program program) {
		final String name = this.getText("Name".toLowerCase());
		final String cate_str = this.getText("Category".toLowerCase());
		final String type = this.getText("Type".toLowerCase());
		final boolean programable;
		final boolean isdeterministic;
		final String description = this.getText("Description".toLowerCase());
		final String version = this.getText("Version".toLowerCase());
		final String create_date = this.getText("CreateDate".toLowerCase());
		final String owner = this.getText("Owner".toLowerCase());
		final String commandline = this.getText("CommandLine".toLowerCase());
		String tensorflowMode = null;

		if("Tensorflow".equals(type))
		{
			tensorflowMode= this.getText("TensorflowMode".toLowerCase());
		}

		if(Constants.studioUIMsg.yes().equals( this.getText("Programable") )){
			programable = true;
		}else
			programable = false;
		if(Constants.studioUIMsg.yes().equals(this.getText("Isdeterministic".toLowerCase())))
			isdeterministic = true;
		else
			isdeterministic = false;

		program.setName(name);
		program.setCategory(cate_str);
		program.setType(type);
		program.setProgramable(programable);
		program.setIsdeterministic(isdeterministic);
		program.setDescription(description);
		program.setVersion(version);
		program.setCreatedate(create_date);
		program.setOwner(owner);
		program.setCommandline(commandline);
		program.setTensorflowMode(tensorflowMode);
		return program;
	}

	public void syncDataset(Dataset dataset){
		this.setText("Name", dataset.getName() );

	}

	public Dataset asDataset(Dataset dataset) {
		dataset.setName(this.getText("Name".toLowerCase()));
		dataset.setCategory(this.getText("Category".toLowerCase()));
		dataset.setStoretype("");
		dataset.setContenttype(this.getText("DataType".toLowerCase()));
		dataset.setDescription(this.getText("Description".toLowerCase()));
		dataset.setVersion(this.getText("Version".toLowerCase()));
		dataset.setCreatedate(this.getText("CreateDate".toLowerCase()));
		dataset.setOwner(this.getText("Owner".toLowerCase()));

		return dataset;
	}
}
