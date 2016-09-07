package com.jdroid.android.firebase.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class FirebaseAnalyticsHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseAnalyticsHelper.class);

	private FirebaseAnalytics firebaseAnalytics;

	public FirebaseAnalyticsHelper() {
		firebaseAnalytics = FirebaseAnalytics.getInstance(AbstractApplication.get());
	}

	public void sendEvent(String eventName, Bundle params) {
		firebaseAnalytics.logEvent(eventName, params);
		LOGGER.debug("Event [" + eventName + "] sent. " + params);
	}

	public void sendEvent(String eventName) {
		firebaseAnalytics.logEvent(eventName, null);
		LOGGER.debug("Event [" + eventName + "] sent. ");
	}

	public void setUserProperty(String name, String value) {
		firebaseAnalytics.setUserProperty(name, value);
		LOGGER.debug("User Property [" + name + "] added. Value [" + value + "]");
	}

	public void removeUserProperty(String name) {
		firebaseAnalytics.setUserProperty(name, null);
		LOGGER.debug("User Property [" + name + "] removed.");
	}

	public void setUserId(String id) {
		firebaseAnalytics.setUserId(id);
		LOGGER.debug("User Id [" + id + "] added.");
	}

	public void removeUserId() {
		firebaseAnalytics.setUserId(null);
		LOGGER.debug("User Id removed.");
	}

}
