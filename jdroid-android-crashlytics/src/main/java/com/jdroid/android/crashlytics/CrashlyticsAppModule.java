package com.jdroid.android.crashlytics;

import android.support.v4.util.Pair;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AbstractAppModule;

public class CrashlyticsAppModule extends AbstractAppModule {

	private static final CrashlyticsAppModule INSTANCE = new CrashlyticsAppModule();

	private CrashlyticsAppContext crashlyticsAppContext;

	public static CrashlyticsAppModule get() {
		return INSTANCE;
	}

	private CrashlyticsAppModule() {
		crashlyticsAppContext = createCrashlyticsAppContext();
	}

	protected CrashlyticsAppContext createCrashlyticsAppContext() {
		return new CrashlyticsAppContext();
	}

	public CrashlyticsAppContext getCrashlyticsAppContext() {
		return crashlyticsAppContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		AbstractApplication.get().getDebugContext().addCustomDebugInfoProperty(new Pair<String, Object>("Crashlytics Enabled", crashlyticsAppContext.isCrashlyticsEnabled()));
	}
}
