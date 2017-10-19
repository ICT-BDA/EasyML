/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import eml.studio.client.mvp.view.Generator;
import eml.studio.client.ui.connection.AbstractConnectionFactory;
import eml.studio.client.ui.connection.CommonConnectionFactory;
import eml.studio.client.ui.connection.Connection;
import eml.studio.client.ui.connection.svg.SVGPanel;
import eml.studio.client.ui.property.PropertyTable;
import eml.studio.client.ui.widget.BaseWidget;
import eml.studio.client.ui.widget.command.FileDescription;
import eml.studio.client.ui.widget.dataset.DatasetWidget;
import eml.studio.client.ui.widget.program.CommonProgramWidget;
import eml.studio.client.ui.widget.program.ProgramWidget;
import eml.studio.client.ui.widget.shape.InNodeShape;
import eml.studio.client.ui.widget.shape.MouseShape;
import eml.studio.client.ui.widget.shape.NodeShape;
import eml.studio.client.ui.widget.shape.OutNodeShape;
import eml.studio.client.ui.widget.shape.Point;
import eml.studio.client.ui.widget.shape.Shape;
import eml.studio.shared.model.Dataset;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.orange.links.client.canvas.BackgroundCanvas;
import com.orange.links.client.canvas.DiagramCanvas;
import com.orange.links.client.canvas.MultiBrowserDiagramCanvas;
import com.orange.links.client.menu.ContextMenu;
import com.orange.links.client.menu.HasContextMenu;
import com.orange.links.client.shapes.DrawableSet;
import com.orange.links.client.utils.LinksClientBundle;

/**
 *  Drawing diagram controller related to
 *  shape drawing
 */
public class DiagramController {

	/**
	 * If the distance between the mouse and segment is under this number in
	 * pixels, then, the mouse is considered over the segment.
	 */
	public static int minDistanceToSegment = 10;
	/**
	 * Timer refresh duration, in milliseconds. It defers if the application is
	 * running in development mode or in the web mode.
	 */
	public static int refreshRate = GWT.isScript() ? 25 : 50;

	private static Logger logger =
			Logger.getLogger(DiagramController.class.getName());

	PopupPanel popupPanel = new PopupPanel(true);
	protected FocusPanel focusPanel = new FocusPanel();

	private boolean lockDrawConnection = false;

	/**
	 * Whether you are in the area which can select a graphic and draw an arrow.
	 */
	protected boolean inEditionToDrawConnection = false;

	/**
	 * Whether you are in the state which can drag the mouse to draw the arrow.
	 */
	protected boolean inDragBuildConnection = false;

	/**
	 * Whether you are in the state which can drag the shape.
	 */
	protected boolean inDragWidget = false;

	/**
	 * diagram menu.
	 */
	protected ContextMenu cmenu = new ContextMenu();

	/**
	 * The width of the canvas in drawing area.
	 */
	protected int canvasWidth;

	/**
	 * The height of the canvas in drawing area.
	 */
	protected int canvasHeight;
	protected long nFrame = 0;
	protected long previousNFrame = 0;
	protected long previousTime = 0;
	protected long fps = 0;
	protected final Timer frameTimer = new Timer() {
		@Override
		public void run() {
			long now = new Date().getTime();
			fps = (now - previousTime) != 0 ? (nFrame - previousNFrame) * 1000
					/ (now - previousTime) : 0;
					previousNFrame = nFrame;
					previousTime = now;
		}
	};
	protected final Timer focusTimer = new Timer() {
		int i = 1;
		@Override
		public void run() {
			if(selectedWidget!=null)
			{
				logger.info("focusTimer "+ i);
				i++;
				selectedWidget.setFocus();

				isFocus = true;
				if(i==10) {
					i=1;
					this.cancel();
				}
			}else{
				logger.info("focuspanelTimer "+ i);
				i++;
				scrollPanel.getElement().focus();

				isFocus = true;
				if(i==10) {
					i=1;
					this.cancel();
				}
			}

		}

	};
	protected boolean inShapeArea = false;
	protected NodeShape shapeUnderMouse = null;
	protected AbstractConnectionFactory connfactory;
	protected BaseWidget selectedWidget = null;
	protected List<BaseWidget> selectedWidgets = new ArrayList<BaseWidget>();
	protected CommonProgramWidget fakefocus = new CommonProgramWidget();
	protected boolean isvacancy = true;

