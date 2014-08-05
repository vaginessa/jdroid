package com.jdroid.javaweb.push.gcm;

public abstract class AuthenticatedGcmMessage extends AbstractGcmMessage {
	
	private static final String USER_ID_KEY = "userIdKey";
	
	public AuthenticatedGcmMessage(Long userId) {
		addParameter(USER_ID_KEY, userId);
	}
}
