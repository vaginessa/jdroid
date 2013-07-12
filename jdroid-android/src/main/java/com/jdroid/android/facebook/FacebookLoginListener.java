package com.jdroid.android.facebook;

public interface FacebookLoginListener {
	
	/**
	 * Called when the login with facebook completes.
	 * 
	 * @param facebookConnector the {@link FacebookConnector} containing with the active session.
	 */
	public void onFacebookLoginCompleted(FacebookConnector facebookConnector);
	
}