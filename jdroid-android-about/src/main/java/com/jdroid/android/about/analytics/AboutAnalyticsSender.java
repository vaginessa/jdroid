package com.jdroid.android.about.analytics;

import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.List;

public class AboutAnalyticsSender extends AnalyticsSender<AboutAnalyticsTracker> implements AboutAnalyticsTracker {

	public AboutAnalyticsSender(List<AboutAnalyticsTracker> trackers) {
		super(trackers);
	}

	@Override
	public void trackAboutLibraryOpen(final String libraryKey) {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(AboutAnalyticsTracker tracker) {
				tracker.trackAboutLibraryOpen(libraryKey);
			}
		});
	}

	@Override
	public void trackContactUs() {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(AboutAnalyticsTracker tracker) {
				tracker.trackContactUs();
			}
		});
	}
}
