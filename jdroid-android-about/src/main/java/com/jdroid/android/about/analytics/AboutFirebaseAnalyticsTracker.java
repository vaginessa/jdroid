package com.jdroid.android.about.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;

public class AboutFirebaseAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements AboutAnalyticsTracker {

	@Override
	public void trackAboutLibraryOpen(String libraryKey) {
		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.ITEM_ID, libraryKey);
		getFirebaseAnalyticsHelper().sendEvent("openLibrary", bundle);
	}

	@Override
	public void trackContactUs() {
		getFirebaseAnalyticsHelper().sendEvent("contactUs");
	}
}
