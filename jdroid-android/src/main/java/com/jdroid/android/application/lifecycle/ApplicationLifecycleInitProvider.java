package com.jdroid.android.application.lifecycle;

import com.jdroid.java.annotation.Internal;

@Internal
public class ApplicationLifecycleInitProvider extends AbstractInitProvider {
	
	@Override
	protected void init() {
		ApplicationLifecycleHelper.onProviderInit(getContext());
	}
}
