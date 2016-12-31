package com.jdroid.android.firebase;

import android.support.annotation.WorkerThread;

import com.jdroid.android.analytics.CoreAnalyticsTracker;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsHelper;
import com.jdroid.android.firebase.analytics.FirebaseCoreAnalyticsTracker;
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

	@WorkerThread
	public synchronized FirebaseAnalyticsHelper getFirebaseAnalyticsHelper() {
		if (firebaseAnalyticsHelper == null) {
			firebaseAnalyticsHelper = createFirebaseAnalyticsHelper();
		}
		return firebaseAnalyticsHelper;
	}

	protected FirebaseAnalyticsHelper createFirebaseAnalyticsHelper() {
		return new FirebaseAnalyticsHelper();
	}

	@Override
	public List<? extends CoreAnalyticsTracker> createCoreAnalyticsTrackers() {
		return firebaseAppContext.isFirebaseAnalyticsEnabled() ? Lists.newArrayList(createFirebaseCoreAnalyticsTracker()) : Lists.<CoreAnalyticsTracker>newArrayList();
	}

	protected CoreAnalyticsTracker createFirebaseCoreAnalyticsTracker() {
		return new FirebaseCoreAnalyticsTracker();
	}
}
