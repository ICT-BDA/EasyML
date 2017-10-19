/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client;

import eml.studio.client.controller.DiagramController;
import eml.studio.client.ui.connection.AbstractConnectionFactory;
import eml.studio.client.ui.connection.Connection;
import eml.studio.client.ui.widget.shape.NodeShape;
import eml.studio.client.ui.widget.shape.Shape;

/**
 *  Diagram widget shape connection factory 
 *
 */
public class OozieConnectionFactory extends AbstractConnectionFactory {

	public static AbstractConnectionFactory singleton = new OozieConnectionFactory();

	private OozieConnectionFactory() {}

	/**
	 * Build diagram widget shape connection
	 * 
	 * @param controller  diagram controller
	 * @param start  start shape point
	 * @param end  end shape point
	 * @return  connection between start and end shape
	 */
	@Override
	public Connection buildConnection(DiagramController controller,
			Shape start,
			Shape end) {
		if (((NodeShape) start).getWidget() == ((NodeShape) end).getWidget())
			return null;
		return new Connection(controller, start, end);
	}
}
