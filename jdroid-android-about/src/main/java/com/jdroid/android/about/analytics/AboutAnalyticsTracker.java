package com.jdroid.android.about.analytics;

import com.jdroid.java.analytics.AnalyticsTracker;

public interface AboutAnalyticsTracker extends AnalyticsTracker {

	public void trackAboutLibraryOpen(String libraryKey);

	public void trackContactUs();

}
