/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.shape;

import eml.studio.client.ui.connection.Connection;

import com.orange.links.client.utils.Direction;

/**
 * Draw point in canvas
 */
public class Point implements Shape {

	protected int left;
	protected int top;
	private Direction direction;

	public Point(int left, int top) {
		this.left = left;
		this.top = top;
	}

	public Point(double left, double top) {
		this.left = new Double(left).intValue();
		this.top = new Double(top).intValue();
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public void setTop(int top) {
		this.top = top;
	}

	@Override
	public int getLeft() {
		return left;
	}

	@Override
	public int getTop() {
		return top;
	}

	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getHeight() {
		return 1;
	}

	public double distance(Point other) {
		return Math.sqrt((getLeft() - other.getLeft()) * (getLeft() - other.getLeft()) + (getTop() - other.getTop())
				* (getTop() - other.getTop()));
	}

	public Point move(Direction dir, int distance) {
		if (dir == Direction.S) {
			return new Point(left, top + distance);
		} else if (dir == Direction.SE) {
			return new Point(left + Math.cos(distance), top - Math.sin(distance));
		} else if (dir == Direction.SW) {
			return new Point(left - Math.cos(distance), top - Math.sin(distance));
		} else if (dir == Direction.N) {
			return new Point(left, top - distance);
		} else if (dir == Direction.NE) {
			return new Point(left + Math.cos(distance), top + Math.sin(distance));
		} else if (dir == Direction.NW) {
			return new Point(left - Math.cos(distance), top + Math.sin(distance));
		} else if (dir == Direction.W) {
			return new Point(left - distance, top);
		} else if (dir == Direction.E) {
			return new Point(left + distance, top);
		} else {
			throw new IllegalStateException();
		}
	}

	public Point move(Point vector) {
		return new Point(left + vector.left, top + vector.top);
	}

	public Point negative() {
		return new Point(-left, -top);
	}

	public static boolean equals(Point p1, Point p2) {
		return p1.left == p2.left && p1.top == p2.top;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	public void translate(int left, int top) {
		this.left += left;
		this.top += top;
	}

	public boolean equals(Point p) {
		return p.getLeft() == left && p.getTop() == top;
	}

	public boolean isInside(Shape s) {
		Rectangle r = new Rectangle(s);
		return getLeft() >= r.getLeft() && getTop() >= r.getTop() && getLeft() <= r.getLeft() + r.getWidth()
				&& getTop() <= r.getTop() + r.getHeight();
	}

	@Override
	public String toString() {
		return "[ " + left + " ; " + top + " ]";
	}

	@Override
	public boolean addConnection(Connection connection) {
		return false;
	}

	@Override
	public boolean removeConnection(Connection connection) {
		return false;
	}

	@Override
	public void setSynchronized(boolean sync) {
	}

	@Override
	public boolean isSynchronized() {
		return true;
	}

	@Override
	public void setAllowSynchronized(boolean allowSynchronized) {
	}

	@Override
	public boolean allowSynchronized(){
		return true;
	}

	@Override
	public void draw() {
	}

	@Override
	public void drawHighlight() {
	}

}
