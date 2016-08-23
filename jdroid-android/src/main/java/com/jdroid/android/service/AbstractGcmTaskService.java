package com.jdroid.android.service;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.exception.ConnectionException;
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
		try {
			String trackingVariable = getTrackingVariable(taskParams);
			String trackingLabel = getTrackingLabel(taskParams);
			LOGGER.info("Starting service. Variable: " + trackingVariable + " - Label: " + trackingLabel);
			long startTime = DateUtils.nowMillis();
			int result = doRunTask(taskParams);
			if (result == GcmNetworkManager.RESULT_SUCCESS) {
				long executionTime = DateUtils.nowMillis() - startTime;
				AbstractApplication.get().getAnalyticsSender().trackServiceTiming(trackingVariable, trackingLabel, executionTime);
			}
			return result;
		} catch (ConnectionException e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
			return GcmNetworkManager.RESULT_RESCHEDULE;
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
			return GcmNetworkManager.RESULT_FAILURE;
		}
	}

	public abstract int doRunTask(TaskParams taskParams);

	protected String getTrackingVariable(TaskParams taskParams) {
		return getClass().getSimpleName();
	}

	protected String getTrackingLabel(TaskParams taskParams) {
		return getClass().getSimpleName();
	}
}
