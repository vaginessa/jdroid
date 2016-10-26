package com.jdroid.android.firebase.remoteconfig;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.List;

public class FirebaseRemoteConfigAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = FirebaseRemoteConfigAppModule.class.getName();

	public static FirebaseRemoteConfigAppModule get() {
		return (FirebaseRemoteConfigAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private FirebaseRemoteConfigDebugContext firebaseRemoteConfigDebugContext;

	@Override
	public void onCreate() {
		super.onCreate();

		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				FirebaseRemoteConfigHelper.init();
			}
		});
	}

	public FirebaseRemoteConfigDebugContext getFirebaseRemoteConfigDebugContext() {
		synchronized (AbstractApplication.class) {
			if (firebaseRemoteConfigDebugContext == null) {
				firebaseRemoteConfigDebugContext = createFirebaseRemoteConfigDebugContext();
			}
		}
		return firebaseRemoteConfigDebugContext;
	}

	protected FirebaseRemoteConfigDebugContext createFirebaseRemoteConfigDebugContext() {
		return new FirebaseRemoteConfigDebugContext();
	}

	@Override
	public List<PreferencesAppender> getPreferencesAppenders() {
		return getFirebaseRemoteConfigDebugContext().getPreferencesAppenders();
	}
}
