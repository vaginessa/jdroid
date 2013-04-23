package com.jdroid.android.analytics;

import android.app.Activity;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class DefaultAnalyticsTracker implements AnalyticsTracker {
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStart(android.app.Activity)
	 */
	@Override
	public void onActivityStart(Activity activity) {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStop(android.app.Activity)
	 */
	@Override
	public void onActivityStop(Activity activity) {
		// Do Nothing
	}
}