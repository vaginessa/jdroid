package com.jdroid.android.sample.google.analytics;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.google.analytics.GoogleAnalyticsAppModule;
import com.jdroid.android.google.analytics.GoogleAnalyticsTracker;
import com.jdroid.android.sample.analytics.AndroidGoogleAnalyticsTracker;

public class AndroidGoogleAnalyticsAppModule extends GoogleAnalyticsAppModule {

	@Override
	protected AnalyticsTracker createGoogleAnalyticsTracker() {
		return new AndroidGoogleAnalyticsTracker();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		if (getGoogleAnalyticsAppContext().isGoogleAnalyticsEnabled()) {
			getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleAnalyticsTracker.CustomDimension.INSTALLATION_SOURCE.name(), 1);
			getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleAnalyticsTracker.CustomDimension.DEVICE_TYPE.name(), 2);
			getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleAnalyticsTracker.CustomDimension.REFERRER.name(), 3);
			getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleAnalyticsTracker.CustomDimension.DEVICE_YEAR_CLASS.name(), 4);
		}
	}
}
