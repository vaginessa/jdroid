package com.jdroid.sample.android.analytics;

import java.util.Map;
import com.jdroid.android.analytics.GoogleAnalyticsTracker;

public class AndroidGoogleAnalyticsTracker extends GoogleAnalyticsTracker implements AndroidAnalyticsTracker {
	
	private static final AndroidGoogleAnalyticsTracker INSTANCE = new AndroidGoogleAnalyticsTracker();
	
	public static AndroidGoogleAnalyticsTracker get() {
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.android.analytics.GoogleAnalyticsTracker#init(java.util.Map, java.util.Map)
	 */
	@Override
	protected void init(Map<String, Integer> customDimensionsMap, Map<String, Integer> customMetricsMap) {
		customDimensionsMap.put(CustomDimension.INSTALLATION_SOURCE.name(), 1);
		customDimensionsMap.put(CustomDimension.DEVICE_TYPE.name(), 2);
		customDimensionsMap.put(CustomDimension.APP_LOADING_SOURCE.name(), 3);
	}
	
	/**
	 * @see com.jdroid.sample.android.analytics.AndroidAnalyticsTracker#trackExampleEvent()
	 */
	@Override
	public void trackExampleEvent() {
		sendEvent("exampleCategory", "exampleAction", "exampleLabel");
	}
	
}
