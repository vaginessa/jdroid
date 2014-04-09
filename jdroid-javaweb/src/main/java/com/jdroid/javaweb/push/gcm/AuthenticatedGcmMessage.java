package com.jdroid.javaweb.push.gcm;


/**
 * 
 * @author Maxi Rosson
 */
public abstract class AuthenticatedGcmMessage extends AbstractGcmMessage {
	
	private static final String USER_ID_KEY = "userIdKey";
	
	public AuthenticatedGcmMessage(Long userId) {
		addParameter(USER_ID_KEY, userId);
	}
}
