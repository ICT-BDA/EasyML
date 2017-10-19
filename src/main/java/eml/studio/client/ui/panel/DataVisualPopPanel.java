/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import eml.studio.client.rpc.ChartService;
import eml.studio.client.rpc.ChartServiceAsync;
import eml.studio.client.rpc.DataAnalysisService;
import eml.studio.client.rpc.DataAnalysisServiceAsync;
import eml.studio.client.ui.charts.DrawBarChart;
import eml.studio.shared.model.Dataset;
import eml.studio.shared.util.DataFeature;
import eml.studio.shared.util.DataTools;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

/**
 * Data result visualization panel 
 */
public class DataVisualPopPanel extends PopupPanel{

	protected static Logger logger = Logger.getLogger(DataVisualPopPanel.class.getName());
	private final ChartServiceAsync barService = GWT.create(ChartService.class);
	private final DataAnalysisServiceAsync dataAnalysisService = GWT.create(DataAnalysisService.class);
	private final Label desc = new Label("Data Visualization");
	private HTML closeButton = new HTML("X");
	private static final  int LIMIT_LINE = 1000000;

	private  Label rowLbl ;
	private  Label colLbl ;

	private DataGrid<JSONObject> dataGrid;
	private SimplePager pager;

	private  ListBox colListBox;
	private  Label meanLbl ;
	private  Label medianLbl ;
	private  Label minLbl ;
	private  Label maxLbl ;
	private  Label stdLbl ;
	private  Label uniqLbl ;
	private  Label missingLbl ;
	private  Label feaTypeLbl ;
	private Grid advancedOptions;

	private  CheckBox freLogChkbox;
	private  TextBox bins ;
	private List<String> colNames;  //Data column name
	private List<Integer> colTypes;  //Data column type (numeric or string)

	private final String BIN_SIZE = "10";
	private String filePath;   //Data path(HDFS)
	private String selectedColumn;  //The selected column
	private Dataset dataset; //Dataset for visualization
	private  int COL_WIDTH = 75;

