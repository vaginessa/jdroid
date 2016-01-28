package com.jdroid.android.sample.gcm;

import android.os.Bundle;

import com.jdroid.android.google.gcm.AbstractGcmAppModule;
import com.jdroid.android.google.gcm.Device;
import com.jdroid.android.google.gcm.GcmDebugContext;
import com.jdroid.android.google.gcm.GcmMessageResolver;
import com.jdroid.android.google.gcm.GcmSender;
import com.jdroid.android.sample.api.SampleApiService;
import com.jdroid.android.sample.application.AndroidApplication;
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
	public GcmSender getGcmSender() {
		return (GcmSender)AndroidApplication.get().getAppContext().getServer();
	}

	@Override
	public void onRegisterOnServer(String registrationToken, Boolean updateLastActiveTimestamp, Bundle bundle) {
		SampleApiService service = new SampleApiService();

		Device device = new Device(registrationToken, null);
		service.addDevice(device, updateLastActiveTimestamp);
	}
}
