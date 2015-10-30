package com.jdroid.android.google.gcm;

import android.os.Bundle;

public interface GcmMessageResolver {
	
	public GcmMessage resolve(String from, Bundle data);
	
}
