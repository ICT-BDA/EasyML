/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.property;

import eml.studio.client.util.Constants;
import eml.studio.shared.model.Dataset;

/**
 * The property table of a dataset widget, shown in the
 * right of the main panel.
 */
public class DatasetPropertyTable extends PropertyTable {

	public DatasetPropertyTable(Dataset dataset) {
		super(7, 2);
		addProperties(dataset);
	}

	private void addProperties(Dataset dataset) {
		int i = 0;
		Property p = null;
		p = new Property(Constants.studioUIMsg.dataType(), dataset.getContenttype());
		addProperty(p, i++);

		p = new Property(Constants.studioUIMsg.dataStorage(), dataset.getStoretype());
		addProperty(p, i++);

		p = new Property(Constants.studioUIMsg.dataDescription(), dataset.getDescription());
		addProperty(p, i++);

		p = new Property(Constants.studioUIMsg.dataVersion(), dataset.getVersion());
		addProperty(p, i++);

		p = new Property(Constants.studioUIMsg.dataCreateTime(), dataset.getCreatedate());
		addProperty(p, i++);

		p = new Property(Constants.studioUIMsg.dataOwner(), dataset.getOwner());
		addProperty(p, i++);

		if (!dataset.getDeprecated())
			p = new Property(Constants.studioUIMsg.dataDeprecated(), Constants.studioUIMsg.no());
		else
			p = new Property(Constants.studioUIMsg.dataDeprecated(), Constants.studioUIMsg.yes());
		addProperty(p, i++);
	}
}
