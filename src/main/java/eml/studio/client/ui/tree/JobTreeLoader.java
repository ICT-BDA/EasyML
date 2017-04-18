/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.tree;

import java.util.List;
import java.util.logging.Logger;

import eml.studio.client.rpc.JobService;
import eml.studio.client.rpc.JobServiceAsync;
import eml.studio.shared.model.BdaJob;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Load job menus for a user
 */
public class JobTreeLoader {

	private static Logger logger = Logger.getLogger(JobTreeLoader.class
			.getName());

	private static JobServiceAsync srv = GWT.create(JobService.class);

	public static JobTree load(String userEmail) {
		final JobTree tree = new JobTree();

		// load example jobs
		srv.listRecentExampleJobs(new AsyncCallback<List<BdaJob>>() {

			public void onFailure(Throwable caught) {
				logger.warning(caught.getMessage());
			}

			public void onSuccess(List<BdaJob> result) {
				logger.info("Oozie example job NUM: " + result.size());
				for (BdaJob job : result)
					tree.addExampleJob(job);
			}
		});

		// load private jobs
		srv.listRecentUserJobs(userEmail, new AsyncCallback<List<BdaJob>>() {
			public void onFailure(Throwable caught) {
				logger.warning(caught.getMessage());
			}

			public void onSuccess(List<BdaJob> result) {
				logger.info("Oozie user job NUM: " + result.size());
				for (BdaJob job : result) {
					if (job.getCurOozJob() == null) {
						continue;
					}
					tree.addMyJob(job);
				}
			}
		});

		// load other jobs
		srv.listRecentJobs(new AsyncCallback<List<BdaJob>>() {

			public void onFailure(Throwable caught) {
				logger.warning(caught.getMessage());
			}

			public void onSuccess(List<BdaJob> result) {
				for (BdaJob job : result)
					tree.addOtherJob(job);
			}
		});

		return tree;
	}
}
