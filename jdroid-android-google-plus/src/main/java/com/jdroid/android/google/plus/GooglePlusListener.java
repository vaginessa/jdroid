package com.jdroid.android.google.plus;

import com.google.android.gms.plus.model.people.Person;
import com.jdroid.android.social.SocialUser;

import java.util.List;

/**
 * Google+ Listener. All the events are invoked on the UI thread
 */
public interface GooglePlusListener {
	
	public void onGooglePlusSignInFailed();
	
	public void onGooglePlusAccessRevoked();
	
	public void onGooglePlusSignOut();
	
	public void onGooglePlusConnectionFailed();
	
	public void onGooglePlusSignIn(Person me, String accountName);
	
	public void onGooglePlusConnected(Person me, String accountName);
	
	public void onGooglePlusDisconnected();
	
	public void onGooglePlusFriendsLoaded(List<SocialUser> friends);
}
