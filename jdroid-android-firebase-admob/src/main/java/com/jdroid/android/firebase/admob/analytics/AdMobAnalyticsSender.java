package com.jdroid.android.firebase.admob.analytics;

import com.jdroid.java.analytics.AnalyticsSender;

import java.util.List;

public class AdMobAnalyticsSender extends AnalyticsSender<AdMobAnalyticsTracker> implements AdMobAnalyticsTracker {

	public AdMobAnalyticsSender(List<AdMobAnalyticsTracker> trackers) {
		super(trackers);
	}

	@Override
	public void trackRemoveAdsBannerClicked() {
		execute(new TrackingCommand() {

			@Override
			protected void track(AdMobAnalyticsTracker tracker) {
				tracker.trackRemoveAdsBannerClicked();
			}
		});
	}
}
