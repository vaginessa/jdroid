package com.jdroid.android.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.WakeLockManager;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public abstract class WorkerService extends IntentService {

	private final static Logger LOGGER = LoggerUtils.getLogger(WorkerService.class);

	private static String TAG = WorkerService.class.getSimpleName();
	private static final String ENABLE_PARTIAL_WAKE_LOCK = "enablePartialWakeLock";
	
	public WorkerService() {
		super(TAG);
	}
	
	public WorkerService(String name) {
		super(name);
	}
	
	@Override
	protected final void onHandleIntent(Intent intent) {
		if (intent != null) {
			String trackingVariable = getTrackingVariable(intent);
			String trackingLabel = getTrackingLabel(intent);
			LOGGER.info("Starting service. Variable: " + trackingVariable + " - Label: " + trackingLabel);
			try {
					long startTime = DateUtils.nowMillis();
					doExecute(intent);
					if (enableTimingTracking()) {
						long executionTime = DateUtils.nowMillis() - startTime;
						AbstractApplication.get().getAnalyticsSender().trackTiming("Service", getTrackingVariable(intent),
								getTrackingLabel(intent), executionTime);
					}
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			} finally {
				if (intent.hasExtra(ENABLE_PARTIAL_WAKE_LOCK)) {
					WakeLockManager.releasePartialWakeLock();
				}
			}
		} else {
			WakeLockManager.releasePartialWakeLock();
			AbstractApplication.get().getExceptionHandler().logWarningException(
				"Null intent when starting the service: " + getClass().getName());
		}
	}

	protected Boolean enableTimingTracking() {
		return true;
	}

	protected String getTrackingVariable(Intent intent) {
		return getClass().getSimpleName();
	}

	protected String getTrackingLabel(Intent intent) {
		return getClass().getSimpleName();
	}

	protected abstract void doExecute(Intent intent);
	
	protected static void runIntentInService(Context context, Intent intent, Class<? extends WorkerService> serviceClass) {
		WorkerService.runIntentInService(context, intent, serviceClass, true);
	}
	
	protected static void runIntentInService(Context context, Intent intent,
			Class<? extends WorkerService> serviceClass, Boolean enablePartialWakeLock) {
		context.startService(getServiceIntent(context, intent, serviceClass, enablePartialWakeLock));
	}
	
	protected static Intent getServiceIntent(Context context, Intent intent,
			Class<? extends WorkerService> serviceClass, Boolean enablePartialWakeLock) {
		if (enablePartialWakeLock) {
			WakeLockManager.acquirePartialWakeLock(context);
			intent.putExtra(ENABLE_PARTIAL_WAKE_LOCK, true);
		}
		intent.setClass(context, serviceClass);
		return intent;
	}
	
}
