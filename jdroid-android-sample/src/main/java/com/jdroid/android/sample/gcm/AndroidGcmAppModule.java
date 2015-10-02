package com.jdroid.android.sample.gcm;

import com.jdroid.android.google.gcm.AbstractGcmAppModule;
import com.jdroid.android.google.gcm.GcmDebugContext;
import com.jdroid.android.google.gcm.GcmMessageResolver;
import com.jdroid.android.sample.debug.AndroidGcmDebugContext;

public class AndroidGcmAppModule extends AbstractGcmAppModule {

	@Override
	protected GcmMessageResolver createGcmMessageResolver() {
		return new AndroidGcmResolver();
	}

	@Override
	protected GcmDebugContext createGcmDebugContext() {
		return new AndroidGcmDebugContext();
	}
}
