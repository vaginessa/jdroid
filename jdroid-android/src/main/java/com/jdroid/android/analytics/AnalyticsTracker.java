package com.jdroid.android.analytics;

import android.app.Activity;
import com.jdroid.java.exception.ConnectionException;

/**
 * 
 * @author Maxi Rosson
 */
public interface AnalyticsTracker {
	
	public void init();
	
	public Boolean isEnabled();
	
	public void onActivityStart(Activity activity, Object data);
	
	public void onActivityStop(Activity activity);
	
	public void trackConnectionException(ConnectionException connectionException);
	
	public void trackUriHandled(Boolean handled, String validUri, String invalidUri);
	
}
