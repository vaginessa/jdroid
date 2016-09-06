package com.jdroid.android.google.instanceid;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class InstanceIdService extends FirebaseInstanceIdService {

	private final static Logger LOGGER = LoggerUtils.getLogger(InstanceIdService.class);

	/**
	 * Called if InstanceID token is updated. This may occur if the security of
	 * the previous token had been compromised. Note that this is also called
	 * when the InstanceID token is initially generated, so this is where
	 * you retrieve the token.
	 */
	@Override
	public void onTokenRefresh() {
		LOGGER.info("Refreshing Instance Id Tokens");
		InstanceIdHelper.clearInstanceId();

		for (AppModule appModule : AbstractApplication.get().getAppModules()) {
			appModule.onInstanceIdTokenRefresh();
		}
	}
};