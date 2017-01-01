package com.jdroid.android.google.analytics;

import android.support.annotation.WorkerThread;

import com.jdroid.android.analytics.CoreAnalyticsTracker;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class GoogleAnalyticsAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = GoogleAnalyticsAppModule.class.getName();

	public static GoogleAnalyticsAppModule get() {
		return (GoogleAnalyticsAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private GoogleAnalyticsHelper googleAnalyticsHelper;
	private GoogleAnalyticsAppContext googleAnalyticsAppContext;

	public GoogleAnalyticsAppModule() {
		googleAnalyticsAppContext = createGoogleAnalyticsAppContext();
	}

	protected GoogleAnalyticsAppContext createGoogleAnalyticsAppContext() {
		return new GoogleAnalyticsAppContext();
	}

	public GoogleAnalyticsAppContext getGoogleAnalyticsAppContext() {
		return googleAnalyticsAppContext;
	}

	@WorkerThread
	public synchronized GoogleAnalyticsHelper getGoogleAnalyticsHelper() {
		if (googleAnalyticsHelper == null) {
			googleAnalyticsHelper = createGoogleAnalyticsHelper();
		}
		return googleAnalyticsHelper;
	}

	protected GoogleAnalyticsHelper createGoogleAnalyticsHelper() {
		return new GoogleAnalyticsHelper();
	}

	@Override
	public List<? extends CoreAnalyticsTracker> createCoreAnalyticsTrackers() {
		return googleAnalyticsAppContext.isGoogleAnalyticsEnabled() ? Lists.newArrayList(createGoogleCoreAnalyticsTracker()) : Lists.<CoreAnalyticsTracker>newArrayList();
	}

	protected CoreAnalyticsTracker createGoogleCoreAnalyticsTracker() {
		return new GoogleCoreAnalyticsTracker();
	}
}
