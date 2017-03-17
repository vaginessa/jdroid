package com.jdroid.android.firebase.crash;

import com.jdroid.android.analytics.CoreAnalyticsTracker;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class FirebaseCrashAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = FirebaseCrashAppModule.class.getName();

	public static FirebaseCrashAppModule get() {
		return (FirebaseCrashAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	@Override
	public List<? extends CoreAnalyticsTracker> createCoreAnalyticsTrackers() {
		return Lists.newArrayList(createFirebaseCrashTracker());
	}

	protected CoreAnalyticsTracker createFirebaseCrashTracker() {
		return new FirebaseCrashTracker();
	}
}
