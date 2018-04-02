/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface StudioUIMessages extends Messages {

	String yes();
	String no();
	String submit();
	String clear();
	String clone();
	String refresh();
	String runHistory();
	String confirm();
	String cancel();
	String stop();
	String createJob();

	String program();
	String data();
	String job();


	String systemProgram();
	String systemData();
	String preprocess();
	String standalone();
	String modelDistributed();
	String dataDistributed();
	String distributed();
	String evaluation();
	String transformation();
	String machineLearning();
	String deepLearning();
	String textAnalysis();
	String recommendation();
	String graphAnalysis();
	String classification();
	String clusterAnalysis();
	String structuralAnalysis();
	String sharedData();
	String sharedProgram();
	String myProgram();
	String myData();
	String chooseCategory();

	String examples();
	String myJobs();
	String otherJobs();
	String finished();
	String running();
	String historyView();
	String historyDelete();
	String historySearch();
	String historyBatchDel();
	String historySelectAll();
	String historyDelHint();


	String jobPanelTitle();
	String jobName();
	String jobOwner();
	String jobId();
	String jobStatus();
	String startTime();
	String endTime();
	String useTime();
	String jobDescription();
	String cronSetting();


	String modulePanelTitle();
	String moduleName();
	String moduleDesription();
	String moduleDeterminacy();
	String moduleVersion();
	String moduleCreateTime();
	String moduleOwner();
	String moduleDeprecated();
	String parameter();
	String type();
	String value();
	String moduleCategory();
	String moduleType();
	String programable();
	String moduleCMDFormat();
	String tensorflowMode();

	String dataName();
	String dataCategory();
	String dataType();
	String dataStorage();
	String dataDescription();
	String dataVersion();
	String dataCreateTime();
	String dataOwner();
	String dataDeprecated();

	String commandLine();
	String defaultValue();
	String add();
	String remove();
	String generate();
	String description();

	//operations on control panel
	String cut();
	String copy();
	String paste();
	String supernode();
	String input();
	String output();
	String back();

	String minute();
	String hour();
	String day();
	String month();
	String week();
}

