/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.shape;

import com.google.gwt.core.client.GWT;
import com.orange.links.client.utils.Direction;
import java.util.HashSet;
import java.util.Set;

/**
 * Connection utils in canvas
 */
public class ConnectionUtils {

	public static Segment computeFasterSegment(Shape startShape, Shape endShape){
		Direction[] d = computeDirections(startShape, endShape);

		Point p1 = pointOnBorder(d[0],startShape);
		Point p2 = pointOnBorder(d[1],endShape);

		return new Segment(p1, p2);
	}

	private static Direction[] computeDirections(Shape s1, Shape s2) {
		Direction[] d1 = Direction.getAll();
		Direction[] d2 = Direction.getAll();

		Direction[] bestDirections = new Direction[2];
		double best = Double.MAX_VALUE;
		for (int i = 0; i < d1.length; i++) {
			for (int j = 0; j < d2.length; j++) {
				double actual = pointOnBorder(d1[i],s1).distance(pointOnBorder(d2[j],s2));
				if( actual < best ){
					best = actual;
					bestDirections[0] = d1[i];
					bestDirections[1] = d2[j];
				}
			}
		}
		return bestDirections;
	}

	public static Point middle(Point p1, Point p2){
		return new Point((p1.getLeft()+p2.getLeft())/2,(p1.getTop()+p2.getTop())/2);
	}

	public static boolean isLinkDiagonal(Rectangle r1, Rectangle r2){

		if(    (r1.getCornerTopLeft().getLeft() > r2.getCornerBottomRight().getLeft()) && (r1.getCornerTopLeft().getTop() > r2.getCornerBottomRight().getTop())
				|| (r1.getCornerTopRight().getLeft() < r2.getCornerBottomLeft().getLeft()) && (r1.getCornerTopRight().getTop() > r2.getCornerBottomLeft().getTop())
				|| (r1.getCornerBottomRight().getLeft() < r2.getCornerTopLeft().getLeft()) && (r1.getCornerBottomRight().getTop() < r2.getCornerTopLeft().getTop())
				|| (r1.getCornerBottomLeft().getLeft() > r2.getCornerTopRight().getLeft()) && (r1.getCornerBottomLeft().getTop() < r2.getCornerTopRight().getTop())
				)
		{
			// Link is Diagonal
			return true;
		}

		return false;
	}

	public static Segment computeSegment(Shape s1, Shape s2) {
		return computeSegment(new Rectangle(s1), new Rectangle(s2));
	}


