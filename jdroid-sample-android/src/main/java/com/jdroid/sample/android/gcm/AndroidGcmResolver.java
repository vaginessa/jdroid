package com.jdroid.sample.android.gcm;

import com.jdroid.android.gcm.AbstractGcmMessageResolver;
import com.jdroid.android.gcm.GcmMessageResolver;

/**
 * 
 * @author Maxi Rosson
 */
public class AndroidGcmResolver extends AbstractGcmMessageResolver {
	
	private static final GcmMessageResolver INSTANCE = new AndroidGcmResolver();
	
	public static GcmMessageResolver get() {
		return INSTANCE;
	}
	
	public AndroidGcmResolver() {
		super(AndroidGcmMessage.values());
	}
	
	/**
	 * @see com.jdroid.android.gcm.AbstractGcmMessageResolver#onNotAuthenticatedUser(java.lang.Long)
	 */
	@Override
	protected void onNotAuthenticatedUser(Long userId) {
	}
	
}
