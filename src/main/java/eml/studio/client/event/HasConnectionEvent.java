/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.event;

import eml.studio.client.ui.connection.Connection;

public interface HasConnectionEvent {
	void onConnectionCancel();

	void onConnectionStart();

	void onConnectionEnd(Connection conn);
}
