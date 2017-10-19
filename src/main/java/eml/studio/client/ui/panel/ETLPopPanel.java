/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import eml.studio.client.rpc.ETLService;
import eml.studio.client.rpc.ETLServiceAsync;
import eml.studio.client.ui.panel.Grid.SqlETLGrid;
import eml.studio.client.ui.panel.component.DescListBox;
import eml.studio.client.ui.widget.command.Parameter;
import eml.studio.client.ui.widget.panel.ParameterPanel;
import eml.studio.client.ui.widget.program.CommonProgramWidget;
import eml.studio.client.ui.widget.program.ProgramWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.List;
import java.util.logging.Logger;

import java.util.ArrayList;

/**
 * 
 * ETL Connected Information Configuration Panel
 *
 */
public class ETLPopPanel extends BasePanel {
	private static final Logger logger = Logger.getLogger(ETLPopPanel.class.getName());
	//button
	protected Button submitBtn = new Button("OK");
	protected Button connectBtn = new Button("Connect");

	protected HorizontalPanel northPanel = new HorizontalPanel();
	protected HorizontalPanel midPanel = new HorizontalPanel();
	protected HorizontalPanel southPanel = new HorizontalPanel();
	protected VerticalPanel mid_westPanel = new VerticalPanel();
	protected VerticalPanel mid_eastPanel = new VerticalPanel();

	protected DescListBox category = new DescListBox();
	protected SqlETLGrid sqlGrid = new SqlETLGrid();

	protected Grid rightGrid;
	protected DescListBox tableLB;
	protected DescListBox columnLB;
	protected DescListBox formatLB;
	protected ParameterPanel panel;

	protected ProgramWidget widget;

	protected ETLServiceAsync svc = GWT.create(ETLService.class);

	public ETLPopPanel(ProgramWidget widget) {
		this.widget = widget;
		init_UI();
		init_function();
	}
	/**
	 * Init UI
	 */
	public void init_UI(){
		this.addStyleName("bda-etlpanel");

		//north
		northPanel.add(new Label("Type: "));
		category.addItem("Sql");
		category.addItem("Hive");
		category.setVisibleItemCount(1);
		northPanel.add(category);

		//mid
		init_rightGrid();
		mid_eastPanel.add(sqlGrid);
		mid_westPanel.add(rightGrid);
		midPanel.add(mid_eastPanel.asWidget());
		midPanel.add(mid_westPanel.asWidget());

		//south
		submitBtn.removeStyleName("gwt-Button");
		connectBtn.removeStyleName("gwt-Button");
		submitBtn.addStyleName("button-style");
		connectBtn.addStyleName("button-style");
		connectBtn.getElement().getStyle().setMarginLeft(310, Style.Unit.PX);
		verticalPanel.removeStyleName("vpanel");

		verticalPanel.addStyleName("bda-etlpanel-panel");
		southPanel.setStyleName("bda-etlpanel-south");
		northPanel.setStyleName("bda-etlpanel-north");
		midPanel.setStyleName("bda-etlpanel-mid");
		mid_eastPanel.setStyleName("bda-etlpanel-mideast");
		mid_westPanel.setStyleName("bda-etlpanel-midwest");
		southPanel.add(connectBtn);
		southPanel.add(submitBtn);

		verticalPanel.add(northPanel);
		verticalPanel.add(midPanel);
		verticalPanel.add(southPanel);
	}
	/**
	 * Init button bind event
	 */
	public void init_function(){
		addSubmitHandler();
		addConnectHandeler();
	}

