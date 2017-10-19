/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.shape;

/**
 * Draw Segment from P1 to P2
 */
public class Segment {

	private Point p1;
	private Point p2;

	public Segment(Point p1, Point p2){
		this.p1 = p1;
		this.p2 = p2;
	}

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public boolean equals(Segment s){
		return s.getP1().equals(p1) && s.getP2().equals(p2);
	}

	@Override
	public String toString(){
		return  p1 + " , " + p2;
	}

	public double length(){
		return Math.sqrt((p2.getLeft()-p1.getLeft())^2+(p2.getTop()-p1.getTop())^2);
	}

	public void translate(int left, int top){
		p1.translate(left, top);
		p2.translate(left, top);
	}

	public Point middle(){
		return new Point(
				(p2.getLeft()+p1.getLeft())/2,
				(p2.getTop()+p1.getTop())/2
				);
	}

	public double getAngleWithTop(){
		double linkAngle = Math.acos((p2.getLeft()-p1.getLeft())/Math.sqrt(Math.pow(p2.getLeft()-p1.getLeft(),2)+Math.pow(p2.getTop()-p1.getTop(),2)));
		if(p2.getTop()<p1.getTop())
			linkAngle = linkAngle * -1;
		return linkAngle;
	}
}
