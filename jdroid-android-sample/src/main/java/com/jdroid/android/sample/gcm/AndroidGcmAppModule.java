package com.jdroid.android.sample.gcm;

import com.jdroid.android.google.gcm.AbstractGcmAppModule;
import com.jdroid.android.google.gcm.Device;
import com.jdroid.android.google.gcm.GcmDebugContext;
import com.jdroid.android.google.gcm.GcmMessageResolver;
import com.jdroid.android.sample.api.SampleApiService;
import com.jdroid.android.sample.debug.AndroidGcmDebugContext;

public class AndroidGcmAppModule extends AbstractGcmAppModule {

	@Override
	public GcmMessageResolver createGcmMessageResolver() {
		return new AndroidGcmResolver();
	}

	@Override
	protected GcmDebugContext createGcmDebugContext() {
		return new AndroidGcmDebugContext();
	}

	@Override
	public void onRegisterOnServer(String registrationToken) {
		SampleApiService service = new SampleApiService();

		Device device = new Device(registrationToken, null);
		service.addDevice(device);
	}
}
