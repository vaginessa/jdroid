package com.jdroid.android.sample.gcm;

import com.jdroid.android.google.gcm.GcmContext;
import com.jdroid.android.google.gcm.GcmMessageResolver;

public class AndroidGcmContext extends GcmContext {

	@Override
	public GcmMessageResolver getGcmResolver() {
		return AndroidGcmResolver.get();
	}
}
