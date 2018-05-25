/**
 * Control parser
 * Parse and draw the control from control meta string returned from the server
 * 
 * @param object  Control json string
 * @param id   Draw parameter area id
 */
function controlParser(object,id) {
	$("#"+id).empty();
	var controls = object.controls;
	var selCount = 0;
	for (var i = 0; i < controls.length; i++) {
		var control = controls[i];
		var control_type = control.control_type;
		var control_id = control.id;
		var control_value = control.value;
		var control_lable = control.label;
		var control_change = control.onchange;
		switch(control_type)
		{
		case "label":
			$("<label>",{
				id: control_id,
				text: control_value
			}).appendTo($("#"+id));
			break;
		case "select":
			var multiSel = control.multiple;
			if(multiSel=="true")
			{
				$("<select>",{
					id: control_id,
					class: "form-control",
					multiple: "multiple",
					onchange: control_change
				}).appendTo($("#"+id));
			}else{
				$("<select>",{
					id: control_id,
					class: "form-control",
					onchange: control_change
				}).appendTo($("#"+id));
			}

			control_value = control_value.split(",");
			for (var j = 0; j < control_value.length; j++) {
				$("<option>",{
					value: control_value[j],
					text: control_value[j]
				}).appendTo($("#"+control_id));
			}
			$("#"+control_id).prop('selectedIndex', selCount);
			selCount = selCount +1;
			break;
		case "checkbox":
			$("<div>",{
				id: "checkbox" + i,
				class: "checkbox"
			}).appendTo($("#"+id));
			$("<label>",{
				id: "checklabel"+i
			}).appendTo($("#checkbox" + i));
			$("<input>",{
				id: control_id,
				type: "checkbox",
				name: "form-field-checkbox",
				class: "ace ace-checkbox-2",
				onclick: control.onclick
			}).appendTo($("#checklabel" + i));
			$("<span>",{
				id: control_id,
				class: "lbl",
				text:" " + control_lable
			}).appendTo($("#checklabel" + i));
			break;
		}
	}
}

/**
 * Draw Color Visual Map
 * 
 * @param chart
 * @param color
 * @param min
 * @param max
 */
function drawVisualMap(chart, color, min, max) {
	if(color == null) {
		var option = {
				visualMap: {
					min: min,
					max: max,
					dimension: 2,
					orient: 'vertical',
					right: 10,
					top: 'center',
					text: ['HIGH', 'LOW'],
					calculable: true,
					inRange: {
						color: ['#FF4500', '#6C7B8B']
					}
				}	
		}
		chart.setOption(option);
	}else {
		var option = {
				visualMap: {
					type: 'piecewise',
					dimension: 2,
					categories: color,
					left: 0,
					top: 80,
					calculable: true,
					precision: 0.1,
					textGap: 10,
					itemGap: 3,
					itemHeight: 10,
					inRange: {
						color: ['#f2c31a', '#24b7f2','#FF4500','#6C7B8B','#9932CC']
					},
					outOfRange: {
						color: '#555'
					}
				}	
		}
		chart.setOption(option);
	}
}