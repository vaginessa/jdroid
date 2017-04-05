package com.jdroid.android.sample.ui.google.admob;

import com.jdroid.android.firebase.admob.AdMobAppContext;
import com.jdroid.android.firebase.admob.AdMobAppModule;

public class SampleAdMobAppModule extends AdMobAppModule {

	@Override
	protected AdMobAppContext createAdMobAppContext() {
		return new SampleAdMobAppContext();
	}
}
