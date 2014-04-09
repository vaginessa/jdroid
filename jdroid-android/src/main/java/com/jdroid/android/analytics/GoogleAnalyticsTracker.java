package com.jdroid.android.analytics;

import android.app.Activity;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger.LogLevel;
import com.jdroid.android.AbstractApplication;

/**
 * 
 * @author Maxi Rosson
 */
public class GoogleAnalyticsTracker extends AbstractAnalyticsTracker {
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().getAppContext().isGoogleAnalyticsEnabled();
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#init()
	 */
	@Override
	public void init() {
		GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(AbstractApplication.get());
		
		if (AbstractApplication.get().getAppContext().isGoogleAnalyticsDebugEnabled()) {
			googleAnalytics.getLogger().setLogLevel(LogLevel.VERBOSE);
		}
		googleAnalytics.getTracker(AbstractApplication.get().getAppContext().getGoogleAnalyticsTrackingId());
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#onActivityStart(android.app.Activity, java.lang.Object)
	 */
	@Override
	public void onActivityStart(Activity activity, Object data) {
		EasyTracker.getInstance(activity).activityStart(activity);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#onActivityStop(android.app.Activity)
	 */
	@Override
	public void onActivityStop(Activity activity) {
		EasyTracker.getInstance(activity).activityStop(activity);
	}
}
