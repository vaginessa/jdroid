package com.jdroid.android.google.gcm;

import com.jdroid.android.AbstractApplication;

public class GcmContext {

	/**
	 * @return The Google project ID acquired from the API console
	 */
	public String getGoogleProjectId() {
		return AbstractApplication.get().getBuildConfigValue("GOOGLE_PROJECT_ID");
	}

	public GcmMessageResolver getGcmResolver() {
		return null;
	}
}
