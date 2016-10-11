package com.jdroid.android.firebase.remoteconfig;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.FirebaseAppModule;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

public class FirebaseRemoteConfigHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseRemoteConfigHelper.class);

	private static FirebaseRemoteConfig firebaseRemoteConfig;

	static void init() {
		firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

		FirebaseRemoteConfigSettings.Builder configSettingsBuilder = new FirebaseRemoteConfigSettings.Builder();
		configSettingsBuilder.setDeveloperModeEnabled(!AbstractApplication.get().getAppContext().isProductionEnvironment());

		firebaseRemoteConfig.setConfigSettings(configSettingsBuilder.build());

		List<RemoteConfigParameter> remoteConfigParameters = FirebaseRemoteConfigAppModule.get().getFirebaseRemoteConfigAppContext().getRemoteConfigParameters();
		if (remoteConfigParameters != null) {
			Map<String, Object> defaults = Maps.newHashMap();
			for (RemoteConfigParameter each : remoteConfigParameters) {
				defaults.put(each.getKey(), each.getDefaultValue());
			}
			firebaseRemoteConfig.setDefaults(defaults);
		}

		fetch(0, true);
	}

	public static void fetchNow() {
		fetch(0, false);
	}

	public static void fetch(long cacheExpirationSeconds, final Boolean setExperimentUserProperty) {
		Task<Void> task = firebaseRemoteConfig.fetch(cacheExpirationSeconds);
		task.addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				LOGGER.debug("Firebase Remote Config fetch succeeded");
				// Once the config is successfully fetched it must be activated before newly fetched values are returned.

				Boolean result = firebaseRemoteConfig.activateFetched();
				// true if there was a Fetched Config, and it was activated. false if no Fetched Config was found, or the Fetched Config was already activated.
				LOGGER.debug("Firebase Remote Config activate fetched result: " + result);

				if (setExperimentUserProperty) {
					final List<RemoteConfigParameter> remoteConfigParameters = FirebaseRemoteConfigAppModule.get().getFirebaseRemoteConfigAppContext().getRemoteConfigParameters();
					if (remoteConfigParameters != null) {
						ExecutorUtils.execute(new Runnable() {
							@Override
							public void run() {
								for (RemoteConfigParameter each : remoteConfigParameters) {
									if (each.isABTestingExperiment()) {
										String experimentVariant = FirebaseRemoteConfig.getInstance().getString(each.getKey());
										FirebaseAppModule.get().getFirebaseAnalyticsHelper().setUserProperty(each.getKey(), experimentVariant);
									}
								}
							}
						});
					}
				}
			}
		});
		task.addOnFailureListener(new OnFailureListener() {
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

	public static String getString(RemoteConfigParameter remoteConfigParameter) {
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = firebaseRemoteConfig.getValue(remoteConfigParameter.getKey());
		Object value;
		if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
			value = remoteConfigParameter.getDefaultValue();
		} else {
			value = firebaseRemoteConfigValue.asString();
		}
		log(remoteConfigParameter, firebaseRemoteConfigValue, value);
		return (String)value;
	}

	public static Boolean getBoolean(RemoteConfigParameter remoteConfigParameter) {
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = firebaseRemoteConfig.getValue(remoteConfigParameter.getKey());
		Object value;
		if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
			value = remoteConfigParameter.getDefaultValue();
		} else {
			value = firebaseRemoteConfigValue.asBoolean();
		}
		log(remoteConfigParameter, firebaseRemoteConfigValue, value);
		return (Boolean)value;
	}

	public static Double getDouble(RemoteConfigParameter remoteConfigParameter) {
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = firebaseRemoteConfig.getValue(remoteConfigParameter.getKey());
		Object value;
		if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
			value = remoteConfigParameter.getDefaultValue();
		} else {
			value = firebaseRemoteConfigValue.asDouble();
		}
		log(remoteConfigParameter, firebaseRemoteConfigValue, value);
		return (Double)value;
	}

	public static Long getLong(RemoteConfigParameter remoteConfigParameter) {
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = firebaseRemoteConfig.getValue(remoteConfigParameter.getKey());
		Object value;
		if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
			value = remoteConfigParameter.getDefaultValue();
		} else {
			value = firebaseRemoteConfigValue.asLong();
		}
		log(remoteConfigParameter, firebaseRemoteConfigValue, value);
		return (Long)value;
	}

	private static void log(RemoteConfigParameter remoteConfigParameter, FirebaseRemoteConfigValue firebaseRemoteConfigValue, Object value) {
		if (LoggerUtils.isEnabled()) {
			String source = null;
			if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
				source = "Static";
			} else if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_REMOTE) {
				source = "Remote";
			} else if (firebaseRemoteConfigValue.getSource() == FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT) {
				source = "Default";
			}
			LOGGER.info("Loaded Firebase Remote Config. Source [" + source + "] Key [" + remoteConfigParameter.getKey() + "] Value [" + value + "]");
		}
	}

}
