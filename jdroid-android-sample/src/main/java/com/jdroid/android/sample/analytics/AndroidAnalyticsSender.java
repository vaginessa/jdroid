package com.jdroid.android.sample.analytics;

import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.java.concurrent.ExecutorUtils;

public class AndroidAnalyticsSender extends AnalyticsSender<AndroidAnalyticsTracker> implements AndroidAnalyticsTracker {
	
	private static final AndroidAnalyticsSender INSTANCE = new AndroidAnalyticsSender(AndroidCrashlyticsTracker.get(),
			AndroidGoogleAnalyticsTracker.get());
	
	public static AndroidAnalyticsSender get() {
		return INSTANCE;
	}
	
	private AndroidAnalyticsSender(AndroidAnalyticsTracker... trackers) {
		super(trackers);
	}
	
	/**
	 * @see com.jdroid.android.sample.analytics.AndroidAnalyticsTracker#trackExampleEvent()
	 */
	@Override
	public void trackExampleEvent() {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(AndroidAnalyticsTracker tracker) {
				tracker.trackExampleEvent();
			}
		});
	}

	@Override
	public void trackExampleTransaction() {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(AndroidAnalyticsTracker tracker) {
				tracker.trackExampleTransaction();
			}
		});
	}

	@Override
	public void trackExampleTiming() {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(AndroidAnalyticsTracker tracker) {
				tracker.trackExampleTiming();
			}
		});
	}
}
