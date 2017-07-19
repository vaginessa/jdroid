package com.jdroid.android.firebase.instanceid;

import android.support.annotation.WorkerThread;

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
	 *
	 * Since Android O, have a guaranteed life cycle limited to 10 seconds for this method execution.
	 * To avoid your process being terminated before your callback is completed, be sure to perform only quick operations
	 * (like updating a local database, or displaying a custom notification) inside the callback, and use JobScheduler to
	 * schedule longer background processes (like downloading additional images or syncing the database with a remote source).
	 */
	@WorkerThread
	@Override
	public void onTokenRefresh() {
		LOGGER.info("Refreshing Instance Id Tokens");

		// TODO Is this required? It seems that the instance id is not changed here
		InstanceIdHelper.clearInstanceId();

		for (AppModule appModule : AbstractApplication.get().getAppModules()) {
			appModule.onInstanceIdTokenRefresh();
		}
	}
};