	/**
	 * All widgets in the drawing area, {widget_id:widget,...}.
	 */
	protected Map<String, BaseWidget> widgets = new HashMap<String, BaseWidget>();
	protected Map<String, BaseWidget> allwidgets = new HashMap<String, BaseWidget>();

	/**
	 * All widgets connection in the drawing area.
	 */
	protected DrawableSet<Connection> connDrawSet = new DrawableSet<Connection>();

	/**
	 * Whether to display the grid.
	 */
	protected boolean showGrid;

	/**
	 * Background canvas, used to fill the grid, always in the bottom
	 */
	protected BackgroundCanvas backgroundCanvas;

	protected Boolean isFocus = false;

	/**
	 * Widget panel where the widget should be attached to.
	 */
	protected AbsolutePanel widgetPanel;

	/**
	 * scroll panel, the widgetpanel was attached to it so that it has scroll.
	 */
	protected ScrollPanel scrollPanel;

	/**
	 * top canvas help to draw connections
	 */
	protected DiagramCanvas topCanvas;

	protected SVGPanel svgPanel;

	/**
	 * Widget attached it can be dragged.
	 */
	protected DragController dragController;
	protected Shape startShape;

	/**
	 * Coordinates synchronize with the mouse position( for the drawing area),
	 * can be updated in real time.
	 */
	private Point mousePoint = new Point(0, 0);

	protected Generator generator;

	/**
	 * Coordinates synchronize with the mouse position( for the browser window),
	 * can be updated in real time.
	 */
	private Point mouseOffsetPoint = new Point(0, 0);

	/**
	 * Setup timer
	 */
	protected final Timer timer = new Timer() {
		@Override
		public void run() {
			nFrame++;
			update();
		}
	};

	private Connection buildConnection;

	/**
	 * Initialize the controller diagram. Use this constructor to start your
	 * diagram. A code sample is : <br/>
	 * <br/>
	 * <code>
	 * DiagramController controller = new DiagramController(400,400);<br/>
	 * RootPanel.get().add(controller.getView());
	 * </code> <br/>
	 */
	public DiagramController(int canvasWidth, int canvasHeight) {

		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		this.backgroundCanvas = new BackgroundCanvas(canvasWidth, canvasHeight);
		this.topCanvas = new MultiBrowserDiagramCanvas(canvasWidth, canvasHeight);
		this.connfactory = CommonConnectionFactory.singleton;
		initWidgetPanel(topCanvas);

		dragController = new PickupDragController(getView(), true);
		dragController.setBehaviorMultipleSelection(true);

		this.registerDragController(dragController);

		LinksClientBundle.INSTANCE.css().ensureInjected();
		initEventHandler(widgetPanel);

		timer.scheduleRepeating(refreshRate);
		frameTimer.scheduleRepeating(70);

		scrollPanel = new ScrollPanel(getView());

		//Block the browser's menu
		ContextMenu.disableBrowserContextMenu(widgetPanel.asWidget().getElement());
		ContextMenu.disableBrowserContextMenu(topCanvas.asWidget().getElement());
	}

	public DiagramController(int canvasWidth, int canvasHeight, int frameWidth,
			int frameHeight) {
		this(canvasWidth, canvasHeight);
		scrollPanel = new ScrollPanel(getView());
		scrollPanel.setWidth(frameWidth-30 + "px");
		scrollPanel.setHeight(frameHeight + "px");

	}

	/**
	 * Initialize the widget panel according to the canvas.
	 * 
	 * @param canvas
	 */
	protected void initWidgetPanel(final DiagramCanvas canvas) {
		widgetPanel = new AbsolutePanel();
		widgetPanel.getElement().getStyle().setWidth(canvas.getWidth(), Unit.PX);
		widgetPanel.getElement().getStyle().setHeight(canvas.getHeight(), Unit.PX);
		widgetPanel.add(canvas.asWidget());

		this.svgPanel = new SVGPanel();
		widgetPanel.add(svgPanel);
		focusPanel.add(widgetPanel);
		scrollPanel = new ScrollPanel(widgetPanel);

	}

	/**
	 * Get the scroll panel in the drawing cavcas.
	 * @return
	 */
	public ScrollPanel getViewAsScrollPanel() {
		scrollPanel.addScrollHandler(new ScrollHandler() {
			@Override
			public void onScroll(ScrollEvent event) {
			}
		});
		return scrollPanel;
	}

