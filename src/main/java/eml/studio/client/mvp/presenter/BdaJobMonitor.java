/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.mvp.presenter;

import java.util.List;
import java.util.logging.Logger;

import eml.studio.client.controller.MonitorController;
import eml.studio.client.rpc.JobService;
import eml.studio.client.rpc.JobServiceAsync;
import eml.studio.client.util.TimeUtils;
import eml.studio.shared.model.BdaJob;
import eml.studio.shared.oozie.OozieAction;
import eml.studio.shared.oozie.OozieJob;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Monitor class of EML job
 */
public class BdaJobMonitor {
	protected static JobServiceAsync jobSrv = GWT.create(JobService.class);

	private static Logger logger = Logger.getLogger(BdaJobMonitor.class.getName());

	private MonitorPresenter presenter = null;
	private MonitorController controller = null;

	private int refressRate = 10000;

	/**
	 * This timer is used to update job status in refress rate
	 */
	private class UpdateJobTimer extends Timer {
		private boolean canceled = true;

		@Override
		public void run() {
			logger.info("Timer was started...");
			updateCurrentJob();
		}

		@Override
		public void scheduleRepeating(int periodMillis) {
			super.scheduleRepeating(periodMillis);
			canceled = false;
		}

		@Override
		public void cancel() {
			if (!canceled)
				super.cancel();
			canceled = true;
		}
	}

	private UpdateJobTimer updateJobTimer = new UpdateJobTimer();


	public BdaJobMonitor(MonitorPresenter presenter) {
		this.presenter = presenter;
		controller = presenter.getView().getController();
	}

	/**
	 * After submit bdajob, do monitor task
	 * @param bdaJob
	 */
	public void afterSubmit(BdaJob bdaJob){
		updateJobTimer.scheduleRepeating(refressRate);
		(presenter).updateJobIFView();
		(controller).updateWgtCurOozJobId( bdaJob.getCurOozJobId() );
		updateCurrentJob();
	}

	/**
	 * According to the synchronization of the current oozJob
	 * update task view information
	 * @param oozJob
	 */
	private void updateView(OozieJob oozJob){
		final MonitorPresenter presenter =  this.presenter;

		presenter.getView().getJobDescGrid().setJobStatus(oozJob.getStatus());
		String createdTime = TimeUtils.format(oozJob.getCreatedTime());
		presenter.getView().getJobDescGrid().setStartTime(createdTime);
		String endTime = TimeUtils.format(oozJob.getEndTime());
		presenter.getView().getJobDescGrid().setEndTime( endTime );

		String exeTime = TimeUtils.timeDiff( oozJob.getCreatedTime(), 
				oozJob.getEndTime() );
		if (exeTime == null) {
			presenter.getView().getJobDescGrid().setUseTime("");
		} else {
			presenter.getView().getJobDescGrid().setUseTime(exeTime);
		}
	}

	/**
	 * Update current Job
	 */
	public void updateCurrentJob() {
		final MonitorPresenter presenter =  this.presenter;
		if(presenter.isInstanceFlag()) //OozieJob  instance
		{
			jobSrv.synCurOozJobByOozId( presenter.getCurrentOozieJob().getId(), new AsyncCallback<OozieJob>() {

				@Override
				public void onFailure(Throwable caught) {
					logger.warning("job status getJob failed!");
				}

				@Override
				public void onSuccess(OozieJob result) {
					if( result.getActions() != null  ){
						logger.info("Executed Actions Number: " + result.getActions().size());
						//Update oozie job related action dynamically
						controller.updateWidgetStatus(result.getActions());
					}else{
						updateJobTimer.cancel();
						presenter.unlockSubmit();
						logger.info("Executed Actions is null");
						return;
					}
					//Update job view
					updateView(result);
					//Update the current oozie job
					presenter.setCurrentOozieJob(result);
					presenter.getCurrentJob().setCurOozJob( result );
					if (presenter.getCurrentOozieJob().getEndTime() != null) {
						updateJobTimer.cancel();
						presenter.unlockSubmit();
						logger.info("Timer was canceled");
					}
				}
			});
		}else{		
			jobSrv.synCurOozJob( presenter.getCurrentJob().getJobId(), new AsyncCallback<OozieJob>() {

				@Override
				public void onFailure(Throwable caught) {
					logger.warning("job status getJob failed!");
				}

				@Override
				public void onSuccess(OozieJob result) {
					if( result.getActions() != null  ){
						logger.info("Executed Actions Number: " + result.getActions().size());
						//Dynamically update oozJob related action
						controller.updateWidgetStatus(result.getActions());
					}else{
						updateJobTimer.cancel();
						presenter.unlockSubmit();
						logger.info("Executed Actions is null");
						return;
					}
					//Update the job view
					updateView(result);
					//Update the current oozie Job
					presenter.getCurrentJob().setCurOozJob( result );
					if (presenter.getCurrentJob().getCurOozJob().getEndTime() != null) {
						updateJobTimer.cancel();
						presenter.unlockSubmit();
						logger.info("Timer was canceled");
					}
				}
			});
		}

	}

	/**
	 * Init current job
	 */
	public void initCurrentJob() {
		final MonitorPresenter presenter =  this.presenter;
		if(presenter.isInstanceFlag())
		{
			//Get non latest submit action
			final List<OozieAction> curActions = presenter.getNonLatestAction(presenter.getCurrentOozieJob().getGraph());
			jobSrv.getSynOozieJob( presenter.getCurrentOozieJob().getId(), new AsyncCallback<OozieJob>() {

				@Override
				public void onFailure(Throwable caught) {
					logger.warning("job status getJob failed!");
				}

				@Override
				public void onSuccess(OozieJob result) {
					logger.info("init current oozie job status");
					presenter.setCurrentOozieJob(result);
					//Current view action include other job finished instance action and submit action
					curActions.addAll(result.getActions());
					result.setActions(curActions);
					controller.updateWidgetStatus(result.getActions());
					//Update the job view
					updateView(result);
					//If the current oozie job has not finished, then start timer to update the status
					if (presenter.getCurrentOozieJob().getEndTime() == null) {
						updateJobTimer.scheduleRepeating(refressRate);
					}else{
						presenter.unlockSubmit();
					}
				}
			});
		}else{
			jobSrv.getBdaJob( presenter.getCurrentJob().getJobId(), new AsyncCallback<BdaJob>() {

				@Override
				public void onFailure(Throwable caught) {
					logger.warning("job status getJob failed!");
				}

				@Override
				public void onSuccess(BdaJob result) {
					logger.info("init current job status");
					presenter.setCurrentJob(result);
					//First loaded, update all programWidget
					controller.updateWidgetStatus(result.getActions());
					//Set the view information
					updateView(result.getCurOozJob());
					//If there is no end time, the scheduled update is initiated
					if (presenter.getCurrentJob().getCurOozJob().getEndTime() == null) {
						updateJobTimer.scheduleRepeating(refressRate);
					}else{
						presenter.unlockSubmit();
					}
				}
			});
		}

	}

	public void afterDAGBuild() {
		if(presenter.isInstanceFlag())
			presenter.updateOozieJobIFView();
		else
			presenter.updateJobIFView();
		initCurrentJob();
	}
}
