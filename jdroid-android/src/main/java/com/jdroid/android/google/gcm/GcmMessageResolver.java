package com.jdroid.android.google.gcm;

import android.content.Intent;

public interface GcmMessageResolver {
	
	public GcmMessage resolve(Intent intent);
	
}
