package com.jdroid.android.sample.gcm;


import com.jdroid.android.google.gcm.AbstractGcmMessageResolver;
import com.jdroid.android.google.gcm.GcmMessageResolver;

public class AndroidGcmResolver extends AbstractGcmMessageResolver {
	
	private static final GcmMessageResolver INSTANCE = new AndroidGcmResolver();
	
	public static GcmMessageResolver get() {
		return INSTANCE;
	}
	
	public AndroidGcmResolver() {
		super(AndroidGcmMessage.values());
	}
	
	@Override
	protected void onNotAuthenticatedUser(Long userId) {
	}
	
}
