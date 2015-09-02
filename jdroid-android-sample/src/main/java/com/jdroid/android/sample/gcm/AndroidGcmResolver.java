package com.jdroid.android.sample.gcm;


import com.jdroid.android.google.gcm.AbstractGcmMessageResolver;

public class AndroidGcmResolver extends AbstractGcmMessageResolver {
	
	public AndroidGcmResolver() {
		super(AndroidGcmMessage.values());
	}
	
	@Override
	protected void onNotAuthenticatedUser(Long userId) {
	}
	
}
