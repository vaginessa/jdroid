package com.jdroid.android.sample.analytics;

import android.os.Bundle;

import com.jdroid.android.firebase.analytics.FirebaseAnalyticsTracker;

public class AndroidFirebaseAnalyticsTracker extends FirebaseAnalyticsTracker implements AppAnalyticsTracker {
	
	@Override
	public void trackExampleEvent() {
		Bundle bundle = new Bundle();
		bundle.putString("exampleParam", "exampleValue");
		getFirebaseAnalyticsHelper().sendEvent("ExampleAction", bundle);
	}

	@Override
	public void trackExampleTransaction() {
		// Do nothing
	}

	@Override
	public void trackExampleTiming() {
		// Do nothing
	}
	
}
