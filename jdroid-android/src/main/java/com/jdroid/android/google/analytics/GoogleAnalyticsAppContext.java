package com.jdroid.android.google.analytics;

import com.jdroid.android.context.AbstractAppContext;

public class GoogleAnalyticsAppContext extends AbstractAppContext {

	/**
	 * @return Whether the application has Google Analytics enabled or not
	 */
	public Boolean isGoogleAnalyticsEnabled() {
		return getBuildConfigValue("GOOGLE_ANALYTICS_ENABLED", false);
	}

	/**
	 * @return The Google Analytics Tracking ID
	 */
	public String getGoogleAnalyticsTrackingId() {
		return getBuildConfigValue("GOOGLE_ANALYTICS_TRACKING_ID", null);
	}

}
