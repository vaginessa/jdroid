package com.jdroid.android.google.gcm;

import android.content.SharedPreferences;

import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.sql.Timestamp;

/**
 * Utilities for device registration.
 * <p>
 * <strong>Note:</strong> this class uses a private {@link SharedPreferences} object to keep track of the registration
 * token.
 */
public final class GcmPreferences {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(GcmPreferences.class);
	
	// Default lifespan (7 days) of the {@link #isRegisteredOnServer()} flag until it is considered expired.
	private static final long DEFAULT_ON_SERVER_LIFESPAN_MS = 1000 * 3600 * 24 * 7;

	private static final String PREFERENCES = "gcm";
	private static final String PROPERTY_REGISTRATION_TOKEN = "registrationToken";
	private static final String PROPERTY_ON_SERVER = "onServer";
	private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTime";
	private static final String PROPERTY_ON_SERVER_LIFESPAN = "onServerLifeSpan";
	
	/**
	 * Gets the current registration id for application on GCM service.
	 * 
	 * @return registration id, or null if the registration is not complete.
	 */
	public static String getRegistrationToken() {
		return getSharedPreferencesHelper().loadPreference(PROPERTY_REGISTRATION_TOKEN);
	}
	
	/**
	 * Clears the registration id in the persistence store.
	 */
	public static void clearRegistrationToken() {
		setRegistrationToken(null);
	}
	
	/**
	 * Sets the registration id in the persistence store.
	 * 
	 * @param registrationToken The registrationToken
	 */
	public static void setRegistrationToken(String registrationToken) {
		String oldRegistrationToken = getRegistrationToken();
		if (oldRegistrationToken == null || (oldRegistrationToken != null && !oldRegistrationToken.equals(registrationToken))) {
			setRegisteredOnServer(false);
		}
		getSharedPreferencesHelper().savePreferenceAsync(PROPERTY_REGISTRATION_TOKEN, registrationToken);
	}
	
	/**
	 * Sets whether the device was successfully registered in the server side.
	 * 
	 * @param flag
	 */
	public static void setRegisteredOnServer(boolean flag) {
		getSharedPreferencesHelper().savePreferenceAsync(PROPERTY_ON_SERVER, flag);
		long registerOnServerLifespan = getSharedPreferencesHelper().loadPreferenceAsLong(PROPERTY_ON_SERVER_LIFESPAN, DEFAULT_ON_SERVER_LIFESPAN_MS);
		long expirationTime = System.currentTimeMillis() + registerOnServerLifespan;
		getSharedPreferencesHelper().savePreferenceAsync(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
		LOGGER.debug("Setted registeredOnServer status as " + flag + " until " + new Timestamp(expirationTime));
	}
	
	/**
	 * Checks whether the device was successfully registered in the server side, as set by
	 * {@link #setRegisteredOnServer(boolean)}.
	 * 
	 * <p>
	 * To avoid the scenario where the device sends the registration to the server but the server loses it, this flag
	 * has an expiration date, which is {@link #DEFAULT_ON_SERVER_LIFESPAN_MS} by default (but can be changed by
	 * {@link #setRegisterOnServerLifespan(long)}).
	 * 
	 * @return whether the device was successfully registered in the server side
	 */
	public static boolean isRegisteredOnServer() {
		boolean isRegistered = getSharedPreferencesHelper().loadPreferenceAsBoolean(PROPERTY_ON_SERVER, false);
		if (isRegistered) {
			// checks if the information is not stale
			long expirationTime = getSharedPreferencesHelper().loadPreferenceAsLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1L);
			if (System.currentTimeMillis() > expirationTime) {
				LOGGER.debug("registeredOnServer flag expired on: " + new Timestamp(expirationTime));
				return false;
			}
		}
		return isRegistered;
	}
	
	/**
	 * Sets how long (in milliseconds) the {@link #isRegisteredOnServer()} flag is valid.
	 * 
	 * @param lifespan
	 */
	public static void setRegisterOnServerLifespan(long lifespan) {
		getSharedPreferencesHelper().savePreferenceAsync(PROPERTY_ON_SERVER_LIFESPAN, lifespan);
	}

	private static SharedPreferencesHelper getSharedPreferencesHelper() {
		return SharedPreferencesHelper.get(PREFERENCES);
	}
}
