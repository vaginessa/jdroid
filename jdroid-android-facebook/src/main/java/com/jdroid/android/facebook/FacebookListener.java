package com.jdroid.android.facebook;

public interface FacebookListener {
	
	public void onFacebookSignIn(FacebookUser facebookUser);
	
	public void onFacebookSignInFailed();
	
	public void onFacebookConnected(FacebookUser facebookUser);
	
	public void onFacebookDisconnected();
}
