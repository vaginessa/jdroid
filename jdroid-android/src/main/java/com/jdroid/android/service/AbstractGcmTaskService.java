package com.jdroid.android.service;

import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public abstract class AbstractGcmTaskService extends GcmTaskService {

	private final static Logger LOGGER = LoggerUtils.getLogger(GcmTaskService.class);

	@Override
	public void onInitializeTasks() {
		LOGGER.info("Initializing task: " + getClass().getSimpleName());
	}

	@Override
	public final int onRunTask(TaskParams taskParams) {
		String trackingVariable = getTrackingVariable(taskParams);
		String trackingLabel = getTrackingLabel(taskParams);
		LOGGER.info("Starting service. Variable: " + trackingVariable + " - Label: " + trackingLabel);
		long startTime = DateUtils.nowMillis();
		int result = doRunTask(taskParams);
		long executionTime = DateUtils.nowMillis() - startTime;
		AbstractApplication.get().getAnalyticsSender().trackTiming("Service", trackingVariable, trackingLabel, executionTime);
		return result;
	}

	public abstract int doRunTask(TaskParams taskParams);

	protected String getTrackingVariable(TaskParams taskParams) {
		return getClass().getSimpleName();
	}

	protected String getTrackingLabel(TaskParams taskParams) {
		return getClass().getSimpleName();
	}
}
