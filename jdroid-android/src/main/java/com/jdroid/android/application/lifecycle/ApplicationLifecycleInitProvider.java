package com.jdroid.android.application.lifecycle;

public class ApplicationLifecycleInitProvider extends AbstractInitProvider {
	
	@Override
	protected void init() {
		ApplicationLifecycleHelper.onProviderInit(getContext());
	}
}
