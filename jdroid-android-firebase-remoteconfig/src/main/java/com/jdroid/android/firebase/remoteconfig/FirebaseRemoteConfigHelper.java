package com.jdroid.android.firebase.remoteconfig;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.FirebaseAppModule;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.annotation.Internal;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

public class FirebaseRemoteConfigHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseRemoteConfigHelper.class);

	private static final String MOCKS_ENABLED = "firebase.remote.config.mocks.enabled";

	private static FirebaseRemoteConfig firebaseRemoteConfig;

	private static final long DEFAULT_FETCH_EXPIRATION = DateUtils.SECONDS_PER_HOUR * 12;

	private static int retryCount = 0;

	private static Boolean mocksEnabled = false;
	private static Map<String, String> mocks;
	private static SharedPreferencesHelper sharedPreferencesHelper;

	@WorkerThread
	@Internal
	static void init() {

		try {
			FirebaseApp.initializeApp(AbstractApplication.get());

			firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

			FirebaseRemoteConfigSettings.Builder configSettingsBuilder = new FirebaseRemoteConfigSettings.Builder();
			configSettingsBuilder.setDeveloperModeEnabled(!AbstractApplication.get().getAppContext().isProductionEnvironment());

			firebaseRemoteConfig.setConfigSettings(configSettingsBuilder.build());

			List<RemoteConfigParameter> remoteConfigParameters = AbstractApplication.get().getRemoteConfigParameters();
			if (remoteConfigParameters != null) {
				Map<String, Object> defaults = Maps.newHashMap();
				for (RemoteConfigParameter each : remoteConfigParameters) {
					Object defaultValue = each.getDefaultValue();
					if (defaultValue != null) {
						defaults.put(each.getKey(), defaultValue);
					}
				}
				firebaseRemoteConfig.setDefaults(defaults);
			}

			if (!AbstractApplication.get().getAppContext().isProductionEnvironment()) {
				sharedPreferencesHelper = SharedPreferencesHelper.get(FirebaseRemoteConfigHelper.class.getSimpleName());
				mocks  = (Map<String, String>)sharedPreferencesHelper.loadAllPreferences();
				mocksEnabled = sharedPreferencesHelper.loadPreferenceAsBoolean(MOCKS_ENABLED, false);
			}

			fetch(DEFAULT_FETCH_EXPIRATION, true);
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException("Error initializing Firebase Remote Config", e);
		}
	}

	@Internal
	public static void fetchNow() {
		fetchNow(null);
	}

	@Internal
	public static void fetchNow(OnSuccessListener<Void> onSuccessListener) {
		fetch(0, false, onSuccessListener);
	}

	@Internal
	public static void fetch(long cacheExpirationSeconds, Boolean setExperimentUserProperty) {
		fetch(cacheExpirationSeconds, setExperimentUserProperty, null);
	}

	@Internal
	public static void fetch(final long cacheExpirationSeconds, final Boolean setExperimentUserProperty, final OnSuccessListener<Void> onSuccessListener) {
		if (firebaseRemoteConfig != null) {
			Task<Void> task = firebaseRemoteConfig.fetch(cacheExpirationSeconds);
			task.addOnSuccessListener(new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void aVoid) {

					retryCount = 0;

					LOGGER.debug("Firebase Remote Config fetch succeeded");
					// Once the config is successfully fetched it must be activated before newly fetched values are returned.

					Boolean result = firebaseRemoteConfig.activateFetched();
					// true if there was a Fetched Config, and it was activated. false if no Fetched Config was found, or the Fetched Config was already activated.
					LOGGER.debug("Firebase Remote Config activate fetched result: " + result);

					if (setExperimentUserProperty) {
						final List<RemoteConfigParameter> remoteConfigParameters = AbstractApplication.get().getRemoteConfigParameters();
						if (remoteConfigParameters != null) {
							ExecutorUtils.execute(new Runnable() {
								@Override
								public void run() {
									for (RemoteConfigParameter each : remoteConfigParameters) {
										if (each.isUserProperty()) {
											String experimentVariant = FirebaseRemoteConfig.getInstance().getString(each.getKey());
											FirebaseAppModule.get().getFirebaseAnalyticsHelper().setUserProperty(each.getKey(), experimentVariant);
										}
									}
								}
							});
						}
					}

					if (onSuccessListener != null) {
						ExecutorUtils.execute(new Runnable() {
							@Override
							public void run() {
								onSuccessListener.onSuccess(null);
							}
						});
					}
				}
			});
			task.addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception exception) {
					LOGGER.error("Firebase Remote Config fetch failed", exception);
					retryCount++;

					if (retryCount <= 3) {
						Bundle bundle = new Bundle();
						bundle.putLong(FirebaseRemoteConfigFetchCommand.CACHE_EXPIRATION_SECONDS, cacheExpirationSeconds);
						bundle.putBoolean(FirebaseRemoteConfigFetchCommand.SET_EXPERIMENT_USER_PROPERTY, setExperimentUserProperty);
						new FirebaseRemoteConfigFetchCommand().start(bundle);
					}
				}
			});
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("Ignoring Firebase Remote Config fetch, because it wasn't initialized yet.");
		}
	}

	@Nullable
	public static FirebaseRemoteConfig getFirebaseRemoteConfig() {
		return firebaseRemoteConfig;
	}

	private static FirebaseRemoteConfigValue getFirebaseRemoteConfigValue(RemoteConfigParameter remoteConfigParameter) {
		if (mocksEnabled && !AbstractApplication.get().getAppContext().isProductionEnvironment()) {
			return new MockFirebaseRemoteConfigValue(remoteConfigParameter, mocks);
		} else if (firebaseRemoteConfig == null) {
			return new StaticFirebaseRemoteConfigValue(remoteConfigParameter);
		} else {
			return firebaseRemoteConfig.getValue(remoteConfigParameter.getKey());
		}
	}

	public static String getString(RemoteConfigParameter remoteConfigParameter) {
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = getFirebaseRemoteConfigValue(remoteConfigParameter);
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
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = getFirebaseRemoteConfigValue(remoteConfigParameter);
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
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = getFirebaseRemoteConfigValue(remoteConfigParameter);
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
		FirebaseRemoteConfigValue firebaseRemoteConfigValue = getFirebaseRemoteConfigValue(remoteConfigParameter);
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
			} else if (firebaseRemoteConfigValue.getSource() == -1) {
				source = "Mock";
			}
			LOGGER.info("Loaded Firebase Remote Config. Source [" + source + "] Key [" + remoteConfigParameter.getKey() + "] Value [" + value + "]");
		}
	}

	@Internal
	public static Boolean isMocksEnabled() {
		return mocksEnabled;
	}

	@Internal
	public static void setMocksEnabled(Boolean mocksEnabled) {
		if (mocksEnabled) {
			for (RemoteConfigParameter each : AbstractApplication.get().getRemoteConfigParameters()) {
				String value = getString(each);
				sharedPreferencesHelper.savePreference(each.getKey(), value);
				mocks.put(each.getKey(), value);
			}
		}
		FirebaseRemoteConfigHelper.mocksEnabled = mocksEnabled;
		sharedPreferencesHelper.savePreference(MOCKS_ENABLED, mocksEnabled);
	}

	@Internal
	public static void setParameterMock(RemoteConfigParameter remoteConfigParameter, String value) {
		sharedPreferencesHelper.savePreferenceAsync(remoteConfigParameter.getKey(), value);
		mocks.put(remoteConfigParameter.getKey(), value);
	}
}
