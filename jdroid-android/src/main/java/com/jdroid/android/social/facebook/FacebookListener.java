package com.jdroid.android.social.facebook;

public interface FacebookListener {
	
	public void onFacebookSignIn(FacebookUser facebookUser);
	
	public void onFacebookSignInFailed();
	
	public void onFacebookConnected(FacebookUser facebookUser);
	
	public void onFacebookDisconnected();
}
