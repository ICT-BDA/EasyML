/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.shape;


import eml.studio.client.ui.connection.Connection;

import com.orange.links.client.shapes.Drawable;

/**
 * Interface to represent a point, widget or mouse as a rectangle
 */
public interface Shape extends Drawable {

	/**
	 *
	 * @return left margin
	 */
	int getLeft();

	/**
	 *
	 * @return top margin
	 */
	int getTop();

	/**
	 *
	 * @return width of the shape
	 */
	int getWidth();

	/**
	 *
	 * @return height of the shape
	 */
	int getHeight();

	/**
	 * Add a connection to this shape.
	 *
	 * @return whether or not a connection was added
	 */
	boolean addConnection(Connection connection);

	/**
	 * Remove a connection to this shape.
	 *
	 * @return whether or not a connection was removed
	 */
	boolean removeConnection(Connection connection);

}