package com.jdroid.android.sample.exception;

import com.jdroid.android.crashlytics.CrashlyticsAppContext;
import com.jdroid.android.crashlytics.CrashlyticsAppModule;

public class AndroidCrashlyticsAppModule extends CrashlyticsAppModule {

	@Override
	protected CrashlyticsAppContext createCrashlyticsAppContext() {
		return new AndroidCrashlyticsAppContext();
	}
}
