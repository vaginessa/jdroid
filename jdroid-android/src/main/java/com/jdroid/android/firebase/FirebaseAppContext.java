package com.jdroid.android.firebase;

import com.jdroid.android.application.AbstractApplication;

public class FirebaseAppContext {

	/**
	 * @return Whether the application has Firebase Analytics enabled or not
	 */
	public Boolean isFirebaseAnalyticsEnabled() {
		return AbstractApplication.get().getAppContext().getBuildConfigValue("FIREBASE_ANALYTICS_ENABLED", false);
	}
}
