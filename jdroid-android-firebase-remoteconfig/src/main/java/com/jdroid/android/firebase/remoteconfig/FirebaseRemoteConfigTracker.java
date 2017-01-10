package com.jdroid.android.firebase.remoteconfig;

import android.app.Activity;
import android.os.Bundle;

import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker;

public class FirebaseRemoteConfigTracker extends AbstractCoreAnalyticsTracker {

	@Override
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			FirebaseRemoteConfigHelper.fetchNowIfExpired();
		}
	}

	@Override
	public Boolean isEnabled() {
		return true;
	}
}
