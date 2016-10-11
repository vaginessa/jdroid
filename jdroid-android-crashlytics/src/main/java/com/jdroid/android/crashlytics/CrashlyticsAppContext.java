package com.jdroid.android.crashlytics;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.context.AbstractAppContext;

public class CrashlyticsAppContext extends AbstractAppContext {

	public Boolean isCrashlyticsEnabled() {
		return getBuildConfigValue("CRASHLYTICS_ENABLED", false);
	}

	public AnalyticsTracker getAnalyticsTracker() {
		return CrashlyticsTracker.get();
	}
}
