package com.jdroid.android.sample.analytics;

import com.jdroid.java.analytics.BaseAnalyticsSender;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;

public class AppAnalyticsSender extends BaseAnalyticsSender<AppAnalyticsTracker> implements AppAnalyticsTracker {

	private static final AppAnalyticsSender INSTANCE = new AppAnalyticsSender();

	public static AppAnalyticsSender get() {
		return INSTANCE;
	}

	public AppAnalyticsSender() {
		super(Lists.newArrayList(new AndroidFirebaseAnalyticsTracker(), new AndroidGoogleAnalyticsTracker()));
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
