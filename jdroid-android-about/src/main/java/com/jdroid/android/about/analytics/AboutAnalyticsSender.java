package com.jdroid.android.about.analytics;

import com.jdroid.java.analytics.AnalyticsSender;

import java.util.List;

public class AboutAnalyticsSender extends AnalyticsSender<AboutAnalyticsTracker> implements AboutAnalyticsTracker {

	public AboutAnalyticsSender(List<AboutAnalyticsTracker> trackers) {
		super(trackers);
	}

	@Override
	public void trackAboutLibraryOpen(final String libraryKey) {
		execute(new TrackingCommand() {

			@Override
			protected void track(AboutAnalyticsTracker tracker) {
				tracker.trackAboutLibraryOpen(libraryKey);
			}
		});
	}

	@Override
	public void trackContactUs() {
		execute(new TrackingCommand() {

			@Override
			protected void track(AboutAnalyticsTracker tracker) {
				tracker.trackContactUs();
			}
		});
	}
}
