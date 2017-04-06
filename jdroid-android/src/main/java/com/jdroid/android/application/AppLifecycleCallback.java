package com.jdroid.android.application;

import android.content.Context;

import com.jdroid.android.application.lifecycle.ApplicationLifecycleCallback;


public class AppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		AbstractApplication.get().onProviderInit();
	}
	
	@Override
	public void onLocaleChanged(Context context) {
		AbstractApplication.get().onLocaleChanged();
	}
}
