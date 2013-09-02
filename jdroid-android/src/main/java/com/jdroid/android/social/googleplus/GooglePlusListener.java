package com.jdroid.android.social.googleplus;

import java.util.List;

/**
 * 
 * @author Maxi Rosson
 */
public interface GooglePlusListener {
	
	public void onAccessRevoked();
	
	public void onConnectionFailed();
	
	public void onConnected();
	
	public void onDisconnected();
	
	public void onPeopleLoaded(List<SocialUser> friends);
}
