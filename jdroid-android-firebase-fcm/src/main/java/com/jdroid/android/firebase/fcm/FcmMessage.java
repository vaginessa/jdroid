package com.jdroid.android.firebase.fcm;

import com.google.firebase.messaging.RemoteMessage;

public interface FcmMessage {
	
	public String getMessageKey();
	
	public void handle(RemoteMessage remoteMessage);
	
}
