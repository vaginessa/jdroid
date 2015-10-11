package com.jdroid.android.sample.exception;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.crashlytics.CrashlyticsAppContext;
import com.jdroid.android.sample.analytics.AndroidCrashlyticsTracker;

public class AndroidCrashlyticsAppContext extends CrashlyticsAppContext {

	@Override
	public AnalyticsTracker getAnalyticsTracker() {
		return new AndroidCrashlyticsTracker();
	}
}
