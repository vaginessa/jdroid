package com.jdroid.android.analytics;

import android.app.Activity;

/**
 * 
 * @author Maxi Rosson
 */
public interface AnalyticsTracker {
	
	public Boolean isEnabled();
	
	public void onActivityStart(Activity activity);
	
	public void onActivityStop(Activity activity);
}
