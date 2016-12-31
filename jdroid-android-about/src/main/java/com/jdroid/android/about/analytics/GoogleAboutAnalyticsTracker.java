package com.jdroid.android.about.analytics;

import com.jdroid.android.google.analytics.AbstractGoogleAnalyticsTracker;

public class GoogleAboutAnalyticsTracker extends AbstractGoogleAnalyticsTracker implements AboutAnalyticsTracker {

	private static final String ABOUT_CATEGORY = "about";

	@Override
	public void trackAboutLibraryOpen(String libraryKey) {
		getGoogleAnalyticsHelper().sendEvent(ABOUT_CATEGORY, "openLibrary", libraryKey);
	}

	@Override
	public void trackContactUs() {
		getGoogleAnalyticsHelper().sendEvent(ABOUT_CATEGORY, "contactUs", "contactUs");
	}
}
