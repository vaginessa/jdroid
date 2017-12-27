package com.jdroid.android.firebase.jobdispatcher;


import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.perf.metrics.Trace;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.performance.TraceHelper;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public abstract class AbstractJobService extends JobService {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractJobService.class);
	
	@MainThread
	@Override
	public final boolean onStartJob(final JobParameters jobParameters) {
		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				Boolean needsReschedule = false;
				Trace trace = null;
				try {
					String trackingVariable = getTrackingVariable(jobParameters);
					String trackingLabel = getTrackingLabel(jobParameters);
					
					if (timingTrackingEnabled()) {
						trace = TraceHelper.startTrace(trackingLabel);
					}
					LOGGER.info("Starting service. Variable: " + trackingVariable + " - Label: " + trackingLabel);
					long startTime = DateUtils.nowMillis();
					needsReschedule = onRunJob(jobParameters);
					long executionTime = DateUtils.nowMillis() - startTime;
					LOGGER.debug("Finished service. Variable: " + trackingVariable + " - Label: " + trackingLabel + ". Execution time: " + DateUtils.formatDuration(executionTime));
				} catch (ConnectionException e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
					needsReschedule = true;
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
					needsReschedule = false;
				} finally {
					if (trace != null) {
						trace.stop();
					}
					jobFinished(jobParameters, needsReschedule);
				}
			}
		});
		return true;
	}
	
	protected Boolean timingTrackingEnabled() {
		return true;
	}
	
	@WorkerThread
	public abstract boolean onRunJob(JobParameters jobParameters);
	
	protected String getTrackingVariable(JobParameters jobParameters) {
		return getClass().getSimpleName();
	}
	
	protected String getTrackingLabel(JobParameters jobParameters) {
		return getClass().getSimpleName();
	}
	
	@MainThread
	@Override
	public boolean onStopJob(JobParameters jobParameters) {
		return true;
	}
}
