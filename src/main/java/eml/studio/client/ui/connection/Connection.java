/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.connection;

import eml.studio.client.controller.DiagramController;
import eml.studio.client.ui.connection.svg.SVGLine;
import eml.studio.client.ui.widget.shape.ConnectionUtils;
import eml.studio.client.ui.widget.shape.Point;
import eml.studio.client.ui.widget.shape.Segment;
import eml.studio.client.ui.widget.shape.Shape;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import com.orange.links.client.canvas.ConnectionCanvas;
import com.orange.links.client.exception.DiagramViewNotDisplayedException;
import com.orange.links.client.menu.ContextMenu;
import com.orange.links.client.menu.HasContextMenu;
import com.orange.links.client.shapes.DecorationShape;
import com.orange.links.client.shapes.Drawable;

/**
 * A connection that is a {@link Drawable} and {@link HasContextMenu}. 
 * It indicates an arrow from one {@link Shape} to another {@link Shape},
 * When draw is called, a SVG element will be add to the html dom tree.
 * 
 * */
public class Connection implements Drawable, HasContextMenu {
	protected Shape startShape;
	protected Shape endShape;
	protected DiagramController controller;
	protected ConnectionCanvas canvas;
	protected DecorationShape decoration;

	public static CssColor defaultConnectionColor = CssColor.make("#000000");
	protected CssColor connectionColor = defaultConnectionColor;

	protected ContextMenu menu;
	protected static String deleteMenuText = "Delete";

	private boolean sync;
	private boolean allowSync = true;
	private SVGLine line;

	public Connection(DiagramController controller, Shape startShape,
			Shape endShape) throws DiagramViewNotDisplayedException {
		this.startShape = startShape;
		this.endShape = endShape;
		initMenu();

		setController(controller);
		line = new SVGLine();

		this.controller.getSvgPanel().addElement(line.asElement());
	}

	protected void initMenu() {
		menu = new ContextMenu();
		menu.addItem(new MenuItem(deleteMenuText, true, new Command() {
			@Override
			public void execute() {
				controller.deleteConnection(Connection.this);
				startShape.removeConnection(Connection.this);
				endShape.removeConnection(Connection.this);
				menu.hide();
			}
		}));
	}

	@Override
	public boolean isSynchronized() {
		return sync;
	}

	@Override
	public void setSynchronized(boolean sync) {
		if (allowSync) {
			this.sync = sync;
		}
	}

	@Override
	public boolean allowSynchronized() {
		return allowSync;
	}

	public void delete() {
		Shape start = this.getStartShape();
		start.removeConnection(this);
		Shape end = this.getEndShape();
		end.removeConnection(this);
		this.controller.removeConnection(this);
		this.controller.getSvgPanel().removeElement(line.asElement());
	}

	@Override
	public void draw(){
		if (this.sync)
			return;

		Segment segment = ConnectionUtils.computeSegment(startShape,endShape);
		line.draw(segment.getP1(),segment.getP2() );
		setSynchronized(true);
	}

	@Override
	public void drawHighlight() {

	}

	public Shape getStartShape() {
		return startShape;
	}

	public Shape getEndShape() {
		return endShape;
	}

	/**
	 * To judge whether the mouse pointer is near the connection's area
	 * @param p the mouse pointer axis location
	 * @return true if near, otherwise false
	 */
	public boolean isMouseNearConnection(Point p) {
		Segment segment = ConnectionUtils.computeSegment(startShape,endShape);
		if (!segment.getP1().equals(segment.getP2()) ){
			double dist = ConnectionUtils.distanceToSegment(segment, p);    
			if(  dist < DiagramController.minDistanceToSegment)
				return true;
		}
		return false;
	}

	/**
	 * Get the context menu
	 */
	@Override
	public ContextMenu getContextMenu() {
		return menu;
	}

	/**
	 * Set the controller that the connection belongs to.
	 * @param controller
	 */
	public void setController(DiagramController controller) {
		this.controller = controller;
	}

	@Override
	public void setAllowSynchronized(boolean allowSynchronized) {
		this.allowSync = allowSynchronized;
	}
}