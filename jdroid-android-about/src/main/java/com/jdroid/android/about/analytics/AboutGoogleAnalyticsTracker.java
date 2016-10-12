package com.jdroid.android.about.analytics;

import com.jdroid.android.google.analytics.AbstractGoogleAnalyticsTracker;
import com.jdroid.android.google.analytics.GoogleAnalyticsTracker;

public class AboutGoogleAnalyticsTracker extends AbstractGoogleAnalyticsTracker implements AboutAnalyticsTracker {

	private static final String ABOUT_CATEGORY = "about";

	@Override
	public void trackAboutLibraryOpen(String libraryKey) {
		getGoogleAnalyticsHelper().sendEvent(ABOUT_CATEGORY, "openLibrary", libraryKey);
	}

	@Override
	public void trackContactUs() {
		getGoogleAnalyticsHelper().sendEvent(ABOUT_CATEGORY, "contactUs", "contactUs");
	}

	@Override
	public void trackSendAppInvitation(String invitationId) {
		getGoogleAnalyticsHelper().sendEvent(GoogleAnalyticsTracker.SOCIAL, "sendAppInvitation", invitationId);
	}
}
