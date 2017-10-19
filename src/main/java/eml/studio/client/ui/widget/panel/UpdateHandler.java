/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.panel;

import com.google.gwt.event.shared.EventHandler;

/**
 * Upadate event handler interface
 */
public interface UpdateHandler extends EventHandler {
	void onUpdate(UpdateEvent event);
}
