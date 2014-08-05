package com.jdroid.android.gcm;

import android.content.Intent;

public interface GcmMessageResolver {
	
	public GcmMessage resolve(Intent intent);
	
}
