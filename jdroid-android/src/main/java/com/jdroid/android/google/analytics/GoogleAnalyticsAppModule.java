package com.jdroid.android.google.analytics;

import android.support.annotation.WorkerThread;

import com.jdroid.android.analytics.AnalyticsTracker;
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
	public List<? extends AnalyticsTracker> createAnalyticsTrackers() {
		return googleAnalyticsAppContext.isGoogleAnalyticsEnabled() ? Lists.newArrayList(createGoogleAnalyticsTracker()) : Lists.<AnalyticsTracker>newArrayList();
	}

	protected AnalyticsTracker createGoogleAnalyticsTracker() {
		return new GoogleAnalyticsTracker();
	}
}
