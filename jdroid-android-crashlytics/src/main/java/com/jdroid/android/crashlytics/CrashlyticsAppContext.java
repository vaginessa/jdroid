package com.jdroid.android.crashlytics;

import com.jdroid.android.context.AbstractAppContext;

public class CrashlyticsAppContext extends AbstractAppContext {

	public Boolean isCrashlyticsEnabled() {
		return getBuildConfigValue("CRASHLYTICS_ENABLED", false);
	}
}
