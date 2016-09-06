package com.jdroid.android.google.gcm;

import com.google.firebase.messaging.RemoteMessage;

public interface GcmMessageResolver {
	
	public GcmMessage resolve(RemoteMessage remoteMessage);
	
}
