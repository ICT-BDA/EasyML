/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.client.ui.connection;

import bda.studio.client.controller.DiagramController;
import bda.studio.client.ui.widget.shape.Shape;

/**
 * Connection Factory
 */
public class CommonConnectionFactory extends AbstractConnectionFactory {
  public static AbstractConnectionFactory singleton = new CommonConnectionFactory();

  private CommonConnectionFactory() {
  }

  
  @Override
  public Connection buildConnection(DiagramController controller, Shape start,
      Shape end) {
    return new Connection(controller, start, end);
  }
}
