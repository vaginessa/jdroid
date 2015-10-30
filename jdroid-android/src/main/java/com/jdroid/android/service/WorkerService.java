package com.jdroid.android.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.WakeLockManager;
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
		LOGGER.info("Starting service: " + getClass().getSimpleName());
		if (intent != null) {
			try {
					long startTime = System.currentTimeMillis();
					doExecute(intent);
					long executionTime = System.currentTimeMillis() - startTime;
					AbstractApplication.get().getAnalyticsSender().trackTiming("Service", getClass().getSimpleName(),
							getClass().getSimpleName(), executionTime);


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
