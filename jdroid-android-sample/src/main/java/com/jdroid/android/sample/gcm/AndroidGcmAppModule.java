package com.jdroid.android.sample.gcm;

import com.jdroid.android.google.gcm.AbstractGcmAppModule;
import com.jdroid.android.google.gcm.GcmDebugContext;
import com.jdroid.android.google.gcm.GcmMessageResolver;
import com.jdroid.android.sample.debug.AndroidGcmDebugContext;

public class AndroidGcmAppModule extends AbstractGcmAppModule {

	public static AndroidGcmAppModule get() {
		synchronized (AndroidGcmAppModule.class) {
			if (AbstractGcmAppModule.INSTANCE == null) {
				AbstractGcmAppModule.INSTANCE = new AndroidGcmAppModule();
			}
		}
		return (AndroidGcmAppModule)AbstractGcmAppModule.INSTANCE;
	}

	@Override
	protected GcmMessageResolver createGcmMessageResolver() {
		return new AndroidGcmResolver();
	}

	@Override
	protected GcmDebugContext createGcmDebugContext() {
		return new AndroidGcmDebugContext();
	}
}
