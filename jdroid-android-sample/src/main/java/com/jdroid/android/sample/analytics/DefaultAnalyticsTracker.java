package com.jdroid.android.sample.analytics;

import com.jdroid.android.analytics.AbstractAnalyticsTracker;

public abstract class DefaultAnalyticsTracker extends AbstractAnalyticsTracker implements AndroidAnalyticsTracker {
	
	/**
	 * @see com.jdroid.android.sample.analytics.AndroidAnalyticsTracker#trackExampleEvent()
	 */
	@Override
	public void trackExampleEvent() {
		// Do nothing
	}

	@Override
	public void trackExampleTransaction() {
		// Do nothing
	}

	@Override
	public void trackExampleTiming() {
		// Do nothing
	}
}
