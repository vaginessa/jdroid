package com.jdroid.android.sample.analytics;

import android.app.Activity;

import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.crashlytics.CrashlyticsTracker;

import java.util.Map;

public class AndroidCrashlyticsTracker extends DefaultAnalyticsTracker {
	
	private static final AndroidCrashlyticsTracker INSTANCE = new AndroidCrashlyticsTracker();
	
	public static AndroidCrashlyticsTracker get() {
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return CrashlyticsTracker.get().isEnabled();
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#onInitExceptionHandler(java.util.Map)
	 */
	@Override
	public void onInitExceptionHandler(Map<String, String> metadata) {
		CrashlyticsTracker.get().onInitExceptionHandler(metadata);
	}
	
	@Override
	public void trackHandledException(Throwable throwable, int priority) {
		CrashlyticsTracker.get().trackHandledException(throwable, priority);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#onActivityStart(java.lang.Class,
	 *      com.jdroid.android.analytics.AppLoadingSource, java.lang.Object)
	 */
	@Override
	public void onActivityStart(Class<? extends Activity> activityClass, AppLoadingSource appLoadingSource, Object data) {
		CrashlyticsTracker.get().onActivityStart(activityClass, appLoadingSource, data);
	}
}