	public static Segment computeSegment(Rectangle r1, Rectangle r2) {

		Point p1 = null;
		Point p2 = null;

		if(isLinkDiagonal(r1, r2)){
			if((r1.getCornerTopLeft().getLeft() >= r2.getCornerBottomRight().getLeft()) && (r1.getCornerTopLeft().getTop() >= r2.getCornerBottomRight().getTop())){
				p1 = r1.getCornerTopLeft();
				p2 = r2.getCornerBottomRight();
			}
			else if ((r1.getCornerTopRight().getLeft() <= r2.getCornerBottomLeft().getLeft()) && (r1.getCornerTopRight().getTop() >= r2.getCornerBottomLeft().getTop())){
				p1 = r1.getCornerTopRight();
				p2 = r2.getCornerBottomLeft();
			}
			else if ((r1.getCornerBottomRight().getLeft() <= r2.getCornerTopLeft().getLeft()) && (r1.getCornerBottomRight().getTop() <= r2.getCornerTopLeft().getTop())){
				p1 = r1.getCornerBottomRight();
				p2 = r2.getCornerTopLeft();
			}
			else if ((r1.getCornerBottomLeft().getLeft() >= r2.getCornerTopRight().getLeft()) && (r1.getCornerBottomLeft().getTop() <= r2.getCornerTopRight().getTop())){
				p1 = r1.getCornerBottomLeft();
				p2 = r2.getCornerTopRight();
			}
			return new Segment(p1, p2);
		}

		// Find the direction (Vertical or Horizontal)
		if(r1.getCornerBottomLeft().getTop() < r2.getCornerTopLeft().getTop()){
			// DOWN
			if(r1.getBorderBottom().length() < r2.getBorderTop().length()){
				// R1 is smaller than R2
				Segment tempSegment = project(r1.getBorderBottom(), r2.getBorderTop());
				if(tempSegment == null){
					GWT.log("gwt-debug : The projection has been aborted");
				}
				Point middlePoint = middle(tempSegment.getP1(),tempSegment.getP2());
				p1 = new Point(middlePoint.getLeft(),r1.getCornerBottomRight().getTop()-1);
				p2 = new Point(p1.getLeft(),r2.getCornerTopLeft().getTop());
			}else{
				Segment tempSegment = project(r2.getBorderTop(), r1.getBorderBottom());
				Point middlePoint = middle(tempSegment.getP1(),tempSegment.getP2());
				p2 = new Point(middlePoint.getLeft(),r2.getCornerTopRight().getTop()-1);
				p1 = new Point(p2.getLeft(),r1.getCornerBottomLeft().getTop());
			}
			return new Segment(p1,p2);
		}
		else if(r1.getCornerTopLeft().getTop() > r2.getCornerBottomLeft().getTop()){
			// UP
			if(r1.getBorderTop().length() < r2.getBorderBottom().length()){
				// R1 is smaller than R2
				Segment tempSegment = project(r1.getBorderTop(), r2.getBorderBottom());
				Point middlePoint = middle(tempSegment.getP1(),tempSegment.getP2());
				p1 = new Point(middlePoint.getLeft(),r1.getCornerTopRight().getTop());
				p2 = new Point(p1.getLeft(),r2.getCornerBottomLeft().getTop());
			}else{
				Segment tempSegment = project( r2.getBorderBottom(), r1.getBorderTop());
				Point middlePoint = middle(tempSegment.getP1(),tempSegment.getP2());
				p2 = new Point(middlePoint.getLeft(),r2.getCornerBottomRight().getTop());
				p1 = new Point(p2.getLeft(),r1.getCornerTopLeft().getTop());
			}
			return new Segment(p1,p2);
		}
		else if(r1.getCornerTopRight().getLeft() > r2.getCornerTopLeft().getLeft()){
			// LEFT
			if(r1.getBorderRight().length() < r2.getBorderLeft().length()){
				// R1 is smaller than R2
				Segment tempSegment = project(r1.getBorderLeft(), r2.getBorderRight());
				Point middlePoint = middle(tempSegment.getP1(),tempSegment.getP2());
				p1 = new Point(r1.getCornerTopLeft().getLeft(),middlePoint.getTop());
				p2 = new Point(r2.getCornerBottomRight().getLeft(),p1.getTop());
			}else{
				Segment tempSegment = project(r2.getBorderRight(), r1.getBorderLeft());
				Point middlePoint = middle(tempSegment.getP1(),tempSegment.getP2());
				p2 = new Point(r2.getCornerTopRight().getLeft(),middlePoint.getTop());
				p1 = new Point(r1.getCornerBottomLeft().getLeft(),p2.getTop());
			}
			return new Segment(p1,p2);
		}
		else if(r1.getCornerTopLeft().getLeft() < r2.getCornerTopRight().getLeft()){
			// RIGHT
			if(r1.getBorderRight().length() < r2.getBorderLeft().length()){
				// R1 is smaller than R2
				Segment tempSegment = project(r1.getBorderRight(), r2.getBorderLeft());
				Point middlePoint = middle(tempSegment.getP1(),tempSegment.getP2());
				p1 = new Point(r1.getCornerBottomRight().getLeft(),middlePoint.getTop());
				p2 = new Point(r2.getCornerBottomLeft().getLeft(),p1.getTop());
			}else{
				Segment tempSegment = project(r2.getBorderRight(), r1.getBorderLeft());
				Point middlePoint = middle(tempSegment.getP1(),tempSegment.getP2());
				p2 = new Point(r2.getCornerTopLeft().getLeft(),middlePoint.getTop());
				p1 = new Point(r1.getCornerBottomRight().getLeft(),p2.getTop());
			}
			return new Segment(p1,p2);
		}

		return null;
	}

