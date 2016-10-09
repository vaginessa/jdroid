package com.jdroid.android.sample.ui.google.admob;

import com.jdroid.android.google.admob.AdMobAppContext;
import com.jdroid.android.google.admob.AdMobAppModule;

public class SampleAdMobAppModule extends AdMobAppModule {

	@Override
	protected AdMobAppContext createAdMobAppContext() {
		return new SampleAdMobAppContext();
	}
}
