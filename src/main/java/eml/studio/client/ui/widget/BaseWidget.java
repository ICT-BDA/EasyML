/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import eml.studio.client.controller.DiagramController;
import eml.studio.client.ui.menu.HasRightMouseUpMenu;
import eml.studio.client.ui.widget.shape.InNodeShape;
import eml.studio.client.ui.widget.shape.NodeShape;
import eml.studio.client.ui.widget.shape.OutNodeShape;
import eml.studio.client.ui.widget.shape.Point;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;
import com.orange.links.client.menu.ContextMenu;

/**
 * Base class of Base Widget in studio Panel
 */
public class BaseWidget extends Composite implements HasAllKeyHandlers,
HasMouseDownHandlers, HasMouseOutHandlers,
MouseOutHandler, HasMouseOverHandlers, MouseDownHandler, MouseUpHandler,
MouseOverHandler, HasRightMouseUpMenu {
	private static Logger logger =
			Logger.getLogger(BaseWidget.class.getName());
	protected List<InNodeShape> inNodeShapes = new ArrayList<InNodeShape>();
	protected List<OutNodeShape> outNodeShapes = new ArrayList<OutNodeShape>();
	protected Label label;
	protected GWTCanvas canvas;
	protected double offsetWidth;
	protected double offsetHeight;

	protected Integer x = 0;
	protected Integer y = 0;

	protected int offsetTop;
	protected int offsetLeft;
	protected int clientHeight;
	protected int clientWidth;
	protected double boder;
	protected double clientLeft; //The width of the left border
	protected ContextMenu menu = new ContextMenu();
	protected boolean selected = false;
	protected AbsolutePanel abspanel = new AbsolutePanel();
	protected FocusPanel focusPanel = new FocusPanel();
	private boolean firstload = true;
	private DiagramController controller;
	private String id;
	private PopupPanel p = new PopupPanel(true);

	public BaseWidget(String text, String id) {
		this.label = new Label(text);
		p.setWidget(new HTML(text));
		p.getElement().getStyle().setZIndex(6);
		this.id = id;

		label.setStyleName("basewidget");
		label.setTitle(text);

		canvas = new GWTCanvas();
		abspanel.add(label, 0, 0);
		abspanel.add(canvas, 0, 0);
		focusPanel.add(abspanel.asWidget());
		focusPanel.setHeight("35px");
		focusPanel.setFocus(true);
		focusPanel.setStyleName("basefocuspanel");
		abspanel.setHeight("100%");
		initWidget(focusPanel);

		addDomHandler(this, MouseDownEvent.getType());
		addDomHandler(this, MouseUpEvent.getType());
		addDomHandler(this, MouseOverEvent.getType());
		addDomHandler(this, MouseOutEvent.getType());


	}
	@Override
	public void onAttach() {
		super.onAttach();
		if (firstload) {
			init();
			firstload = false;
		}
	}

	/**
	 * Draw NodeShape
	 */
	protected void custom() {

		OutNodeShape outNodeShape = new OutNodeShape(this, 0, offsetLeft + offsetWidth
				/ 2, offsetTop + offsetHeight - boder / 2);
		canvas.clear();
		canvas.beginPath();
		canvas.arc(outNodeShape.getLeftRelative(), outNodeShape.getTopRelative(), 3, 0,
				Math.PI * 2.0, true);
		canvas.setStrokeStyle(Color.BLUE);
		canvas.stroke();
		canvas.setFillStyle(Color.GREEN);
		canvas.fill();
		canvas.closePath();
		outNodeShapes.add(outNodeShape);

		InNodeShape inNodeShape = new InNodeShape(this, 0, offsetLeft + offsetWidth / 2,
				offsetTop + boder / 2);
		canvas.beginPath();
		canvas.arc(inNodeShape.getLeftRelative(), inNodeShape.getTopRelative(), 3, 0,
				Math.PI * 2.0, true);
		canvas.setStrokeStyle(Color.BLUE);
		canvas.stroke();
		canvas.setFillStyle(Color.YELLOW);
		canvas.fill();
		canvas.closePath();
		inNodeShapes.add(inNodeShape);
	}

	/**
	 * Init BaseWidget: clientWidth/Height, offsetWidth/Height,border,
	 * Set PixelSize of BaseWidget, and call custom to draw NodeShape shape
	 * Must be called after the control is attached, so call the init function in the onAttach function
	 */
	protected void init() {
		clientWidth = DOM.getElementPropertyInt(label.getElement(), "clientWidth");
		if (clientWidth < 100) {
			label.setWidth("100px");
		}
		clientWidth = DOM.getElementPropertyInt(label.getElement(), "clientWidth");
		clientHeight = DOM
				.getElementPropertyInt(label.getElement(), "clientHeight");
		offsetWidth = label.getOffsetWidth();
		offsetHeight = label.getOffsetHeight();
		offsetTop = DOM.getElementPropertyInt(label.getElement(), "offsetTop");
		offsetLeft = DOM.getElementPropertyInt(label.getElement(), "offsetLeft");

		boder = (offsetHeight - clientHeight) / 2;
		clientLeft = (offsetWidth - clientWidth)/2;

		custom();

		this.setPixelSize((int) offsetWidth + offsetLeft * 2, (int) offsetHeight
				+ offsetTop * 2);
	}

	public List<InNodeShape> getInNodeShapes() {
		return inNodeShapes;
	}

	public List<OutNodeShape> getOutNodeShapes() {
		return outNodeShapes;
	}

	public void registerController(DiagramController controller) {
		this.controller = controller;

		for (InNodeShape shape : inNodeShapes) {
			shape.registerContoller(controller);
		}
		for (OutNodeShape shape : outNodeShapes) {
			shape.registerContoller(controller);
		}
	}


	public void onDeSelected() {
		selected = false;
	}

	public void onSelected() {
		selected = true;
	}

	public void setFocus(){
		focusPanel.setFocus(true);
	}

	public void cancelFocus(){
		focusPanel.setFocus(false);
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		logger.info("widget left mouse down");
		this.onSelected();
		this.getController().setIsVacancy(false);
		if(event.isControlKeyDown()){
			if(this.getController().isInSelectedWidgets(this)){
				this.getController().getSelectedWidgets().remove(this);
				this.onDeSelected();
			}
			else{

				this.getController().addSelectedWidgets(this);
			}

		}
		else{
			this.getController().selectedWidget(this);
		}

	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
			event.stopPropagation();
			event.preventDefault();
			controller.showMenu(this);
			this.setFocus();
		}
	}


	public void printMessage(String eventName, int code, boolean modifier, boolean control) {
		final NumberFormat formatter = NumberFormat.getDecimalFormat();
		String message = eventName + " -  Char Code: " + formatter.format(code) + ".  ";

		if(code == KeyCodes.KEY_ENTER) {
			message += "Key is ENTER.  ";
		}

		if(modifier)
			message += "Modifier is down.  ";

		if(control)
			message += "CTRL is down.  ";
		logger.info("message"+message);
	}
	@Override
	public ContextMenu getContextMenu() {
		return menu;
	}

	/**
	 * Delete all associated connections in the widget
	 */
	public void delete() {
		for (NodeShape shape : inNodeShapes) {
			shape.deleteAllConnections();
		}

		for (NodeShape shape : outNodeShapes) {
			shape.deleteAllConnections();
		}
	}
	public void clearMenu(){
		menu.clear();
		menu = new ContextMenu();
	}
	/**
	 * Add Menu Item
	 *
	 * @param menuItem target item
	 */
	@Override
	public void addMenuItem(MenuItem menuItem) {
		menu.addItem(menuItem);
	}

	public DiagramController getController() {
		return controller;
	}

	public void setController(DiagramController controller) {
		this.controller = controller;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		Point size = this.getController().getWindowSize();

		int X = this.getAbsoluteLeft() + this.getOffsetWidth() - 5;
		int Y = this.getAbsoluteTop() + this.getOffsetHeight() - 5;
		if (X > size.getLeft()) {
			X = this.getAbsoluteLeft() + 5;
		}
		if (Y > size.getTop()) {
			Y = this.getAbsoluteTop() + 5;
		}
		p.setPopupPosition(X, Y);

		p.show();
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		p.hide();
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}


	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return focusPanel.addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return focusPanel.addKeyDownHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return focusPanel.addKeyPressHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler, MouseDownEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	public int getOffsetLeft() {
		return offsetLeft;
	}

	public void setOffsetLeft(int offsetLeft) {
		this.offsetLeft = offsetLeft;
	}

	public int getOffsetTop() {
		return offsetTop;
	}

	public void setOffsetTop(int offsetTop) {
		this.offsetTop = offsetTop;
	}



	public void setOffsetHeight(double offsetHeight) {
		this.offsetHeight = offsetHeight;
	}



	public void setOffsetWidth(double offsetWidth) {
		this.offsetWidth = offsetWidth;
	}

	public int getClientHeight() {
		return clientHeight;
	}

	public void setClientHeight(int clientHeight) {
		this.clientHeight = clientHeight;
	}

	public int getClientWidth() {
		return clientWidth;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}
	public void setClientWidth(int clientWidth) {
		this.clientWidth = clientWidth;
	}

	public BaseWidget clone(String Id){
		Window.alert("clone");
		return this;
	}

	/**
	 * clone widget Nodes
	 * @param widget the origin widget
	 */
	public void cloneNode(BaseWidget widget){
		inNodeShapes.clear();
		for(InNodeShape inNodeShape : widget.getInNodeShapes()){
			this.inNodeShapes.add(inNodeShape.clone());
		}
		outNodeShapes.clear();
		for(OutNodeShape outNodeShape : widget.getOutNodeShapes()){
			this.outNodeShapes.add(outNodeShape.clone());
		}
	}

	public void setName(String name){
		label.setText(name);
	}
	public String getName(){return label.getText();}
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		BaseWidget.logger = logger;
	}

	public void setInNodeShapes(List<InNodeShape> inNodeShapes) {
		this.inNodeShapes = inNodeShapes;
	}

	public void setOutNodeShapes(List<OutNodeShape> outNodeShapes) {
		this.outNodeShapes = outNodeShapes;
	}
}
