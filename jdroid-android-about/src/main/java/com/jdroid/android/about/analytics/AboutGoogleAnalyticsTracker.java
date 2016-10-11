package com.jdroid.android.about.analytics;

import com.jdroid.android.google.analytics.GoogleAnalyticsAppModule;
import com.jdroid.android.google.analytics.GoogleAnalyticsHelper;
import com.jdroid.android.google.analytics.GoogleAnalyticsTracker;

public class AboutGoogleAnalyticsTracker implements AboutAnalyticsTracker {

	private static final String ABOUT_CATEGORY = "about";

	private GoogleAnalyticsHelper googleAnalyticsHelper;

	public AboutGoogleAnalyticsTracker() {
		googleAnalyticsHelper = GoogleAnalyticsAppModule.get().getGoogleAnalyticsHelper();
	}

	@Override
	public Boolean isEnabled() {
		return GoogleAnalyticsAppModule.get().getGoogleAnalyticsAppContext().isGoogleAnalyticsEnabled();
	}

	@Override
	public void trackAboutLibraryOpen(String libraryKey) {
		googleAnalyticsHelper.sendEvent(ABOUT_CATEGORY, "openLibrary", libraryKey);
	}

	@Override
	public void trackContactUs() {
		googleAnalyticsHelper.sendEvent(ABOUT_CATEGORY, "contactUs", "contactUs");
	}

	@Override
	public void trackSendAppInvitation(String invitationId) {
		googleAnalyticsHelper.sendEvent(GoogleAnalyticsTracker.SOCIAL, "sendAppInvitation", invitationId);
	}
}
