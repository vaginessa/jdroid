package com.jdroid.android.crashlytics;

import com.jdroid.android.application.AbstractApplication;

public class CrashlyticsAppContext {

	public Boolean isCrashlyticsEnabled() {
		return AbstractApplication.get().getBuildConfigValue("CRASHLYTICS_ENABLED", false);
	}
}
