package com.jdroid.android.google.admob;

import android.content.Context;

import com.jdroid.android.application.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigHelper;


public class AdMobAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		
		FirebaseRemoteConfigHelper.addRemoteConfigParameters(AdMobRemoteConfigParameter.values());
	}
}
