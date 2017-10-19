/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.shape;

import eml.studio.client.controller.DiagramController;
import eml.studio.client.ui.connection.Connection;
import eml.studio.client.ui.menu.HasRightMouseUpMenu;
import eml.studio.client.ui.widget.BaseWidget;
import eml.studio.client.event.HasConnectionEvent;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.MenuItem;
import com.orange.links.client.menu.ContextMenu;
import com.orange.links.client.shapes.DrawableSet;
import com.orange.links.client.utils.ClassnameContainerFinder;
import com.orange.links.client.utils.IContainerFinder;

/**
 * Abstract class of nodeshape on widget
 */
public abstract class NodeShape extends Point implements HasConnectionEvent,HasRightMouseUpMenu {

	// portId indicates the location of the NodeShape
	private int portId;
	protected IContainerFinder containerFinder = new ClassnameContainerFinder();
	protected DrawableSet<Connection> conns = new DrawableSet<Connection>();

	/** The parent widget */
	private BaseWidget widget;

	final private int radius = 2;
	private double leftRelative;
	private double topRelative;
	private int containerOffsetLeft = -1;
	private int containerOffsetTop = -1;
	protected DiagramController controller;

	private boolean sync;
	protected boolean allowSync = true;

	protected ContextMenu menu;

	public NodeShape(BaseWidget parent, int portId) {
		super(0, 0);
		this.setWidget(parent);
		this.setPortId(portId);
		this.menu = new ContextMenu();
	}

	public NodeShape(BaseWidget parent,
			int portId,
			double leftRelative,
			double topRelative) {
		this(parent, portId);
		this.leftRelative = leftRelative;
		this.topRelative = topRelative;
	}

	@Override
	public int getLeft() {
		return (int) (getWidget().getAbsoluteLeft() + getLeftRelative()
				- getContainerOffsetLeft() - radius);
	}

	@Override
	public int getTop() {
		return (int) (getWidget().getAbsoluteTop() + getTopRelative()
				- getContainerOffsetTop() - radius);
	}

	public int getOffsetLeft() {
		return (int) (getWidget().getAbsoluteLeft() + getLeftRelative() - radius);
	}

	public int getOffsetTop() {
		return (int) (getWidget().getAbsoluteTop() + getTopRelative() - radius);
	}

	protected int getContainerOffsetLeft() {
		if (containerOffsetLeft < 0 || !sync) {
			int scrollLeft = 0;
			Element parent = DOM.getParent(getWidget().getElement());
			while (parent != null) {
				if (getScrollLeft(parent) > 0) {
					scrollLeft += getScrollLeft(parent);
					GWT.log("Scroll left detected : " + scrollLeft);
				}
				if (containerFinder.isContainer(parent)) {
					containerOffsetLeft = DOM.getAbsoluteLeft(parent) - scrollLeft;
				}
				parent = DOM.getParent(parent);
			}
		}
		return containerOffsetLeft;
	}

	protected int getContainerOffsetTop() {
		if (containerOffsetTop < 0 || !sync) {
			int scrollTop = 0;
			Element parent = DOM.getParent(getWidget().getElement());
			while (parent != null) {
				if (getScrollTop(parent) > 0) {
					scrollTop += getScrollTop(parent);
					GWT.log("Scroll Top detected : " + scrollTop);
				}
				if (containerFinder.isContainer(parent)) {
					containerOffsetTop = DOM.getAbsoluteTop(parent) - scrollTop;
				}
				parent = DOM.getParent(parent);
			}
		}
		return containerOffsetTop;
	}

	private native int getScrollLeft(Element element)/*-{
      return element.scrollLeft;
  }-*/;

	private native int getScrollTop(Element element)/*-{
      return element.scrollTop;
  }-*/;

	public double getTopRelative() {
		return topRelative;
	}

	public double getLeftRelative() {
		return leftRelative;
	}

	@Override
	public int getWidth() {
		return radius * 2;
	}

	@Override
	public int getHeight() {
		return radius * 2;
	}

	public boolean isUnderMouse(Point mousePoint) {
		int x = mousePoint.getLeft();
		int y = mousePoint.getTop();
		int l = getOffsetLeft() - 3 * radius;
		int r = getOffsetLeft() + 3 * radius;
		int t = getOffsetTop() - 3 * radius;
		int b = getOffsetTop() + 3 * radius;

		return (x >= l && x <= r && y >= t && y <= b);
	}

	@Override
	public boolean isSynchronized() {
		return sync;
	}

	@Override
	public void setSynchronized(boolean sync) {
		if (allowSync)
			this.sync = sync;
	}

	@Override
	public boolean allowSynchronized() {
		return allowSync;
	}

	@Override
	public void setAllowSynchronized(boolean allowSynchronized) {
		this.allowSync = allowSynchronized;
	}

	public abstract DrawableSet<Connection> getConnections();

	public void registerContoller(DiagramController controller) {
		this.controller = controller;
	}

	public final void addConnectionToController(Connection conn) {
		if (controller != null) {
			controller.addConnection(conn);
		}
	}


	@Override
	public boolean addConnection(Connection connection) {
		return conns.add(connection);
	}

	@Override
	public boolean removeConnection(Connection connection) {
		return conns.remove(connection);
	}

	public final void deleteConnectionFromController(Connection conn) {
		if (controller != null) {
			controller.deleteConnection(conn);
		}
	}

	/**
	 * Delete all related connections
	 */
	public void deleteAllConnections() {
		Object arr[] = conns.toArray();
		for( Object obj: arr){
			Connection conn = (Connection)obj;
			conn.delete();
		}
	}


	public BaseWidget getWidget() {
		return widget;
	}

	public void setWidget(BaseWidget widget) {
		this.widget = widget;
	}

	public int getPortId() {
		return portId;
	}

	public void setPortId(int portId) {
		this.portId = portId;
	}

	@Override
	public ContextMenu getContextMenu() {

		return menu;
	}

	@Override
	public void addMenuItem(MenuItem menuItem) {
		menu.addItem(menuItem);
	}

	public void setLeftRelative(double leftRelative) {
		this.leftRelative = leftRelative;
	}

	public void setTopRelative(double topRelative) {
		this.topRelative = topRelative;
	}

	public int getRadius(){
		return radius;
	}
}
