package com.jdroid.android.google.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.jdroid.android.application.AbstractApplication;
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

	private static final String PREFERENCES = "com.google.android.gcm";
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
		return getGcmPreferences().getString(PROPERTY_REGISTRATION_TOKEN, null);
	}
	
	/**
	 * Checks whether the application was successfully registered on GCM service.
	 * 
	 * @return Whether the application was successfully registered on GCM service.
	 */
	public static boolean isRegistered() {
		return getRegistrationToken() != null;
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
		SharedPreferences prefs = getGcmPreferences();
		Editor editor = prefs.edit();
		editor.putString(PROPERTY_REGISTRATION_TOKEN, registrationToken);
		editor.apply();
		LOGGER.debug("Saved the registrationToken [" + registrationToken + "]");
	}
	
	/**
	 * Sets whether the device was successfully registered in the server side.
	 * 
	 * @param flag
	 */
	public static void setRegisteredOnServer(boolean flag) {
		SharedPreferences prefs = getGcmPreferences();
		Editor editor = prefs.edit();
		editor.putBoolean(PROPERTY_ON_SERVER, flag);
		long expirationTime = System.currentTimeMillis() + getRegisterOnServerLifespan();
		editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, System.currentTimeMillis()
				+ getRegisterOnServerLifespan());
		editor.apply();
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
		SharedPreferences prefs = getGcmPreferences();
		boolean isRegistered = prefs.getBoolean(PROPERTY_ON_SERVER, false);
		LOGGER.debug("Is registered on server: " + isRegistered);
		if (isRegistered) {
			// checks if the information is not stale
			long expirationTime = prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
			if (System.currentTimeMillis() > expirationTime) {
				LOGGER.debug("registeredOnServer flag expired on: " + new Timestamp(expirationTime));
				return false;
			}
		}
		return isRegistered;
	}
	
	/**
	 * Gets how long (in milliseconds) the {@link #isRegistered()} property is valid.
	 * 
	 * @return value set by {@link #setRegisteredOnServer(boolean)} or {@link #DEFAULT_ON_SERVER_LIFESPAN_MS}
	 *         if not set.
	 */
	public static long getRegisterOnServerLifespan() {
		return getGcmPreferences().getLong(PROPERTY_ON_SERVER_LIFESPAN, DEFAULT_ON_SERVER_LIFESPAN_MS);
	}
	
	/**
	 * Sets how long (in milliseconds) the {@link #isRegistered()} flag is valid.
	 * 
	 * @param lifespan
	 */
	public static void setRegisterOnServerLifespan(long lifespan) {
		SharedPreferences prefs = getGcmPreferences();
		Editor editor = prefs.edit();
		editor.putLong(PROPERTY_ON_SERVER_LIFESPAN, lifespan);
		editor.apply();
	}
	
	private static SharedPreferences getGcmPreferences() {
		return AbstractApplication.get().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
	}
}
