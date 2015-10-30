package com.jdroid.javaweb.google.gcm;

import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.push.DeviceType;
import com.jdroid.javaweb.push.PushMessage;
import com.jdroid.javaweb.push.PushMessageSender;
import com.jdroid.javaweb.push.PushResponse;

import org.slf4j.Logger;

public class GcmSender implements PushMessageSender {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(GcmSender.class);
	
	private final static PushMessageSender INSTANCE = new GcmSender();

	private GcmApiService gcmApiService = new GcmApiService();
	
	private GcmSender() {
	}
	
	public static PushMessageSender get() {
		return INSTANCE;
	}
	
	@Override
	public PushResponse send(PushMessage pushMessage) {
		
		GcmMessage gcmMessage = (GcmMessage)pushMessage;

		GcmResponse gcmResponse = gcmApiService.sendMessage(gcmMessage);

		PushResponse pushResponse = new PushResponse(DeviceType.ANDROID);
		if (!gcmResponse.isOk()) {
			for (int i = 0; i < gcmResponse.getResults().size(); i++) {
				GcmResult each = gcmResponse.getResults().get(i);
				if (each.getMessageId() != null) {
					if (each.getRegistrationId() != null) {
						// Replace the original ID with the new value (canonical ID) in your server database.
						// Note that the original ID is not part of the result, so you need to obtain it from the list of registration_ids passed in
						// the request (using the same index).
						String registrationIdToReplace = gcmMessage.getRegistrationIds().get(i);
						pushResponse.addRegistrationTokenToReplace(registrationIdToReplace, each.getRegistrationId());
						LOGGER.info("Registration id [" + registrationIdToReplace + "] to be replaced by " + each.getRegistrationId());
					}
				} else {
					LOGGER.info("Error [" + each.getError() + "] when sending GCM message/s");
					if ("Unavailable".equals(each.getError())) {
						// TODO
						// The server couldn't process the request in time. Retry the same request, but you must:
						// 	Honor the Retry-After header if it is included in the response from the GCM Connection Server.
						// 	Implement exponential back-off in your retry mechanism. (e.g. if you waited one second before the first retry,
						// 	wait at least two second before the next one, then 4 seconds and so on). If you're sending multiple messages,
						// 	delay each one independently by an additional random amount to avoid issuing a new request for all messages at the same time.
						// Senders that cause problems risk being blacklisted.
					} else if ("NotRegistered".equals(each.getError())) {
						// you should remove the registration ID from your server database because the application was uninstalled from the device,
						// or the client app isn't configured to receive messages.
						String registrationIdToRemove = null;
						if (gcmMessage.getTo() != null) {
							registrationIdToRemove = gcmMessage.getTo();
						} else {
							registrationIdToRemove = gcmMessage.getRegistrationIds().get(i);
						}
						LOGGER.info("Registration id [" + registrationIdToRemove + "] to be removed");
						pushResponse.addRegistrationTokenToRemove(registrationIdToRemove);
					}
				}
			}
		} else {
			LOGGER.info("Gcm message sent successfully");
		}
		return pushResponse;
	}
}
