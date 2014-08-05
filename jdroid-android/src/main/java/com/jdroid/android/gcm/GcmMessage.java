package com.jdroid.android.gcm;

import android.content.Intent;

public interface GcmMessage {
	
	public String getMessageKey();
	
	public void handle(Intent intent);
	
}
