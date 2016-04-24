package com.jdroid.android.about.analytics;

import com.jdroid.java.analytics.BaseAnalyticsTracker;

public interface AboutAnalyticsTracker extends BaseAnalyticsTracker {

	public void trackAboutLibraryOpen(String libraryKey);

	public void trackContactUs();

	// App Invitations

	public void trackSendAppInvitation(String invitationId);
}
