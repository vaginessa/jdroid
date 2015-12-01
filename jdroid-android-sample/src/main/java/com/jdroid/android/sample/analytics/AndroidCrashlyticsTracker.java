package com.jdroid.android.sample.analytics;

import android.app.Activity;

import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.crashlytics.CrashlyticsTracker;

import java.util.List;
import java.util.Map;

public class AndroidCrashlyticsTracker extends DefaultAnalyticsTracker {
	
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
	public void trackHandledException(Throwable throwable, List<String> tags) {
		CrashlyticsTracker.get().trackHandledException(throwable, tags);
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