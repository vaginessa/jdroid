package com.jdroid.android.google.analytics;

import com.jdroid.android.context.AbstractAppContext;
import com.jdroid.android.firebase.testlab.FirebaseTestLab;

public class GoogleAnalyticsAppContext extends AbstractAppContext {

	/**
	 * @return Whether the application has Google Analytics enabled or not
	 */
	public Boolean isGoogleAnalyticsEnabled() {
		return getBuildConfigBoolean("GOOGLE_ANALYTICS_ENABLED", false) && !FirebaseTestLab.isRunningInstrumentedTests();
	}

	/**
	 * @return The Google Analytics Tracking ID
	 */
	public String getGoogleAnalyticsTrackingId() {
		return getBuildConfigValue("GOOGLE_ANALYTICS_TRACKING_ID", null);
	}

}
