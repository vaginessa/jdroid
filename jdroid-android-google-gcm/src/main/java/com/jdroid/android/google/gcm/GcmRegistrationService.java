package com.jdroid.android.google.gcm;

import android.content.Context;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.TaskParams;
import com.google.android.gms.iid.InstanceID;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

public class GcmRegistrationService extends GcmTaskService {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(GcmRegistrationService.class);

	@Override
	public void onInitializeTasks() {
		GcmRegistrationService.start();
	}

	@Override
	public int onRunTask(TaskParams taskParams) {

		LOGGER.info("Starting GCM registration");
		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(this)) {

			String registrationToken = null;
			try {
				registrationToken = getRegistrationToken(this);
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException("Failed to register the device on gcm. Will retry later.", e);
				return GcmNetworkManager.RESULT_RESCHEDULE;
			}

			try {
				LOGGER.info("Registering GCM token on server");
				AbstractGcmAppModule.get().onRegisterOnServer(registrationToken);
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException("Failed to register the device on server. Will retry later.", e);
				return GcmNetworkManager.RESULT_RESCHEDULE;
			}

			try {
				subscribeTopics(registrationToken);
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException("Failed to subscribe to topic channels. Will retry later.", e);
				return GcmNetworkManager.RESULT_RESCHEDULE;
			}
			return GcmNetworkManager.RESULT_SUCCESS;
		} else {
			LOGGER.warn("GCM not initialized because Google Play Services is not available");
			return GcmNetworkManager.RESULT_RESCHEDULE;
		}
	}

	/**
	 * Subscribe to any GCM topics of interest, as defined by the getSubscriptionTopics method.
	 *
	 * @param token GCM token
	 * @throws IOException if unable to reach the GCM PubSub service
	 */
	private void subscribeTopics(String token) throws IOException {
		List<String> subscriptionTopics = AbstractGcmAppModule.get().getSubscriptionTopics();
		if (!Lists.isNullOrEmpty(subscriptionTopics)) {
			GcmPubSub pubSub = GcmPubSub.getInstance(this);
			for (String topic : subscriptionTopics) {
				pubSub.subscribe(token, "/topics/" + topic, null);
			}
		}
	}

	public static void start() {
		OneoffTask.Builder builder = new OneoffTask.Builder();
		builder.setService(GcmRegistrationService.class);
		builder.setExecutionWindow(0, 5);
		builder.setPersisted(true);
		builder.setTag(GcmRegistrationService.class.getSimpleName());
		builder.build();

		LOGGER.info("Scheduling GCM Registration");
		GcmNetworkManager.getInstance(AbstractApplication.get()).schedule(builder.build());
	}

	public static String getRegistrationToken(Context context) throws IOException {
		String senderId = AbstractGcmAppModule.get().getGcmContext().getSenderId();
		if (senderId == null) {
			throw new UnexpectedException("Missing GCM Sender Id");
		}

		InstanceID instanceID = InstanceID.getInstance(context);
		return instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
	}
}
