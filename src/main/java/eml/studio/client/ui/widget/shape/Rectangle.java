/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.shape;

/**
 * Draw Rectangle in canvas
 */
public class Rectangle {

	private Point cornerTopLeft;
	private Point cornerTopRight;
	private Point cornerBottomLeft;
	private Point cornerBottomRight;
	private Shape shape;

	public Rectangle(Shape s){
		shape = s;
		cornerTopLeft = new Point(s.getLeft(),s.getTop());
		cornerTopRight = new Point(s.getLeft()+s.getWidth(),s.getTop());
		cornerBottomLeft = new Point(s.getLeft(),s.getTop()+s.getHeight());
		cornerBottomRight = new Point(s.getLeft()+s.getWidth(),s.getTop()+s.getHeight());
	}

	public Rectangle(Point cornerTopLeft, Point cornerTopRight, Point cornerBottomLeft, Point cornerBottomRight){
		this.cornerTopLeft = cornerTopLeft;
		this.cornerTopRight = cornerTopRight;
		this.cornerBottomLeft = cornerBottomLeft;
		this.cornerBottomRight = cornerBottomRight;
	}

	public Point getCornerTopLeft() {
		return cornerTopLeft;
	}

	public void setCornerTopLeft(Point cornerTopLeft) {
		this.cornerTopLeft = cornerTopLeft;
	}

	public Point getCornerTopRight() {
		return cornerTopRight;
	}

	public void setCornerTopRight(Point cornerTopRight) {
		this.cornerTopRight = cornerTopRight;
	}

	public Point getCornerBottomLeft() {
		return cornerBottomLeft;
	}

	public void setCornerBottomLeft(Point cornerBottomLeft) {
		this.cornerBottomLeft = cornerBottomLeft;
	}

	public Point getCornerBottomRight() {
		return cornerBottomRight;
	}

	public void setCornerBottomRight(Point cornerBottomRight) {
		this.cornerBottomRight = cornerBottomRight;
	}

	public Segment getBorderLeft(){
		return new Segment(cornerTopLeft,cornerBottomLeft);
	}

	public Segment getBorderRight(){
		return new Segment(cornerTopRight,cornerBottomRight);
	}

	public Segment getBorderTop(){
		return new Segment(cornerTopLeft,cornerTopRight);
	}

	public Segment getBorderBottom(){
		return new Segment(cornerBottomLeft,cornerBottomRight);
	}

	public void translate(int left, int top){
		cornerTopLeft.translate(left,top);
		cornerTopRight.translate(left,top);
		cornerBottomLeft.translate(left,top);
		cornerBottomRight.translate(left,top);
	}

	public boolean isInside(Point p){
		return p.getLeft() >= getLeft()
				&& p.getTop() >= getTop()
				&& p.getLeft() <= getLeft() + getWidth()
				&& p.getTop() <= getTop() + getHeight()
				;
	}

	public int getLeft(){
		return cornerTopLeft.getLeft();
	}

	public int getTop(){
		return cornerTopLeft.getTop();
	}

	public int getWidth(){
		return cornerTopRight.getLeft() - cornerTopLeft.getLeft();
	}

	public int getHeight(){
		return cornerBottomLeft.getTop() - cornerTopLeft.getTop();
	}

	@Override
	public String toString(){
		return "[ top : " + getTop() + " | left : " + getLeft() + " | width : " + getWidth() + " | height : " + getHeight() + "]";
	}
}

