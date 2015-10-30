package com.jdroid.android.google.gcm;

import android.os.Bundle;

public interface GcmMessage {
	
	public String getMessageKey();
	
	public void handle(String from, Bundle data);
	
}
