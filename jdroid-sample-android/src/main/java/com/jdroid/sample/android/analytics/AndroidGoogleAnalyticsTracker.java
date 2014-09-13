package com.jdroid.sample.android.analytics;

import com.jdroid.android.analytics.GoogleAnalyticsTracker;

public class AndroidGoogleAnalyticsTracker extends GoogleAnalyticsTracker implements AndroidAnalyticsTracker {
	
	private static final AndroidGoogleAnalyticsTracker INSTANCE = new AndroidGoogleAnalyticsTracker();
	
	public static AndroidGoogleAnalyticsTracker get() {
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.sample.android.analytics.AndroidAnalyticsTracker#trackExampleEvent()
	 */
	@Override
	public void trackExampleEvent() {
		sendEvent("exampleCategory", "exampleAction", "exampleLabel");
	}
	
}
