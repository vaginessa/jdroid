package com.jdroid.android.google.analytics;

import android.support.annotation.WorkerThread;

import com.jdroid.java.analytics.BaseAnalyticsTracker;

public class AbstractGoogleAnalyticsTracker implements BaseAnalyticsTracker {

	@Override
	public Boolean isEnabled() {
		return GoogleAnalyticsAppModule.get().getGoogleAnalyticsAppContext().isGoogleAnalyticsEnabled();
	}

	@WorkerThread
	protected GoogleAnalyticsHelper getGoogleAnalyticsHelper() {
		return GoogleAnalyticsAppModule.get().getGoogleAnalyticsHelper();
	}
}
