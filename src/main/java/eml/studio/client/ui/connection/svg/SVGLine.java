/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.connection.svg;

import eml.studio.client.ui.widget.shape.Point;

import com.google.gwt.dom.client.Element;

import java.util.logging.Logger;

/**
 * SVG line attached to {@link SVGPanel}
 */
public class SVGLine {
	private static Logger logger = Logger.getLogger(SVGLine.class.getName());
	private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
	Element line;

	public SVGLine() {
		line = createElementNS(SVG_NAMESPACE, "line");
		line.getStyle().setProperty("stroke", "#707070");
		line.getStyle().setProperty("strokeWidth", "2");
		line.setAttribute("marker-end", "url(#arrow)");
	}

	public Element asElement() {
		return line;
	}

	public void draw(Point p1, Point p2) {
		logger.info(p1.toString() + " " + p2.toString());
		line.setAttribute("x1", "0" + p1.getLeft());
		line.setAttribute("x2", "0" + p2.getLeft());
		line.setAttribute("y1", "0" + p1.getTop());
		line.setAttribute("y2", "0" + p2.getTop());
	}

	private static native Element createElementNS(final String ns,
			final String name)/*-{
								return document.createElementNS(ns, name);
								}-*/;
}