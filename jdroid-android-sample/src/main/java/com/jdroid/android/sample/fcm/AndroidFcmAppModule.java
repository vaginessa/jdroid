package com.jdroid.android.sample.fcm;

import android.os.Bundle;

import com.jdroid.android.firebase.fcm.AbstractFcmAppModule;
import com.jdroid.android.firebase.fcm.FcmDebugContext;
import com.jdroid.android.firebase.fcm.FcmMessageResolver;
import com.jdroid.android.firebase.fcm.FcmSender;
import com.jdroid.android.firebase.fcm.device.Device;
import com.jdroid.android.sample.api.SampleApiService;
import com.jdroid.android.sample.application.AndroidApplication;
import com.jdroid.android.sample.debug.AndroidFcmDebugContext;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AndroidFcmAppModule extends AbstractFcmAppModule {

	@Override
	public FcmMessageResolver createFcmMessageResolver() {
		return new AndroidFcmResolver();
	}

	@Override
	protected FcmDebugContext createFcmDebugContext() {
		return new AndroidFcmDebugContext();
	}

	@Override
	public List<FcmSender> getFcmSenders() {
		return Lists.newArrayList((FcmSender)AndroidApplication.get().getAppContext().getServer());
	}

	@Override
	public void onRegisterOnServer(String registrationToken, Boolean updateLastActiveTimestamp, Bundle bundle) {
		SampleApiService service = new SampleApiService();

		Device device = new Device(registrationToken, null);
		service.addDevice(device, updateLastActiveTimestamp);
	}
}
