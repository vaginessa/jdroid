package com.jdroid.android.sample.analytics;

import com.jdroid.android.sample.firebase.analytics.FirebaseAppAnalyticsTracker;
import com.jdroid.android.sample.google.analytics.AppGoogleAnalyticsTracker;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;

public class AppAnalyticsSender extends AnalyticsSender<AppAnalyticsTracker> implements AppAnalyticsTracker {

	private static final AppAnalyticsSender INSTANCE = new AppAnalyticsSender();

	public static AppAnalyticsSender get() {
		return INSTANCE;
	}

	public AppAnalyticsSender() {
		super(Lists.newArrayList(new FirebaseAppAnalyticsTracker(), new AppGoogleAnalyticsTracker()));
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
