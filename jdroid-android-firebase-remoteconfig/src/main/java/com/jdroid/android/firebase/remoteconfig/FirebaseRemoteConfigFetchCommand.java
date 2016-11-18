package com.jdroid.android.firebase.remoteconfig;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.Task;
import com.jdroid.android.service.ServiceCommand;

public class FirebaseRemoteConfigFetchCommand extends ServiceCommand {

	public static final String CACHE_EXPIRATION_SECONDS = "cacheExpirationSeconds";
	public static final String SET_EXPERIMENT_USER_PROPERTY = "setExperimentUserProperty";

	@Override
	protected int execute(Bundle bundle) {
		Long cacheExpirationSeconds = bundle.getLong(CACHE_EXPIRATION_SECONDS);
		Boolean setExperimentUserProperty = bundle.getBoolean(SET_EXPERIMENT_USER_PROPERTY);
		FirebaseRemoteConfigHelper.fetch(cacheExpirationSeconds, setExperimentUserProperty);
		return GcmNetworkManager.RESULT_SUCCESS;
	}

	@Override
	protected Task.Builder createRetryTaskBuilder() {
		return super.createRetryTaskBuilder().setRequiredNetwork(Task.NETWORK_STATE_CONNECTED);
	}

	@Override
	protected Boolean requiresInstantExecution() {
		return false;
	}
}
