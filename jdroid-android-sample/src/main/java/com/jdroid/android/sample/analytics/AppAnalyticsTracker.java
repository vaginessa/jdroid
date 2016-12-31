package com.jdroid.android.sample.analytics;

import com.jdroid.java.analytics.BaseAnalyticsTracker;

public interface AppAnalyticsTracker extends BaseAnalyticsTracker {
	
	public void trackExampleEvent();

	public void trackExampleTransaction();

	public void trackExampleTiming();

}
