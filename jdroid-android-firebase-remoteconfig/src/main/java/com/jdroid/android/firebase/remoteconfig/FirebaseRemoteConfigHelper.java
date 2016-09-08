package com.jdroid.android.firebase.remoteconfig;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Map;

public class FirebaseRemoteConfigHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseRemoteConfigHelper.class);

	private static FirebaseRemoteConfig firebaseRemoteConfig;

	public static void init(Map<String, Object> defaults) {
		firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

		FirebaseRemoteConfigSettings.Builder configSettingsBuilder =new FirebaseRemoteConfigSettings.Builder();
		configSettingsBuilder.setDeveloperModeEnabled(!AbstractApplication.get().getAppContext().isProductionEnvironment());

		firebaseRemoteConfig.setConfigSettings(configSettingsBuilder.build());
		firebaseRemoteConfig.setDefaults(defaults);

		firebaseRemoteConfig.fetch(0).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				LOGGER.debug("Firebase Remote Config fetch succeeded");
				// Once the config is successfully fetched it must be activated before newly fetched values are returned.

				Boolean result = firebaseRemoteConfig.activateFetched();
				// true if there was a Fetched Config, and it was activated. false if no Fetched Config was found, or the Fetched Config was already activated.
				LOGGER.debug("Firebase Remote Config activate fetched result: " + result);
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception exception) {
				LOGGER.debug("Firebase Remote Config fetch failed");
				AbstractApplication.get().getExceptionHandler().logHandledException(exception);
			}
		});
	}

	public static FirebaseRemoteConfig getFirebaseRemoteConfig() {
		return firebaseRemoteConfig;
	}

}
