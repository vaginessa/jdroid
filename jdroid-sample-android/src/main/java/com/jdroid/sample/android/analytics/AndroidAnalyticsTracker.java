package com.jdroid.sample.android.analytics;

import com.jdroid.android.analytics.AnalyticsTracker;

public interface AndroidAnalyticsTracker extends AnalyticsTracker {
	
	public void trackExampleEvent();

	public void trackExampleTransaction();

	public void trackExampleTiming();

}