	/**
	 * Use to compute the projection of s1 on s2.
	 * S1 and s2 must be vertical or horizontal
	 *
	 * @return the projected segment on s2
	 */
	public static Segment project(Segment s1, Segment s2){
		if(s1.getP1().getLeft() == s1.getP2().getLeft() && s2.getP1().getLeft() == s2.getP2().getLeft()){
			// Lines vertical
			Point p1;
			Point p2;
			if(s1.getP2().getTop() >= s2.getP2().getTop()){
				// S1 on the bottom of S2
				if(s1.getP1().getTop() > s2.getP2().getTop()){
					// No intersection
					return null;
				}
				p1 = new Point(s2.getP1().getLeft(),s1.getP1().getTop());
				p2 = new Point(s2.getP1().getLeft(),s2.getP2().getTop());
				return new Segment(p1,p2);
			}
			else if(s1.getP1().getTop() <= s2.getP1().getTop()){
				// S1 on the Top of S2
				if(s1.getP2().getTop() < s2.getP1().getTop()){
					// No intersection
					return null;
				}
				p1 = new Point(s2.getP1().getLeft(),s2.getP1().getTop());
				p2 = new Point(s2.getP1().getLeft(),s1.getP2().getTop());
				return new Segment(p1,p2);
			}
			else{
				// S1 inside S2
				p1 = new Point(s2.getP1().getLeft(),s1.getP1().getTop());
				p2 = new Point(s2.getP1().getLeft(),s1.getP2().getTop());
				return new Segment(p1,p2);
			}
		}
		else if (s1.getP1().getTop() == s1.getP2().getTop() && s2.getP1().getTop() == s2.getP2().getTop()) {
			// Lines horizontal
			Point p1;
			Point p2;
			if(s1.getP1().getLeft() <= s2.getP1().getLeft()){
				// S1 on the left of S2
				if(s1.getP2().getLeft() < s2.getP1().getLeft()){
					// No intersection
					return null;
				}
				p1 = new Point(s2.getP1().getLeft(),s2.getP1().getTop());
				p2 = new Point(s1.getP2().getLeft(),s2.getP1().getTop());
				return new Segment(p1,p2);
			}
			else if(s1.getP2().getLeft() >= s2.getP2().getLeft()){
				// S1 on the right of S2
				if(s1.getP1().getLeft() > s2.getP2().getLeft()){
					// No intersection
					return null;
				}
				p1 = new Point(s1.getP1().getLeft(),s2.getP1().getTop());
				p2 = new Point(s2.getP2().getLeft(),s2.getP1().getTop());
				return new Segment(p1,p2);
			}
			else{
				// S1 inside S2
				p1 = new Point(s1.getP1().getLeft(),s2.getP1().getTop());
				p2 = new Point(s1.getP2().getLeft(),s2.getP1().getTop());
				return new Segment(p1,p2);
			}
		}
		else {
			throw new IllegalArgumentException("The segment must be parallel and horizontal or vertical");
		}
	};

	public static Set<Point> pointsOnBorder(Shape s){
		Set<Point> pointSet = new HashSet<Point>();
		int offsetLeft = s.getLeft();
		int offsetTop = s.getTop();
		int width = s.getWidth();
		int height = s.getHeight();
		for(int i = offsetLeft; i < offsetLeft+width ; i=i+1){
			Point p1 = new Point(i, offsetTop);
			Point p2 = new Point(i, offsetTop+height);
			pointSet.add(p1);
			pointSet.add(p2);
		}
		for(int i = offsetTop ; i < offsetTop + height ; i=i+1){
			Point p1 = new Point(offsetLeft, i);
			Point p2 = new Point(offsetLeft+width, i);
			pointSet.add(p1);
			pointSet.add(p2);
		}
		return pointSet;
	}

	private static Point pointOnBorder(Direction d, Shape s){

		// Compute distance
		int distance;
		if(d.isHorizontal())
			distance = s.getWidth()/2;
		else if(d.isVertical())
			distance = s.getHeight()/2;
		else
			distance = (int) Math.sqrt( Math.pow(s.getWidth(),2) +  Math.pow(s.getHeight(),2)) /2;

		Point p = new Point(s.getLeft()+s.getWidth()/2,s.getTop()+s.getHeight()/2).move(d,distance);
		p.setDirection(d);
		return p;
	}

	public static double distanceToSegment(Segment s, Point p) {
		return distanceToSegment(s.getP1(), s.getP2(), p);
	}

	public static double distanceToSegment(Point p1, Point p2, Point p3) {

		final double xDelta = p2.getLeft() - p1.getLeft();
		final double yDelta = p2.getTop() - p1.getTop();

		if ((xDelta == 0) && (yDelta == 0)) {
			throw new IllegalArgumentException("p1 and p2 cannot be the same point");
		}

		final double u = ((p3.getLeft() - p1.getLeft()) * xDelta + (p3.getTop() - p1.getTop()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		final Point closestPoint;
		if (u < 0) {
			closestPoint = p1;
		} else if (u > 1) {
			closestPoint = p2;
		} else {
			closestPoint = new Point(p1.getLeft() + u * xDelta,p1.getTop() + u * yDelta);
		}

		return closestPoint.distance(p3);
	}

	public static Point projectionOnSegment(Segment s, Point p) {
		return projectionOnSegment(s.getP1(), s.getP2(), p);
	}

	public static Point projectionOnSegment(Point p1, Point p2, Point p3) {

		final double xDelta = p2.getLeft() - p1.getLeft();
		final double yDelta = p2.getTop() - p1.getTop();

		if ((xDelta == 0) && (yDelta == 0)) {
			throw new IllegalArgumentException("p1 and p2 cannot be the same point");
		}

		final double u = ((p3.getLeft() - p1.getLeft()) * xDelta + (p3.getTop() - p1.getTop()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		final Point closestPoint;
		if (u < 0) {
			closestPoint = p1;
		} else if (u > 1) {
			closestPoint = p2;
		} else {
			closestPoint = new Point(p1.getLeft() + u * xDelta,p1.getTop() + u * yDelta);
		}

		return closestPoint;
	}

}
