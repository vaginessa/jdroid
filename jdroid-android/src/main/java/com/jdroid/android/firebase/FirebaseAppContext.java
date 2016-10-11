package com.jdroid.android.firebase;

import com.jdroid.android.context.AbstractAppContext;

public class FirebaseAppContext extends AbstractAppContext {

	/**
	 * @return Whether the application has Firebase Analytics enabled or not
	 */
	public Boolean isFirebaseAnalyticsEnabled() {
		return getBuildConfigValue("FIREBASE_ANALYTICS_ENABLED", false);
	}
}
