package com.jdroid.android.social.googleplus;

import java.util.List;
import com.google.android.gms.plus.model.people.Person;
import com.jdroid.android.social.SocialUser;

/**
 * Google+ Listener. All the events are invoked on the UI thread
 * 
 * @author Maxi Rosson
 */
public interface GooglePlusListener {
	
	public void onGooglePlusSignInCanceled();
	
	public void onGooglePlusAccessRevoked();
	
	public void onGooglePlusSignOut();
	
	public void onGooglePlusConnectionFailed();
	
	public void onGooglePlusSignIn(Person me, String accountName);
	
	public void onGooglePlusConnected(Person me, String accountName);
	
	public void onGooglePlusDisconnected();
	
	public void onGooglePlusFriendsLoaded(List<SocialUser> friends);
}
