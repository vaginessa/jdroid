package com.jdroid.android.google.gcm;

import com.google.firebase.messaging.RemoteMessage;

public interface GcmMessage {
	
	public String getMessageKey();
	
	public void handle(RemoteMessage remoteMessage);
	
}