	public DataVisualPopPanel(String filePath,Dataset dataset)
	{
		this.filePath =filePath;
		this.dataset = dataset;
		init();
	}
	/**
	 * Initialization
	 * 
	 */
	public void init()
	{
		//Get data rows and columns
		colNames = new ArrayList<String>();
		colTypes = new ArrayList<Integer>();
		dataAnalysisService.getDataColumn(filePath, dataset.getContenttype(), new AsyncCallback<LinkedHashMap<String,Integer>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				logger.info("Get data column error:  "+caught.getMessage());
			}

			@Override
			public void onSuccess(LinkedHashMap<String, Integer> result) {
				// TODO Auto-generated method stub
				Iterator<Entry<String, Integer>>  it = result.entrySet().iterator();    
				while(it.hasNext())    
				{    
					Entry<String,Integer> column = it.next();    
					colNames.add(column.getKey());
					colTypes.add(column.getValue());
				}    
				logger.info("Col size ="+colNames.size());
				initUI();
				bindEvent();
			}
		} );
	}
	/**
	 * UI Initialization
	 */
	public void initUI()
	{
		this.setSize("900px", "500px");

		//Dialog title
		closeButton.setSize("10px", "10px");
		closeButton.setStyleName("closebtn");


		//Display the number of rows and columns of the data
		HorizontalPanel  rowColPanel = new HorizontalPanel();
		Label row = new Label("Rows：");
		rowLbl = new Label();
		setDataRows(filePath);
		Label cols = new Label("Cols：");
		colLbl = new Label();
		colLbl.setText(String.valueOf(colNames.size()));
		rowColPanel.add(row);
		rowColPanel.add(rowLbl);
		rowColPanel.add(cols);
		rowColPanel.add(colLbl);

		//Data content split panel
		HorizontalSplitPanel contentPanel = new HorizontalSplitPanel();
		contentPanel.setPixelSize(900, 400);

		//Data table in the left
		buildCellTable();
		VerticalPanel dataPanel = new VerticalPanel();
		dataPanel.add(dataGrid);
		dataPanel.add(pager);

		//Data feature panel in the right
		VerticalPanel featurePanel = new VerticalPanel();
		featurePanel.setSize("400px", "400px");

		//Column selection panel in the top right
		HorizontalPanel colListPanel = new HorizontalPanel();
		colListBox = new ListBox();
		colListBox.addItem("----");
		for(int i=0; i<colNames.size(); i++)
			colListBox.addItem(colNames.get(i));
		colListPanel.add(new Label("Columns Selection: "));
		colListPanel.add(colListBox);

		//Data feature statistics in the right middle
		advancedOptions = new Grid(8, 2);
		advancedOptions.setCellSpacing(6);
		Label mean = new Label("Mean");
		advancedOptions.setHTML(0, 0, mean.getText());
		meanLbl = new Label("-----");
		advancedOptions.setWidget(0, 1, meanLbl);
		Label median = new Label("Median");
		advancedOptions.setHTML(1, 0, median.getText());
		medianLbl= new Label("-----");
		advancedOptions.setWidget(1, 1, medianLbl);
		Label min = new Label("Min");
		minLbl= new Label("-----");
		advancedOptions.setHTML(2, 0, min.getText());
		advancedOptions.setWidget(2, 1, minLbl);
		Label max = new Label("Max");
		maxLbl= new Label("-----");
		advancedOptions.setHTML(3, 0, max.getText());
		advancedOptions.setWidget(3, 1, maxLbl);
		Label std = new Label("Standard Deviation");
		advancedOptions.setHTML(4, 0, std.getText());
		stdLbl= new Label("-----");
		advancedOptions.setWidget(4, 1, stdLbl);
		Label uniq = new Label("Unique Values");
		advancedOptions.setHTML(5, 0, uniq.getText());
		uniqLbl= new Label("-----");
		advancedOptions.setWidget(5, 1, uniqLbl);
		Label missing = new Label("Missing Values");
		missingLbl= new Label("-----");
		advancedOptions.setHTML(6, 0, missing.getText());
		advancedOptions.setWidget(6, 1, missingLbl);
		Label feaType = new Label("Feature Types");
		advancedOptions.setHTML(7, 0, feaType.getText());
		feaTypeLbl = new Label("-----");
		advancedOptions.setWidget(7, 1, feaTypeLbl);

		DisclosurePanel staticsDisclosure = new DisclosurePanel("Statics");
		staticsDisclosure.setAnimationEnabled(true);
		staticsDisclosure.setOpen(true);
		staticsDisclosure.ensureDebugId("staticsDisclosurePanel");
		staticsDisclosure.setContent(advancedOptions);

		//Data feature visualization in the right bottom
		VerticalPanel chartPanel = new VerticalPanel();
		final VerticalPanel chart1Panel = new VerticalPanel();
		chart1Panel.addStyleName("chartPanel");
		chart1Panel.getElement().setAttribute("id", "datachart");
		freLogChkbox = new CheckBox("frequency log scale");
		Label binLbl = new Label("bins");
		bins = new TextBox();
		bins.setText(BIN_SIZE);
		bins.setWidth("120px");

		chartPanel.add(chart1Panel);
		chartPanel.add(freLogChkbox);
		chartPanel.add(binLbl);
		chartPanel.add(bins);
		freLogChkbox.setEnabled(false);
		bins.setEnabled(false);
		DisclosurePanel chartDisclosure = new DisclosurePanel("Visualizations");
		chartDisclosure.setOpen(true);
		chartDisclosure.setAnimationEnabled(true);
		chartDisclosure.ensureDebugId("chartDisclosurePanel");
		chartDisclosure.setContent(chartPanel);

		featurePanel.add(colListPanel);
		featurePanel.add(staticsDisclosure);
		featurePanel.add(chartDisclosure);
		
		contentPanel.add(dataPanel);
		contentPanel.add(featurePanel);

		//Overall arrangement
		VerticalPanel topPanel = new VerticalPanel();
		topPanel.add(closeButton);
		topPanel.setCellHeight(closeButton, "13px");
		topPanel.setStyleName("vpanel");
		desc.setStyleName("popupTitle");
		topPanel.add(desc);
		topPanel.setCellHeight(desc, "30px");
		topPanel.add(rowColPanel);
		topPanel.add(contentPanel);

		this.setGlassEnabled(true);
		this.setModal(true);
		this.add(topPanel);
		this.center();
		this.setStyleName("loading-panel");
	}

	/**
	 * Event binding
	 */
	public void bindEvent()
	{
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				DataVisualPopPanel.this.hide();
			}
		});

		colListBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				bins.setText(BIN_SIZE);
				freLogChkbox.setValue(false);
				int index = colListBox.getSelectedIndex();
				if(index != 0)
				{
					selectedColumn = colListBox.getItemText(index);
					wrapDataStatics(selectedColumn);
					drawBarCharts(selectedColumn);
				}
				else
				{
					freLogChkbox.setEnabled(false);
					bins.setEnabled(false);
					meanLbl.setText("-----");
					medianLbl.setText("-----");
					minLbl.setText("-----");
					maxLbl.setText("-----");
					stdLbl.setText("-----");
					uniqLbl.setText("-----");
					missingLbl.setText("-----");
					feaTypeLbl.setText("-----");
				}
			}
		});

		bins.addKeyboardListener(new BinValueChangeListener());


		freLogChkbox.addValueChangeHandler(new ValueChangeHandler<Boolean>(){

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				// TODO Auto-generated method stub

				drawBarCharts(selectedColumn);
			}

		});
	}

	@Override
	public void center(){
		super.center();
	}

	/**
	 * Draw bar charts
	 * 
	 * @param  selectedColumn select column
	 */
	private void drawBarCharts(String selectedColumn)
	{
		barService.barChartServer(filePath,dataset.getContenttype(),colNames,selectedColumn, Integer.valueOf(bins.getText()),freLogChkbox.getValue(),new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				logger.info(caught.toString());
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				DrawBarChart.drawBarChart("datachart",result);
				freLogChkbox.setEnabled(true);
				bins.setEnabled(true);
			}
		});
	}

	/**
	 * Wrap data statics feature
	 * 
	 * @param selectedColumn   select column
	 */
	private void wrapDataStatics(String selectedColumn)
	{
		dataAnalysisService.extDataFeature(filePath,dataset.getContenttype(),colNames,selectedColumn,new AsyncCallback<DataFeature>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				logger.info("Wrap data statics feature error!");
			}

			@Override
			public void onSuccess(DataFeature feature) {
				// TODO Auto-generated method stub
				if(feature.isNumberic())
				{
					meanLbl.setText(String.valueOf(feature.getMean())); 
					medianLbl.setText(String.valueOf(feature.getMedian())); 
					minLbl.setText(String.valueOf(feature.getMin()));
					maxLbl.setText(String.valueOf(feature.getMax())); 
					stdLbl.setText(String.valueOf(feature.getStandDeviation())); 
					uniqLbl.setText(String.valueOf(feature.getUniqueValues())); 
					missingLbl.setText(String.valueOf(feature.getMissingValues())); 
					feaTypeLbl.setText("Numberic"); 
				}else{
					meanLbl.setText("-----");
					medianLbl.setText("-----");
					minLbl.setText("-----");
					maxLbl.setText("-----");
					stdLbl.setText("-----");
					uniqLbl.setText(String.valueOf(feature.getUniqueValues())); 
					missingLbl.setText(String.valueOf(feature.getMissingValues())); 
					feaTypeLbl.setText("String"); 
				}
			}

		});
	}

	/**
	 * Build dynamic cell box by data
	 * 
	 */
	public void buildCellTable()
	{
		dataGrid = new DataGrid<JSONObject>();

		//Set the grid size
		int totalWidth = COL_WIDTH*colNames.size();
		if(totalWidth < 500)
			totalWidth = 500;
		dataGrid.setSize(totalWidth+"px", "350px");
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label("Data is empty"));

		//Add data
		dataGrid.setVisibleRange(0, 50);
		if(!rowLbl.getText().equals(""))
		{
			int rowCount = Integer.valueOf(rowLbl.getText());
			if(rowCount > LIMIT_LINE)
				dataGrid.setRowCount(LIMIT_LINE);
			else
				dataGrid.setRowCount(rowCount);
		}

		AsyncDataProvider<JSONObject> dataProvider = new AsyncDataProvider<JSONObject>() {
			@Override
			protected void onRangeChanged(HasData<JSONObject> display) {
				final Range range = display.getVisibleRange();
				final int start = range.getStart();
				dataAnalysisService.pageData(filePath, dataset.getContenttype(), colNames, start,  range.getLength(), new AsyncCallback<List<String>> (){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						logger.info("Get range data error:" + caught.getMessage());
					}

					@Override
					public void onSuccess(List<String> result) {
						// TODO Auto-generated method stub
						if(result.size()==0)
							logger.info("cxj");
						List<JSONObject> jsonObjs = new ArrayList<JSONObject>();
						for(int i=0;i<result.size();i++)
						{
							JSONValue value = JSONParser.parse(result.get(i));
							jsonObjs.add(value.isObject());
						}
						dataGrid.setRowData(start, jsonObjs);
					}

				});

			}
		};

		dataProvider.addDataDisplay(dataGrid);

		//Add column
		for(int i = 0;i<colNames.size();i++)
		{
			final String colName = colNames.get(i);
			int type = colTypes.get(i);
			if(type == 0)
			{
				Column<JSONObject, String>  column = new Column<JSONObject, String>(new TextCell()) {
					@Override
					public String getValue(JSONObject object) {
						try {
							return object.get(colName).toString();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return "";
						}
					}
				};
				dataGrid.addColumn(column, colName);
				dataGrid.setColumnWidth(column, COL_WIDTH, Unit.PCT);
			}
			else
			{
				Column<JSONObject, Number> column = new Column<JSONObject, Number>(new NumberCell()) {
					@Override
					public Number getValue(JSONObject object) {
						try {
							return    Double.valueOf(DataTools.getJSONValue(object.get(colName).toString()));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return -1;
						}
					}
				};
				dataGrid.addColumn(column, colName);
				dataGrid.setColumnWidth(column, COL_WIDTH, Unit.PCT);
			}
		}

		//Add a paging
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(dataGrid);

	}

	/**
	 * Bin value change handler
	 */
	private class BinValueChangeListener extends KeyboardListenerAdapter {
		@Override
		public void onKeyPress(Widget sender, char key, int mods) {
			if (KeyboardListener.KEY_ENTER == key)
			{
				if(!DataTools.isPosInteger(bins.getText()))
					Window.alert("Bin size should be a positive integer, please fill again!");
				else
					drawBarCharts(selectedColumn);
			}
		}
	}
	/**
	 * Get data row number
	 * 
	 * @param filePath  data path
	 */
	private void setDataRows(String filePath)
	{
		dataAnalysisService.getDataRows(filePath, dataset.getContenttype(), new AsyncCallback<Integer>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				logger.info("Get data row error:"+caught.getMessage());
				rowLbl.setText("0");
			}

			@Override
			public void onSuccess(Integer result) {
				// TODO Auto-generated method stub
				rowLbl.setText(String.valueOf(result));
				if(dataGrid!=null)
				{
					if(result>LIMIT_LINE)
						dataGrid.setRowCount(LIMIT_LINE);
					else
						dataGrid.setRowCount(result);
				}
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
