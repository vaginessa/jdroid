package com.jdroid.javaweb.google.gcm;

public abstract class AuthenticatedGcmMessage extends GcmMessage {
	
	private static final String USER_ID_KEY = "userIdKey";
	
	public AuthenticatedGcmMessage(Long userId) {
		addParameter(USER_ID_KEY, userId);
	}
}
