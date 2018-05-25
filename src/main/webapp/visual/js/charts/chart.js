/**
 * Draw Bar Chart
 * 
 * @param id  draw area id
 */
function drawBarChart(id) {
	var barChart = echarts.init(document
			.getElementById(id));
	barChart.showLoading({
		text : 'Loading...'
	});
	var categories = [];
	var values = [];
	var url_at = getAppPath()+"/restfulapi/visual/charts/drawBar";
	var groupByCol = $("#group-select").val();
	var valueCol = $("#value-select").val();
	var aggCol = $("#aggre-select").val();
	var logScale = $("#value-logchk").prop("checked");
	var params = {
			filePath : path,
			dataType : type,
			columns : columns,
			groupByCol : groupByCol,
			valueCol : valueCol,
			aggCol : aggCol,
			logScale : logScale
	}
	var dataStyle = {   
			normal: {  
				color: function(params)   
				{  
					// build a color map as your need.              
					var colorList = [              
					                 '#C23531','#2F4554','#91C7AE','#D48265','#61A0A8',  
					                 '#6E7074','#BDA29A','#CA8622','#A7CACF','#60C0DD',             
					                 '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'                       
					                 ];  
					return colorList[params.dataIndex]             
				},  

			}  
	};  
	$.post(url_at, params, function(data, status) {
		if(data.values == null){
			alert("The value column of the bar chart must be numerical.");
		}else if(data.categories == null){
			alert("The group column of the bar chart exceeded the size of the display.");
		}
		categories = data.categories;
		values = data.values;
		var option = {
				tooltip : {
					trigger : 'axis',
					axisPointer : {
						type : 'shadow' 
					}
				},
				xAxis : [ {
					type : 'category',
					data : categories
				} ],
				yAxis : [ {
					type : 'value'
				} ],
				series : [ {
					'name' : aggCol,
					'type' : 'bar',
					'data' : values,
					'itemStyle' :dataStyle
				} ]
		};
		barChart.hideLoading();
		barChart.clear();
		barChart.setOption(option);
	});
}

/**
 * Draw Line Chart
 * 
 * @param draw area id
 */
function drawLineChart(id) {
	var lineChart = echarts.init(document
			.getElementById(id));
	lineChart.showLoading({
		text : 'Loading...'
	});
	var categories = [];
	var values = [];
	var url_at = getAppPath()+"/restfulapi/visual/charts/drawLine";
	var selectedColumns = $("#group-select").val();
	var colorList = [              
	                 '#C23531','#2F4554','#91C7AE','#D48265','#61A0A8',  
	                 '#6E7074','#BDA29A','#CA8622','#A7CACF','#60C0DD',             
	                 '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'                       
	                 ];  
	var params = {
			filePath : path,
			dataType : type,
			columns : columns,
			selectedCol : selectedColumns
	}
	$.post(url_at, params, function(data, status) {
		var datas = eval(data);
		if (data.length == 0) {
			alert("Sorry, the line chart can only show numerical data");
		}
		categories = datas[0].categories;
		var option = {
				tooltip : {
					trigger : 'axis',
					axisPointer : {
						type : 'shadow' 
					}
				},
				xAxis : [ {
					type : 'category',
					data : categories
				} ],
				yAxis : [ {
					type : 'value'
				} ],
				series : function(){
					var series = [];
					var k = datas.length;
					if(datas.length > colorList.length)
						k = colorList.length
						for( var i=0; i<k;i++)
						{
							var item = {
									name : selectedColumns[i],
									type : 'line',
									data : datas[i].values,
									itemStyle : {
										normal : {
											color :  colorList[i]
										}
									}
							}
							series.push(item);
						};
						return series;
				} ()
		};
		lineChart.hideLoading();
		lineChart.clear();
		lineChart.setOption(option);
	});
}

/**
 * Draw Scatter Chart
 * 
 * @param id draw area id
 */
function drawScatterChart(id) {
	var scatterChart = echarts.init(document
			.getElementById(id));
	scatterChart.showLoading({
		text : 'Loading...'
	});
	var values = [];
	var colors = [];
	var min;
	var max;
	var url_at = getAppPath()+"/restfulapi/visual/charts/drawScatter";
	var selectedColumn_x = $("#x-select").val();
	var logScale_x = $("#x-logchk").prop("checked");
	var selectedColumn_y = $("#y-select").val();
	var logScale_y = $("#y-logchk").prop("checked");
	var selectedColumn_c = $("#color-select").val();
	var params = {
			filePath : path,
			dataType : type,
			columns : columns,
			selectedCol_x : selectedColumn_x,
			logScale_x : logScale_x,
			selectedCol_y : selectedColumn_y,
			logScale_y : logScale_y,
			selectedCol_c : selectedColumn_c
	}
	$.post(url_at, params, function(data, status) {
		if(data.values == null){
			alert("The X-axis and Y-axis of the scatter chart must be numerical.");
		}
		values = data.values;
		colors = data.color;
		min = data.min;
		max = data.max;

		//Format the value
		var str = JSON.stringify(values);
		var reg = new RegExp('"',"g");  
		str = str.replace(reg, ""); 
		var reg = new RegExp("'","g");
		str = str.replace(reg, "\"");
		values = JSON.parse(str);

		var option = {
				tooltip : {
					showDelay : 0,
					formatter : function (params) {
			            if (params.value.length > 1) {
			                return params.value[2] + ' :<br/>'
			                + "("+params.value[0] + ','
			                + params.value[1] + ')';
			            }
			        },
			        axisPointer:{
			            show: true,
			            type : 'cross',
			            lineStyle: {
			                type : 'dashed',
			                width : 1
			            }
			        }
				},
				xAxis : [ {
					name: selectedColumn_x
				} ],
				yAxis : [ {
					name: selectedColumn_y
				} ],
				series : [ {
					type : 'scatter',
					data : values
				} ]
		};
		scatterChart.hideLoading();
		scatterChart.clear();
		scatterChart.setOption(option);
		drawVisualMap(scatterChart,colors,min,max)
	});
}
/**
 * Draw frequency bar chart
 * 
 * @param id draw area id
 */
function drawFreqChart(id) {
	var freqChart = echarts.init(document
			.getElementById(id));
	freqChart.showLoading({
		text : 'Loading...'
	});
	var categories = [];
	var values = [];
	var url_at = getAppPath()+"/restfulapi/visual/charts/drawfreqBar";
	var logScale = $("#logScale").prop("checked");
	var bins = $("#bins").val();
	var selectedColumn = $("#data_column_selector").val();
	var params = {
			filePath : path,
			dataType : type,
			columns : columns,
			selectedCol : selectedColumn,
			bins : bins,
			logScale : logScale
	}
	$.post(url_at, params, function(data, status) {
		categories = data.categories;
		values = data.values;
		var option = {
				tooltip : {
					trigger : 'axis',
					axisPointer : {
						type : 'shadow'
					}
				},
				xAxis : [ {
					type : 'category',
					data : categories
				} ],
				yAxis : [ {
					type : 'value'
				} ],
				series : [ {
					'name' : 'frequency',
					'type' : 'bar',
					'data' : values,
					'itemStyle' : {
						normal : {
							color : '#438EB9'
						}
					}
				} ]
		};
		freqChart.hideLoading();
		freqChart.setOption(option);
	});
}