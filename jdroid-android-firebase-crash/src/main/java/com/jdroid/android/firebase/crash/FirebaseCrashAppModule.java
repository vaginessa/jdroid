package com.jdroid.android.firebase.crash;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class FirebaseCrashAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = FirebaseCrashAppModule.class.getName();

	public static FirebaseCrashAppModule get() {
		return (FirebaseCrashAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private FirebaseCrashAppContext firebaseCrashAppContext;

	public FirebaseCrashAppModule() {
		firebaseCrashAppContext = createFirebaseCrashAppContext();
	}

	protected FirebaseCrashAppContext createFirebaseCrashAppContext() {
		return new FirebaseCrashAppContext();
	}

	public FirebaseCrashAppContext getFirebaseCrashAppContext() {
		return firebaseCrashAppContext;
	}

	@Override
	public List<? extends AnalyticsTracker> getAnalyticsTrackers() {
		return Lists.newArrayList(firebaseCrashAppContext.getAnalyticsTracker());
	}
}
