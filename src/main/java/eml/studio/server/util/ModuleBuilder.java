/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.server.util;

import eml.studio.server.db.SecureDao;
import eml.studio.shared.model.Dataset;
import com.google.gwt.user.client.rpc.SerializationException;

import java.util.List;

public class ModuleBuilder {

	/**
	 * Generate dataset module based on path and id
	 *
	 * @param path
	 *          dataset path
	 * @param id
	 *          dataset uuid
	 * @return Dataset
	 * @throws Exception
	 */
	public static Dataset buildDataset(String path, String id) throws Exception {
		if (HDFSIO.exist(path)) {
			Dataset dataset = new Dataset();
			List list;
			try {
				Dataset temp = SecureDao.getObject(dataset, " and id = '" + id + "'");
				if ( temp != null ) {
					dataset = temp;
				} else {
					dataset.setId(id);
					dataset.setName("id" + id);
					dataset.setPath(path);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SerializationException(e);
			}
			return dataset;
		} else {
			System.out.println("DataSet is return null");
			return null;
		}
	}
}
