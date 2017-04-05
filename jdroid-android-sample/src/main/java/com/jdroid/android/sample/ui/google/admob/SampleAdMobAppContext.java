package com.jdroid.android.sample.ui.google.admob;

import com.jdroid.android.firebase.admob.AdMobAppContext;

public class SampleAdMobAppContext extends AdMobAppContext {

	@Override
	public Boolean areAdsEnabled() {
		return true;
	}
}
