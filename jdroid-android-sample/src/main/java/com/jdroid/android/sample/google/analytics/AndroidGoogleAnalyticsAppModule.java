package com.jdroid.android.sample.google.analytics;

import com.jdroid.android.google.analytics.GoogleAnalyticsAppModule;
import com.jdroid.android.google.analytics.GoogleCoreAnalyticsTracker;

public class AndroidGoogleAnalyticsAppModule extends GoogleAnalyticsAppModule {

	@Override
	public void onCreate() {
		super.onCreate();

		if (getGoogleAnalyticsAppContext().isGoogleAnalyticsEnabled()) {
			getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleCoreAnalyticsTracker.CustomDimension.INSTALLATION_SOURCE.name(), 1);
			getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleCoreAnalyticsTracker.CustomDimension.DEVICE_TYPE.name(), 2);
			getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleCoreAnalyticsTracker.CustomDimension.REFERRER.name(), 3);
			getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleCoreAnalyticsTracker.CustomDimension.DEVICE_YEAR_CLASS.name(), 4);
		}
	}
}
