package com.jdroid.android.firebase.admob.analytics;

import com.jdroid.android.google.analytics.AbstractGoogleAnalyticsTracker;

public class GoogleAdMobAnalyticsTracker extends AbstractGoogleAnalyticsTracker implements AdMobAnalyticsTracker {

	private static final String ADS_CATEGORY = "ads";
	private static final String CLICK_ACTION = "click";

	@Override
	public void trackRemoveAdsBannerClicked() {
		getGoogleAnalyticsHelper().sendEvent(ADS_CATEGORY, CLICK_ACTION, "removeAds");
	}
}
