package com.jdroid.android.sample.analytics;

import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.List;

public class AppAnalyticsSender extends AnalyticsSender<AppAnalyticsTracker> implements AppAnalyticsTracker {

	public AppAnalyticsSender(List<AppAnalyticsTracker> trackers) {
		super(trackers);
	}
	
	@Override
	public void trackExampleEvent() {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(AppAnalyticsTracker tracker) {
				tracker.trackExampleEvent();
			}
		});
	}

	@Override
	public void trackExampleTransaction() {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(AppAnalyticsTracker tracker) {
				tracker.trackExampleTransaction();
			}
		});
	}

	@Override
	public void trackExampleTiming() {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(AppAnalyticsTracker tracker) {
				tracker.trackExampleTiming();
			}
		});
	}
}
