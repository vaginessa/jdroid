package com.jdroid.android.application.lifecycle;

class ApplicationLifecycleInitProvider extends AbstractInitProvider {
	
	@Override
	protected void init() {
		ApplicationLifecycleHelper.onProviderInit(getContext());
	}
}
