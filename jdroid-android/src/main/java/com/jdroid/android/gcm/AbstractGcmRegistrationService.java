package com.jdroid.android.gcm;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.service.WorkerService;
import com.jdroid.android.utils.GooglePlayUtils;
import com.jdroid.android.utils.IntentRetryUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.IOException;

public abstract class AbstractGcmRegistrationService extends WorkerService {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractGcmRegistrationService.class);
	
	/**
	 * @see com.jdroid.android.service.WorkerService#doExecute(android.content.Intent)
	 */
	@Override
	protected void doExecute(Intent intent) {
		
		if (GooglePlayUtils.isGooglePlayServicesAvailable(this)) {
			if (!GcmPreferences.isRegistered(this) || !GcmPreferences.isRegisteredOnServer(this)) {
				String registrationId;
				try {
					GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);
					
					String googleProjectId = AbstractApplication.get().getAppContext().getGoogleProjectId();
					if (googleProjectId == null) {
						throw new UnexpectedException("Missing Google Project ID");
					}
					registrationId = googleCloudMessaging.register(googleProjectId);
					GcmPreferences.setRegistrationId(getApplicationContext(), registrationId);
				} catch (IOException e) {
					LOGGER.warn("Failed to register the device on gcm. Will retry later.", e);
					IntentRetryUtils.retry(intent);
					return;
				}
				
				try {
					onRegisterOnServer(registrationId);
					GcmPreferences.setRegisteredOnServer(getApplicationContext(), true);
				} catch (AbstractException e) {
					LOGGER.warn("Failed to register the device on server. Will retry later.", e);
					IntentRetryUtils.retry(intent);
				}
			} else {
				LOGGER.info("Device already registered on GCM. Registration id: "
						+ GcmPreferences.getRegistrationId(this));
			}
		} else {
			LOGGER.warn("GCM not initialized because Google Play Services is not available");
		}
	}
	
	protected abstract void onRegisterOnServer(String registrationId);
	
	public static void runRegistrationService(Context context,
			Class<? extends AbstractGcmRegistrationService> serviceClass) {
		WorkerService.runIntentInService(context, new Intent(), serviceClass);
	}
}
