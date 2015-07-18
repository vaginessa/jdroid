package com.jdroid.android.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.utils.WakeLockManager;

public abstract class WorkerService extends IntentService {
	
	private static String TAG = WorkerService.class.getSimpleName();
	private static final String ENABLE_PARTIAL_WAKE_LOCK = "enablePartialWakeLock";
	
	public WorkerService() {
		super(TAG);
	}
	
	public WorkerService(String name) {
		super(name);
	}
	
	/**
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected final void onHandleIntent(Intent intent) {
		try {
			if (intent != null) {
				long startTime = System.currentTimeMillis();
				doExecute(intent);
				long executionTime = System.currentTimeMillis() - startTime;
				AbstractApplication.get().getAnalyticsSender().trackTiming("Service", getClass().getSimpleName(),
						getClass().getSimpleName(), executionTime);

			}
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		} finally {
			if ((intent == null) || intent.hasExtra(ENABLE_PARTIAL_WAKE_LOCK)) {
				WakeLockManager.releasePartialWakeLock();
			}
		}
		if (intent == null) {
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
