package com.jdroid.android.google.analytics;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;

public class GoogleAnalyticsAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = GoogleAnalyticsAppModule.class.getName();

	public static GoogleAnalyticsAppModule get() {
		return (GoogleAnalyticsAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private GoogleAnalyticsHelper googleAnalyticsHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		if (AbstractApplication.get().getAppContext().isGoogleAnalyticsEnabled()) {
			googleAnalyticsHelper = createGoogleAnalyticsHelper();
		}
	}

	protected GoogleAnalyticsHelper createGoogleAnalyticsHelper() {
		return new GoogleAnalyticsHelper();
	}

	public GoogleAnalyticsHelper getGoogleAnalyticsHelper() {
		return googleAnalyticsHelper;
	}
}