	/**
	 * Get the total window size.
	 * 
	 * @return
	 */
	public Point getWindowSize() {
		Point size = new Point(0, 0);
		size.setLeft(this.scrollPanel.getOffsetWidth());
		size.setTop(this.scrollPanel.getOffsetHeight());
		return size;
	}

	/**
	 * Place a new widget to paint panel
	 * 
	 * @param widget
	 */
	public void addWidget(final BaseWidget widget) {
		int h = this.scrollPanel.getOffsetHeight();
		int w = this.scrollPanel.getOffsetWidth();
		int offsetx = this.scrollPanel.getHorizontalScrollPosition();
		int offsety = this.scrollPanel.getVerticalScrollPosition();
		int x = offsetx + 4 * w / 9 + Random.nextInt(100);
		int y = offsety + 2 * h / 7 + Random.nextInt(100);
		addWidget(widget, x, y);
	}
	/**
	 * Add a widget with specified position
	 * 
	 * @param w
	 * @param left
	 * @param top
	 */
	public void addWidget(final BaseWidget w, int left, int top) {
		w.getElement().getStyle().setZIndex(3);
		widgetPanel.add(w, left, top);
		widgets.put(w.getId(), w);
		w.registerController(this);
		dragController.makeDraggable(w);
	}


	/**
	 * Delete all the widget in the canvas.(Include the selected widget) 
	 */
	public void deleteWidget(){

		if( this.isLockDrawConnection() ) return;
		List<BaseWidget> selectedwidgets = this.getSelectedWidgets();
		for(BaseWidget widget:selectedwidgets){
			this.deleteWidget(widget);
		}
		this.clearSelectedWidgets();
	}

