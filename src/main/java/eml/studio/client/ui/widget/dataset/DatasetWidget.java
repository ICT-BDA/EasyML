/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.dataset;

import eml.studio.client.controller.DiagramController;
import eml.studio.client.ui.property.DatasetPropertyTable;
import eml.studio.client.ui.widget.BaseWidget;
import eml.studio.client.ui.widget.shape.OutNodeShape;
import eml.studio.shared.model.Dataset;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.widgetideas.graphics.client.Color;

/**
 * Defines Data set widget in design panel
 */
public class DatasetWidget extends BaseWidget {

	protected Label nameLabel = new Label();

	private Dataset dataset;

	private DatasetPropertyTable ptable;
	public DatasetWidget(){
		super("","");
	}
	public DatasetWidget(Dataset dataset, String widget_uuid) {
		super(dataset.getName(), widget_uuid);
		outNodeShapes.add(new OutNodeShape(this, 0));
		label.setText("");
		nameLabel.setText(dataset.getName());
		label.setStyleName("filebackground");
		label.setPixelSize(30, 42);
		nameLabel.setWidth("100%");
		abspanel.add(nameLabel, 30, 0);
		abspanel.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		canvas.setPixelWidth(30);
		canvas.setPixelHeight(45);
		canvas.setCoordSize(30, 45);
		this.dataset = dataset;
		ptable = new DatasetPropertyTable(dataset);
	}

	@Override
	protected void init() {
		clientWidth = DOM.getElementPropertyInt(label.getElement(), "clientHeight");
		clientWidth = DOM.getElementPropertyInt(label.getElement(), "clientHeight");
		clientHeight = DOM.getElementPropertyInt(label.getElement(), "clientHeight");
		offsetWidth = label.getOffsetWidth();
		offsetHeight = label.getOffsetHeight();
		offsetTop = DOM.getElementPropertyInt(label.getElement(), "offsetTop");
		offsetLeft = DOM.getElementPropertyInt(label.getElement(), "offsetLeft");

		boder = (offsetHeight - clientHeight) / 2;

		custom();

		this.setPixelSize((int) offsetWidth + offsetLeft * 2, (int) offsetHeight
				+ offsetTop * 2);
	}

	@Override
	protected void custom() {
		OutNodeShape nodeShape = outNodeShapes.get(0);
		nodeShape.setLeftRelative(offsetLeft + offsetWidth / 2);
		nodeShape.setTopRelative(offsetTop + offsetHeight - boder / 2);
		nodeShape.setFileId(dataset.getId());
		canvas.beginPath();
		canvas.arc(nodeShape.getLeftRelative(), nodeShape.getTopRelative(), 3, 0,
				Math.PI * 2.0, true);
		canvas.setStrokeStyle(Color.BLUE);
		canvas.stroke();
		canvas.setFillStyle(Color.GREEN);
		canvas.fill();
		canvas.closePath();
	}

	public DatasetPropertyTable getPropertyTable() {
		return ptable;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {

		super.onMouseDown(event);
		if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
			event.stopPropagation();
			event.preventDefault();
		}

		DiagramController con =this.getController();
		con.setPropTable(ptable);
	}
	@Override
	public void onSelected() {
		selected = true;
		label.removeStyleName("deselected");
		label.addStyleName("selected");
	}

	@Override
	public void onDeSelected() {
		selected = false;
		label.removeStyleName("selected");
		label.addStyleName("deselected");
	}

	public Label getNameLabel() {
		return nameLabel;
	}

	public void setNameLabel(Label nameLabel) {
		this.nameLabel = nameLabel;
	}

	public DatasetPropertyTable getPtable() {
		return ptable;
	}

	public void setPtable(DatasetPropertyTable ptable) {
		this.ptable = ptable;
	}

	@Override
	public DatasetWidget clone(String Id){
		Window.alert("dataSetClone");
		DatasetWidget newWidget = new DatasetWidget(this.dataset, Id);
		return newWidget;
	}
}
