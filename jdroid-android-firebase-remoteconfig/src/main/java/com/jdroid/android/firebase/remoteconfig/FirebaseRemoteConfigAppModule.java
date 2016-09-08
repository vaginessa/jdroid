package com.jdroid.android.firebase.remoteconfig;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.Map;

public class FirebaseRemoteConfigAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = FirebaseRemoteConfigAppModule.class.getName();

	public static FirebaseRemoteConfigAppModule get() {
		return (FirebaseRemoteConfigAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private FirebaseRemoteConfigAppContext firebaseRemoteConfigAppContext;

	public FirebaseRemoteConfigAppModule() {
		firebaseRemoteConfigAppContext = createFirebaseRemoteConfigAppContext();
	}

	protected FirebaseRemoteConfigAppContext createFirebaseRemoteConfigAppContext() {
		return new FirebaseRemoteConfigAppContext();
	}

	public FirebaseRemoteConfigAppContext getFirebaseRemoteConfigAppContext() {
		return firebaseRemoteConfigAppContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		final Map<String, Object> defaults = firebaseRemoteConfigAppContext.getRemoteConfigDefaults();
		if (defaults != null) {
			ExecutorUtils.execute(new Runnable() {
				@Override
				public void run() {
					FirebaseRemoteConfigHelper.init(defaults);
				}
			});
		}
	}
}
