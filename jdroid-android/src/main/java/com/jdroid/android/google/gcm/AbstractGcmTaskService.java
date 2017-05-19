package com.jdroid.android.google.gcm;

import android.support.annotation.MainThread;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public abstract class AbstractGcmTaskService extends GcmTaskService {

	private final static Logger LOGGER = LoggerUtils.getLogger(GcmTaskService.class);

	@MainThread
	@Override
	public void onInitializeTasks() {
		LOGGER.info("Initializing task: " + getClass().getSimpleName());
		AbstractApplication.get().initializeGcmTasks();
	}

	@Override
	public final int onRunTask(TaskParams taskParams) {
		Trace trace = null;
		Integer result = null;
		
		try {
			String trackingVariable = getTrackingVariable(taskParams);
			String trackingLabel = getTrackingLabel(taskParams);
			
			if (timingTrackingEnabled()) {
				trace = FirebasePerformance.getInstance().newTrace(trackingLabel);
				trace.start();
			}
			LOGGER.info("Starting service. Variable: " + trackingVariable + " - Label: " + trackingLabel);
			long startTime = DateUtils.nowMillis();
			result = doRunTask(taskParams);
			long executionTime = DateUtils.nowMillis() - startTime;
			LOGGER.debug("Finished service. Variable: " + trackingVariable + " - Label: " + trackingLabel + ". Execution time: " + DateUtils.formatDuration(executionTime));
			
		} catch (ConnectionException e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
			result = GcmNetworkManager.RESULT_RESCHEDULE;
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
			result = GcmNetworkManager.RESULT_FAILURE;
		}
		
		if (trace != null) {
			if (result == GcmNetworkManager.RESULT_SUCCESS) {
				trace.incrementCounter("success");
			} else if (result== GcmNetworkManager.RESULT_FAILURE) {
				trace.incrementCounter("failure");
			} else if (result== GcmNetworkManager.RESULT_RESCHEDULE) {
				trace.incrementCounter("reschedule");
			}
			trace.stop();
		}
		
		return result;
	}
	
	protected Boolean timingTrackingEnabled() {
		return true;
	}

	public abstract int doRunTask(TaskParams taskParams);

	protected String getTrackingVariable(TaskParams taskParams) {
		return getClass().getSimpleName();
	}

	protected String getTrackingLabel(TaskParams taskParams) {
		return getClass().getSimpleName();
	}
}
