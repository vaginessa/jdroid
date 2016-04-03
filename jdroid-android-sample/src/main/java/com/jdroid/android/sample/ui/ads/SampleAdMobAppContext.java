package com.jdroid.android.sample.ui.ads;

import com.jdroid.android.google.admob.AdMobAppContext;

public class SampleAdMobAppContext extends AdMobAppContext {

	@Override
	public Boolean areAdsEnabled() {
		return true;
	}
}
