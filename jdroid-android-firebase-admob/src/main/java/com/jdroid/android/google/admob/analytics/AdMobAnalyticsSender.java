package com.jdroid.android.google.admob.analytics;

import com.jdroid.java.analytics.BaseAnalyticsSender;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.List;

public class AdMobAnalyticsSender extends BaseAnalyticsSender<AdMobAnalyticsTracker> implements AdMobAnalyticsTracker {

	public AdMobAnalyticsSender(List<AdMobAnalyticsTracker> trackers) {
		super(trackers);
	}

	@Override
	public void trackRemoveAdsBannerClicked() {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(AdMobAnalyticsTracker tracker) {
				tracker.trackRemoveAdsBannerClicked();
			}
		});
	}
}
