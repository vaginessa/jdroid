package com.jdroid.android.firebase;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsHelper;
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsTracker;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class FirebaseAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = FirebaseAppModule.class.getName();

	public static FirebaseAppModule get() {
		return (FirebaseAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private FirebaseAnalyticsHelper firebaseAnalyticsHelper;

	private FirebaseAppContext firebaseAppContext;

	public FirebaseAppModule() {
		firebaseAppContext = createFirebaseAppContext();
	}

	protected FirebaseAppContext createFirebaseAppContext() {
		return new FirebaseAppContext();
	}

	public FirebaseAppContext getFirebaseAppContext() {
		return firebaseAppContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (firebaseAppContext.isFirebaseAnalyticsEnabled()) {
			firebaseAnalyticsHelper = createFirebaseAnalyticsHelper();
		}
	}

	protected FirebaseAnalyticsHelper createFirebaseAnalyticsHelper() {
		return new FirebaseAnalyticsHelper();
	}

	public FirebaseAnalyticsHelper getFirebaseAnalyticsHelper() {
		return firebaseAnalyticsHelper;
	}

	@Override
	public List<? extends AnalyticsTracker> getAnalyticsTrackers() {
		return firebaseAppContext.isFirebaseAnalyticsEnabled() ? Lists.newArrayList(createFirebaseAnalyticsTracker()) : Lists.<AnalyticsTracker>newArrayList();
	}

	protected AnalyticsTracker createFirebaseAnalyticsTracker() {
		return new FirebaseAnalyticsTracker();
	}
}
