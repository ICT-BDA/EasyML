/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.shape;

/**
 * MouseShape at Point
 */
public class MouseShape extends Point {

	private int height = 1;
	private int width = 1;
	Point mousePoint;

	public MouseShape(Point mousePoint) {
		super(mousePoint.getLeft(), mousePoint.getTop());
		this.mousePoint = mousePoint;
	}

	@Override
	public int getLeft() {
		return mousePoint.getLeft();
	}

	@Override
	public int getTop() {
		return mousePoint.getTop();
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

}