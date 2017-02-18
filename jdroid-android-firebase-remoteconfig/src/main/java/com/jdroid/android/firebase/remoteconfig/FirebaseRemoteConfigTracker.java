package com.jdroid.android.firebase.remoteconfig;

import android.app.Activity;

import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker;

public class FirebaseRemoteConfigTracker extends AbstractCoreAnalyticsTracker {

	@Override
	public void onFirstActivityCreate(Activity activity) {
		FirebaseRemoteConfigHelper.init();
	}

	@Override
	public Boolean isEnabled() {
		return true;
	}
}
