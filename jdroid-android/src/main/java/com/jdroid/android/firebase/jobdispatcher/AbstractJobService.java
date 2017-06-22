package com.jdroid.android.firebase.jobdispatcher;


import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public abstract class AbstractJobService extends JobService {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractJobService.class);
	
	@Override
	public final boolean onStartJob(JobParameters jobParameters) {
		Trace trace = null;
		boolean retry;
		
		try {
			String trackingVariable = getTrackingVariable(jobParameters);
			String trackingLabel = getTrackingLabel(jobParameters);
			
			if (timingTrackingEnabled()) {
				trace = FirebasePerformance.getInstance().newTrace(trackingLabel);
				trace.start();
			}
			LOGGER.info("Starting service. Variable: " + trackingVariable + " - Label: " + trackingLabel);
			long startTime = DateUtils.nowMillis();
			retry = onRunJob(jobParameters);
			long executionTime = DateUtils.nowMillis() - startTime;
			LOGGER.debug("Finished service. Variable: " + trackingVariable + " - Label: " + trackingLabel + ". Execution time: " + DateUtils.formatDuration(executionTime));
			
		} catch (ConnectionException e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
			retry = true;
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
			retry = false;
		}
		
		if (trace != null) {
			trace.incrementCounter(retry ? "reschedule" : "success");
			trace.stop();
		}
		
		return retry;
	}
	
	protected Boolean timingTrackingEnabled() {
		return true;
	}
	
	public abstract boolean onRunJob(JobParameters jobParameters);
	
	protected String getTrackingVariable(JobParameters jobParameters) {
		return getClass().getSimpleName();
	}
	
	protected String getTrackingLabel(JobParameters jobParameters) {
		return getClass().getSimpleName();
	}
	
	@Override
	public boolean onStopJob(JobParameters jobParameters) {
		return false;
	}
}
