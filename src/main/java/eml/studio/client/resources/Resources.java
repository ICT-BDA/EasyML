/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {

	Resources instance = GWT.create(Resources.class);

	@Source("dir_close.png")
	ImageResource getTreeClosed();

	@Source("dir_open.png")
	ImageResource getTreeOpen();

	@Source("dir_close.png")
	ImageResource getTreeLeaf();
}
