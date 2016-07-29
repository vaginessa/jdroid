package com.jdroid.android.sample.analytics;

import android.app.Activity;

import com.jdroid.android.crashlytics.CrashlyticsTracker;

import java.util.List;
import java.util.Map;

public class AndroidCrashlyticsTracker extends DefaultAnalyticsTracker {
	
	@Override
	public Boolean isEnabled() {
		return CrashlyticsTracker.get().isEnabled();
	}
	
	@Override
	public void onInitExceptionHandler(Map<String, String> metadata) {
		CrashlyticsTracker.get().onInitExceptionHandler(metadata);
	}
	
	@Override
	public void trackHandledException(Throwable throwable, List<String> tags) {
		CrashlyticsTracker.get().trackHandledException(throwable, tags);
	}
	
	@Override
	public void onActivityStart(Class<? extends Activity> activityClass, String referrer, Object data) {
		CrashlyticsTracker.get().onActivityStart(activityClass, referrer, data);
	}
}