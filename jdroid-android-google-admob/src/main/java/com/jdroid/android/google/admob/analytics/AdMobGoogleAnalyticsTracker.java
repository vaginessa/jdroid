package com.jdroid.android.google.admob.analytics;

import com.jdroid.android.google.analytics.GoogleAnalyticsAppModule;
import com.jdroid.android.google.analytics.GoogleAnalyticsHelper;

public class AdMobGoogleAnalyticsTracker implements AdMobAnalyticsTracker {

	private static final String ADS_CATEGORY = "ads";
	private static final String CLICK_ACTION = "click";

	private GoogleAnalyticsHelper googleAnalyticsHelper;

	public AdMobGoogleAnalyticsTracker() {
		googleAnalyticsHelper = GoogleAnalyticsAppModule.get().getGoogleAnalyticsHelper();
	}

	@Override
	public Boolean isEnabled() {
		return GoogleAnalyticsAppModule.get().getGoogleAnalyticsAppContext().isGoogleAnalyticsEnabled();
	}

	@Override
	public void trackRemoveAdsBannerClicked() {
		googleAnalyticsHelper.sendEvent(ADS_CATEGORY, CLICK_ACTION, "removeAds");
	}
}