	/**
	 * Initialize panel event handler.
	 * 
	 * @param panel
	 */
	protected void initEventHandler(Panel panel) {

		panel.addDomHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				DiagramController.this.onMouseMove(event);
			}
		}, MouseMoveEvent.getType());

		panel.addDomHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				DiagramController.this.onMouseDown(event);
			}
		}, MouseDownEvent.getType());

		panel.addDomHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				DiagramController.this.onMouseUp(event);
			}
		}, MouseUpEvent.getType());
		panel.addDomHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				DiagramController.this.onKeyUp(event);
			}
		}, KeyUpEvent.getType());
		panel.addDomHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent doubleClickEvent) {
				DiagramController.this.onDoubleClick(doubleClickEvent);
			}
		},DoubleClickEvent.getType());
	}

	/**
	 * Add key handler to diagram canvas.
	 *  
	 * @param panel
	 */
	protected  void addKeyHandler(DiagramCanvas panel){
		panel.addDomHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				DiagramController.this.onKeyUp(event);
			}
		}, KeyUpEvent.getType());
	}
	protected  void onDoubleClick(DoubleClickEvent event){

	}
	/**
	 * Trigger action when key up event fired
	 * 
	 * @param event
	 */
	protected  void onKeyUp(KeyUpEvent event){
		logger.info("key" +event.getNativeKeyCode() );
		if(event.getNativeKeyCode()== 46){
			logger.info("delete widget");
			deleteWidget();
		}
	}

	/**
	 * Trigger action when mouse move event fired
	 * 
	 * @param event
	 */
	protected void onMouseMove(MouseMoveEvent event) {

		int X = event.getRelativeX(widgetPanel.getElement());
		int Y = event.getRelativeY(widgetPanel.getElement());
		getMousePoint().setLeft(X);
		getMousePoint().setTop(Y);

		int offsetX = event.getClientX();
		int offsetY = event.getClientY();
		mouseOffsetPoint.setLeft(offsetX);
		mouseOffsetPoint.setTop(offsetY);

		NodeShape shape = (NodeShape) getShapeUnderMouse();
		popupPanel.hide();
		if (shape != null) {
			popupPanel = new PopupPanel(true);
			int x = shape.getOffsetLeft() + 5;
			int y = shape.getOffsetTop() + 5;

			String string = getFileTip(shape);
			popupPanel.add(new Label(string));
			popupPanel.setPopupPosition(x, y);
			popupPanel.getElement().getStyle().setZIndex(9);
			popupPanel.show();
		}
	}

	/**
	 * Trigger action when mouse up event fired
	 * 
	 * @param event
	 */
	protected void onMouseUp(MouseUpEvent event) {
		// Test if Right Click
		if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
			logger.info( "Handle NativeEvent.BUTTON_RIGHT begin >");
			event.stopPropagation();
			event.preventDefault();
			logger.info("Handle NativeEvent.BUTTON_RIGHT end <");
			return;
		}

		if ( !lockDrawConnection && inDragBuildConnection ) {
			logger.info( "draw connection lock: " +  lockDrawConnection );
			NodeShape shape = (NodeShape) getShapeUnderMouse();
			if (shape != null && shape instanceof InNodeShape) {
				Connection c = connfactory.buildConnection(this, startShape, shape);
				if (c == null) {
					Window.alert("Connection can't be build");
				} else {
					c.draw();
					connDrawSet.add(c);
					((NodeShape) startShape).onConnectionEnd(c);
					shape.onConnectionEnd(c);
				}
			}else {
				((NodeShape) startShape).onConnectionCancel();
			}
			deleteConnection(buildConnection);
			inDragBuildConnection = false;
			buildConnection = null;
		}
	}

	/**
	 * Trigger action when mouse down event fired
	 * 
	 * @param event
	 */
	public void onMouseDown(MouseDownEvent event) {
		logger.info("diagram left mouse down");
		this.getWidgetPanel().getElement().focus();
		if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
			NodeShape shape = (NodeShape) getShapeUnderMouse();
			if (shape instanceof OutNodeShape) {
				OutNodeShape outShape = (OutNodeShape)shape;
				int x = outShape.getOffsetLeft() + 2*outShape.getRadius();
				int y = outShape.getOffsetTop() + 2*outShape.getRadius();
				outShape.getContextMenu().setPopupPosition(x,y);
				outShape.getContextMenu().show();
			}
			if(isvacancy){
				event.stopPropagation();
				event.preventDefault();
				//Popup connection menu
				if( !this.inShapeArea ){
					final Connection c = getConnectionNearMouse();
					if (c != null) {
						showMenu(c);
					}else{
						showContextualMenu(event);
					}
				}

			}

			return;
		}

		if (!lockDrawConnection && inEditionToDrawConnection) {
			logger.info( "draw connection lock: " +  lockDrawConnection );
			inDragBuildConnection = true;
			inEditionToDrawConnection = false;
			((NodeShape) startShape).onConnectionStart();
			drawBuildArrow(startShape, getMousePoint());
		}
		if(!isvacancy){
			event.stopPropagation();
			event.preventDefault();
			focusTimer.scheduleRepeating(50);
		}
		else {
			this.clearSelectedWidgets();
			selectedWidget = null;
			focusTimer.scheduleRepeating(50);
		}
		this.setIsVacancy(true);
	}

	/**
	 * Get connection infomation near mouse.
	 * 
	 * @return
	 */
	protected Connection getConnectionNearMouse() {
		for (Connection c : connDrawSet) {
			if (c.isMouseNearConnection(getMousePoint())) {
				return c;
			}
		}
		return null;
	}
	/**
	 * Show the context menu of the mouse position.(Position from mouse down event)
	 * 
	 * @param event  mouse down event.
	 */
	public void showContextualMenu(MouseDownEvent event){
		final int X = event.getRelativeX(widgetPanel.getElement());
		final int Y = event.getRelativeY(widgetPanel.getElement());
		getMousePoint().setLeft(X);
		getMousePoint().setTop(Y);

		final int offsetX = event.getClientX();
		final int offsetY = event.getClientY();
		mouseOffsetPoint.setLeft(offsetX);
		mouseOffsetPoint.setTop(offsetY);

		cmenu.hide();
		cmenu = new ContextMenu();
		Command command = new Command() {
			@Override
			public void execute() {
				cmenu.hide();
			}
		};
	}

	/**
	 * Show the menu of current mouse position.
	 * @param c
	 */
	public void showMenu(final HasContextMenu c) {
		showMenu(c, mouseOffsetPoint.getLeft(), mouseOffsetPoint.getTop());
	}

	/**
	 * Show the context menu in the current position.
	 * 
	 * @param c  Context menu
	 * @param left  Left position
	 * @param top  Top position
	 */
	protected void showMenu(final HasContextMenu c, int left, int top) {
		ContextMenu menu = c.getContextMenu();
		if (menu != null) {
			menu.setPopupPosition(left, top);
			menu.show();
		}
	}

	/**
	 * Get shape under the mouse position
	 * 
	 * @return
	 */
	protected Shape getShapeUnderMouse() {
		for (Map.Entry<String, BaseWidget> entry : widgets.entrySet()) {
			BaseWidget w = entry.getValue();
			for (NodeShape shape : w.getInNodeShapes()) {
				if (shape == null) {
					logger.info(w.getId());
					continue;
				}
				if (shape.isUnderMouse(mouseOffsetPoint)) {
					return shape;
				}
			}
			for (NodeShape shape : w.getOutNodeShapes()) {
				if (shape == null) {
					logger.info(w.getId());
					continue;
				}
				if (shape.isUnderMouse(mouseOffsetPoint)) {
					return shape;
				}
			}
		}
		return null;
	}

	/**
	 * Remove connection
	 * 
	 * @param c connection
	 */
	public void removeConnection(Connection c){
		connDrawSet.remove(c);
	}

	/**
	 * Delete connection<br/>
	 * will call the delete() method from Connection class, in this method the dependencies above
	 * connection、DiagramController and NodeShape will be removed.
	 * 
	 * @param connection
	 */
	public void deleteConnection(Connection connection) {
		connection.delete();
	}

	/**
	 * Add connection to connection set.
	 * 
	 * @param c
	 */
	public void addConnection(Connection c) {
		connDrawSet.add(c);
	}

	/**
	 * Add connection between widgets
	 *
	 * @param source_id   Source widget id
	 * @param source_port The position of nodeshape in source widget
	 * @param dest_id     Destination widget
	 * @param dest_port   The position of nodeshape in destination widget
	 */
	public void addConnection(String source_id, int source_port,
			String dest_id, int dest_port) {
		BaseWidget source_widget = widgets.get(source_id);
		BaseWidget dest_widget = widgets.get(dest_id);
		logger.info("---From :"+source_id+" port: "+source_port +" to "+dest_id+" port: "+dest_port);
		if (source_widget == null ) {
			logger.warning("the source widget is null!");
		}
		if (dest_widget == null) {
			logger.warning("the destination widget is null!");
		}
		Connection conn = null;
		Shape start = null;
		Shape end = null;
		start = source_widget.getOutNodeShapes().get(source_port);
		end = dest_widget.getInNodeShapes().get(dest_port);
		if(start == null || end == null)
		{
			Window.alert("Connection can't be build, can not find the node");
		}
		else
		{
			conn = this.connfactory.buildConnection(this, start, end);
			if (conn == null)
			{
				Window.alert("Connection can't be build");
			} else {
				conn.draw();
				connDrawSet.add(conn);
				((NodeShape) start).onConnectionEnd(conn);
				((NodeShape) end).onConnectionEnd(conn);
				source_widget.setFocus();
			}
		}
	}

	/**
	 * Delete widget from current canvas.
	 * 
	 * @param w   Widget to delete
	 */
	public void deleteWidget(BaseWidget w) {
		if( !lockDrawConnection ){
			widgets.remove(w.getId());
			widgetPanel.remove(w);
			w.delete();
		}
	}

	/**
	 * Draw build arrow from start shape to current mouse position.
	 * 
	 * @param start  Start shape
	 * @param mousePoint   Current mouse position
	 */
	protected void drawBuildArrow(Shape start, Point mousePoint) {

		final Shape endShape = new MouseShape(mousePoint);
		buildConnection = new Connection(this, start, endShape);
		buildConnection.setAllowSynchronized(false);
		buildConnection.setSynchronized(false);
		connDrawSet.add(buildConnection);
	}

	/**
	 * Show the background canvas grid.
	 * 
	 * @param showGrid
	 */
	public void showGrid(boolean showGrid) {
		this.showGrid = showGrid;
		backgroundCanvas.initGrid();
		backgroundCanvas.getElement().getStyle().setZIndex(-1);
		if (this.showGrid) {
			widgetPanel.add(backgroundCanvas.asWidget(), 0, 0);
		} else {
			widgetPanel.remove(backgroundCanvas.asWidget());
		}
	}

	/**
	 * Register drag controller.
	 * 
	 * @param dragController
	 */
	public void registerDragController(DragController dragController) {
		this.dragController = dragController;
		if (dragController != null) {
			registerDragHandler();
		}
	}

	/**
	 * Register drag handler.
	 */
	protected void registerDragHandler() {

		this.dragController.addDragHandler(new DragHandlerAdapter() {

			@Override
			public void onPreviewDragEnd(DragEndEvent event) {
				Widget widget = event.getContext().draggable;
				if (widget == null)
					return;
				BaseWidget bw = (BaseWidget) widget;
				for (NodeShape shape : bw.getInNodeShapes()) {
					shape.getConnections().draw();
				}
				for (NodeShape shape : bw.getOutNodeShapes()) {
					shape.getConnections().draw();
				}
			}

			@Override
			public void onDragEnd(DragEndEvent event) {
				inDragWidget = false;
				Widget widget = event.getContext().draggable;
				if (widget == null)
					return;
				BaseWidget bw = (BaseWidget) widget;
				for (NodeShape shape : bw.getInNodeShapes()) {
					shape.getConnections().setAllowSynchronized(true);
					shape.getConnections().setSynchronized(true);
				}
				for (NodeShape shape : bw.getOutNodeShapes()) {
					shape.getConnections().setAllowSynchronized(true);
					shape.getConnections().setSynchronized(true);
				}
			}

			@Override
			public void onDragStart(DragStartEvent event) {
				inDragWidget = true;
				Widget widget = event.getContext().draggable;
				if (widget == null)
					return;
				BaseWidget bw = (BaseWidget) widget;
				for (NodeShape shape : bw.getInNodeShapes()) {
					shape.getConnections().setSynchronized(false);
					shape.getConnections().setAllowSynchronized(false);
				}
				for (NodeShape shape : bw.getOutNodeShapes()) {
					shape.getConnections().setSynchronized(false);
					shape.getConnections().setAllowSynchronized(false);
				}
			}
		});
	}

	/**
	 * Update the canvas.
	 */
	public void update() {
		connDrawSet.draw();
		if (!inDragBuildConnection) {
			for (Map.Entry<String, BaseWidget> entry : widgets.entrySet()) {
				BaseWidget w = entry.getValue();
				for ( NodeShape shape : w.getOutNodeShapes()) {
					if (shape.isUnderMouse(mouseOffsetPoint)) {
						inEditionToDrawConnection = true;
						startShape = shape;
						inShapeArea = true;
						shapeUnderMouse = (NodeShape) startShape;
						topCanvas.setForeground();
						topCanvas.getElement().getStyle().setCursor(Cursor.POINTER);
						return;
					}
				}
				for (NodeShape shape : w.getInNodeShapes()) {
					if (shape.isUnderMouse(getMouseOffsetPoint())) {
						startShape = shape;
						inShapeArea = true;
						shapeUnderMouse = (NodeShape) startShape;
						topCanvas.setForeground();
						topCanvas.getElement().getStyle().setCursor(Cursor.POINTER);
						return;
					}
				}
			}

			inShapeArea = false;
			inEditionToDrawConnection = false;
			topCanvas.setBackground();
			topCanvas.getElement().getStyle().setCursor(Cursor.AUTO);
		} else {

		}
	}

	/**
	 * Clear the canvas in the diagram.
	 */
	public void clear() {
		widgets.clear();
		connDrawSet.clear();
		widgetPanel.clear();
		widgetPanel.getElement().getStyle().setWidth(topCanvas.getWidth(), Unit.PX);
		widgetPanel.getElement().getStyle()
		.setHeight(topCanvas.getHeight(), Unit.PX);
		widgetPanel.add(topCanvas.asWidget());
		svgPanel = new SVGPanel();
		widgetPanel.add(svgPanel);
		topCanvas.getElement().getStyle().setPosition(Position.ABSOLUTE);
		showGrid(showGrid);
	}

	/**
	 * Select a widget.
	 * 
	 * @param selectedWidget
	 */
	public void selectedWidget(final BaseWidget selectedWidget) {
		if (this.selectedWidget != null) {
			this.selectedWidget.onDeSelected();
			if(!this.selectedWidget.getId().equals(selectedWidget.getId())){
				this.clearSelectedWidgets();
				selectedWidgets.add(selectedWidget);
				this.selectedWidget = selectedWidget;
			}
			else {
				this.clearSelectedWidgets();
			}
		}
		else{
			this.clearSelectedWidgets();
			selectedWidgets.add(selectedWidget);
			this.selectedWidget = selectedWidget;
		}
	}

	/**
	 * Get the file tip when the mouse move to the connected point.
	 * 
	 * @param shape 
	 * @return  File tip content
	 */
	protected String getFileTip(NodeShape shape) {
		logger.info("--getFiletip");
		BaseWidget oozieWidget = shape.getWidget();
		int shapeid = shape.getPortId();
		if (oozieWidget instanceof ProgramWidget) {
			ProgramWidget programWidget = (ProgramWidget) oozieWidget;
			FileDescription fip = null;
			if (shape instanceof OutNodeShape){
				fip = programWidget.getProgramConf().getOutputFile(shapeid);
			}
			else if(shape instanceof InNodeShape)
			{
				fip = programWidget.getProgramConf().getInputFile(shapeid);
			}

			if( fip == null ) return "";

			String tips = "";
			if (fip.getContentType().equals("")) {
				tips += "General : ";
			} else {
				tips += fip.getContentType() + " : ";
			}

			if (fip.getDescription().equals("")) {
				tips += "no description";
			} else {
				tips += fip.getDescription();
			}
			return tips;

		} else if(oozieWidget instanceof DatasetWidget){
			DatasetWidget fw = (DatasetWidget) shape.getWidget();
			String tips = "";
			Dataset data = fw.getDataset();
			if (data.getContenttype().equals("")) {
				tips += "General : ";
			} else {
				tips += data.getContenttype() + " : ";
			}

			if (data.getDescription().equals("")) {
				tips += "no description";
			} else {
				tips += data.getDescription();
			}
			return tips;
		} else {
			return "no descriptions";
		}
	}

	/**
	 * Add a new selected widget.
	 * 
	 * @param selectedWidget
	 */
	public void addSelectedWidgets(BaseWidget selectedWidget)
	{
		this.selectedWidget = selectedWidget;
		selectedWidgets.add(selectedWidget);
	}

	/**
	 * Clear all the selected widget.
	 */
	public void clearSelectedWidgets(){
		for(BaseWidget widget:selectedWidgets){
			widget.onDeSelected();
			selectedWidgets.remove(widget);
		}
		selectedWidgets.clear();
		selectedWidget = fakefocus;
	}

	/**
	 * Identity the widget is selected widget.
	 * 
	 * @param widget
	 * @return  
	 */
	public Boolean isInSelectedWidgets(BaseWidget widget){
		for (BaseWidget w:selectedWidgets){
			if(w == widget) return true;
		}
		return false;
	}

	public AbsolutePanel getWidgetPanel() {
		return widgetPanel;
	}

	public void setWidgetPanel(AbsolutePanel widgetPanel) {
		this.widgetPanel = widgetPanel;
	}

	public DrawableSet<Connection> getConnDrawSet() {
		return connDrawSet;
	}

	public Generator getGenerator() {
		return generator;
	}

	public void setGenerator(Generator generator) {
		this.generator = generator;
	}

	public void setPropTable(PropertyTable pt) {
		this.generator.setPropTable(pt);
	}

	public List<BaseWidget> getSelectedWidgets(){
		return this.selectedWidgets;
	}

	public void lockDrawConnection() {
		lockDrawConnection = true;
	}

	public void unlockDrawConnection() {
		lockDrawConnection = false;
	}

	public boolean isLockDrawConnection(){
		return lockDrawConnection;
	}

	public void setIsVacancy(Boolean isvacancy){
		this.isvacancy = isvacancy;
	}

	public Boolean getIsVacancy(){
		return this.isvacancy;
	}

	public int getWidgetNum() {
		return widgets.size();
	}

	public Point getMouseOffsetPoint() {
		return mouseOffsetPoint;
	}

	public Point getMousePoint() {
		return mousePoint;
	}

	public Map<String, BaseWidget> getAllwidgets() {
		return allwidgets;
	}

	public void setAllwidgets(Map<String, BaseWidget> submitwidgets) {
		this.allwidgets = submitwidgets;
	}

	public BaseWidget getSelectedWidget() {
		return selectedWidget;
	}

	public int getCanvasWidth() {
		return canvasWidth;
	}

	public int getCanvasHeight() {
		return canvasHeight;
	}

	public AbsolutePanel getView() {
		return widgetPanel;
	}

	public Map<String, BaseWidget> getWidgets() {
		return widgets;
	}

	public void setWidgets(Map<String, BaseWidget> widgets) {
		this.widgets = widgets;
	}

	public SVGPanel getSvgPanel() {
		return svgPanel;
	}

	public void setConnectionFactory(AbstractConnectionFactory factory) {
		this.connfactory = factory;
	}
}