	/**
	 * Bind event to submit button
	 */
	private void addSubmitHandler() {

		submitBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				logger.info(widget.getProgram().getPath());
				if(tableLB.getItemCount()==0) {Window.alert("Please make sure the database is properly connected！");return;}
				if(panel!= null) setParameterPanel(panel);
				ETLPopPanel.this.hide();
			}
		});
	}

	/**
	 * Bind event to connect button
	 */
	private void addConnectHandeler(){
		connectBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				tableLB.clear();
				columnLB.clear();
				connectBtn.setEnabled(false);
				gettable();
			}
		});
	}

	/**
	 * Init etl program widget' right grid panel
	 */
	public void init_rightGrid(){
		Label table = new Label("table");
		Label column = new Label("column");
		Label format = new Label("format");
		rightGrid = new Grid(3,2);
		rightGrid.setWidget(1,0,table);
		tableLB = new DescListBox();
		tableLB.setVisibleItemCount(1);
		tableLB.setStyleName("bda-etlpanel-listbox");
		rightGrid.setWidget(1,1,tableLB);

		rightGrid.setWidget(2,0,column);
		columnLB = new DescListBox();
		columnLB.setMultipleSelect(true);
		columnLB.setVisibleItemCount(2);
		columnLB.setStyleName("bda-etlpanel-Multilistbox");
		rightGrid.setWidget(2,1,columnLB);

		rightGrid.setWidget(0,0,format);
		formatLB = new DescListBox();
		formatLB.addItem("json");
		formatLB.addItem("tsv");
		formatLB.addItem("csv");
		formatLB.addItem("parquet");
		formatLB.setVisibleItemCount(1);
		formatLB.setStyleName("bda-etlpanel-listbox");
		rightGrid.setWidget(0,1,formatLB);
		mid_westPanel.add(rightGrid);

		tableLB.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent changeEvent) {
				getColumn();
			}
		});
	}


	public void setETLPanel(ParameterPanel panel) {
		if (panel == null)
			return;
		CommonProgramWidget w = (CommonProgramWidget) widget;
		List<Parameter> params = w.getProgramConf().getParameters();
		if(w.getProgram().getName().contains("ive")){
			category.setValue("Hive");
		}
		sqlGrid.setUrlTB(params.get(0).getParamValue());
		sqlGrid.setUserTB(params.get(1).getParamValue());
		sqlGrid.setPasswordTB(params.get(2).getParamValue());
		this.panel = panel;
	}

	public void setParameterPanel(ParameterPanel panel){
		if (panel == null)
			return;
		CommonProgramWidget w = (CommonProgramWidget) widget;
		List<Parameter> params = w.getProgramConf().getParameters();
		String columnStr = "";
		for(int i : columnLB.getSelectedItems()){
			columnStr += columnLB.getValue(i)+",";
		}
		columnStr = columnStr.substring(0,columnStr.length()-1);
		params.get(0).setParamValue(sqlGrid.getUrlTB().getValue().toString());
		params.get(1).setParamValue(sqlGrid.getUserTB().getValue().toString());
		params.get(2).setParamValue(sqlGrid.getPasswordTB().getValue().toString());
		params.get(3).setParamValue(tableLB.getValue().toString());
		params.get(4).setParamValue(columnStr);
		logger.info("urlLB: "+sqlGrid.getUrlTB().getValue().toString());
		logger.info("userLB: "+sqlGrid.getUserTB().getValue().toString());
		logger.info("passwordLB: "+sqlGrid.getPasswordTB().getValue().toString());
		logger.info("formateLB: "+formatLB.getValue().toString());
		logger.info("columnLB: "+columnStr);
		logger.info("tableLB: "+tableLB.getValue().toString());
		params.get(5).setParamValue(formatLB.getValue().toString());
		((TextBox)panel.getParamBoxs().get(0)).setText(sqlGrid.getUrlTB().getValue());
		((TextBox)panel.getParamBoxs().get(1)).setText(sqlGrid.getUserTB().getValue());
		((TextBox)panel.getParamBoxs().get(2)).setText(sqlGrid.getPasswordTB().getValue());
		((TextBox)panel.getParamBoxs().get(3)).setText(tableLB.getValue());
		((TextBox)panel.getParamBoxs().get(4)).setText(columnStr);
		((TextBox)panel.getParamBoxs().get(5)).setText(formatLB.getValue());
		this.panel = panel;
	}
	/**
	 * Get connected database table column
	 */
	public void getColumn(){
		columnLB.clear();
		if(category.getValue().equals("Sql")){
			svc.SqlETLGetColumns(sqlGrid.getUrlTB().getValue(),sqlGrid.getUserTB().getValue(),
					sqlGrid.getPasswordTB().getValue(),tableLB.getValue(),new AsyncCallback<ArrayList<String>>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Loading "+tableLB.getValue()+" content failed");
				}

				@Override
				public void onSuccess(ArrayList<String> result) {
					for(String e:result){
						logger.info("column: "+result);
						columnLB.addItem(e);
					}
				}
			});
		}else{
			svc.HiveETLGetColumns(sqlGrid.getUrlTB().getValue(),sqlGrid.getUserTB().getValue(),
					sqlGrid.getPasswordTB().getValue(),tableLB.getValue(),new AsyncCallback<ArrayList<String>>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Loading "+tableLB.getValue()+" content failed");
				}

				@Override
				public void onSuccess(ArrayList<String> result) {
					for(String e:result){
						logger.info("column "+result);
						columnLB.addItem(e);
					}
				}
			});
		}
	}

	/**
	 * Get connected database table
	 */
	public void gettable(){
		if(category.getValue().equals("Sql")){
			svc.SqlETLGetTables(sqlGrid.getUrlTB().getValue(),sqlGrid.getUserTB().getValue(),
					sqlGrid.getPasswordTB().getValue(),new AsyncCallback<ArrayList<String>>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Loading table failed");
				}

				@Override
				public void onSuccess(ArrayList<String> result) {
					for(String e:result){
						logger.info("table: "+result);
						if(e.contains("Database connection failed")) {Window.alert(e); connectBtn.setEnabled(true); return;}
						tableLB.addItem(e);
					}
					connectBtn.setEnabled(true);
					getColumn();
				}
			});
		}else{
			svc.HiveETLGetTables(sqlGrid.getUrlTB().getValue(),sqlGrid.getUserTB().getValue(),
					sqlGrid.getPasswordTB().getValue(),new AsyncCallback<ArrayList<String>>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Loading table failed");
				}

				@Override
				public void onSuccess(ArrayList<String> result) {
					for(String e:result){
						logger.info("table: "+result);
						if(e.contains("Database connection failed")) {Window.alert(e); connectBtn.setEnabled(true); return;}
						tableLB.addItem(e);
					}
					connectBtn.setEnabled(true);
					getColumn();
				}
			});
		}
	}
	public ParameterPanel getPanel() {
		return panel;
	}

	public DescListBox getColumnLB() {
		return columnLB;
	}

	public void setColumnLB(DescListBox columnLB) {
		this.columnLB = columnLB;
	}

	public DescListBox getTableLB() {
		return tableLB;
	}

	public void setTableLB(DescListBox tableLB) {
		this.tableLB = tableLB;
	}

	public DescListBox getFormatLB() {
		return formatLB;
	}

	public void setFormatLB(DescListBox formatLB) {
		this.formatLB = formatLB;
	}

}
