package com.jdroid.android.google.gcm;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class GcmListenerResolver {

	private static final Logger LOGGER = LoggerUtils.getLogger(GcmListenerResolver.class);

	/**
	 * Called when message is received.
	 */
	public void onMessageReceived(RemoteMessage remoteMessage) {
		LOGGER.info("Message received from : " + remoteMessage.getFrom() + ", with data: " + remoteMessage.getData());

		GcmMessageResolver gcmResolver = AbstractGcmAppModule.get().getGcmMessageResolver(remoteMessage.getFrom());
		if (gcmResolver != null) {
			GcmMessage gcmMessage = null;
			try {
				gcmMessage = gcmResolver.resolve(remoteMessage);
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException("Error when resolving gcm message", e);
				return;
			}

			if (gcmMessage != null) {
				try {
					gcmMessage.handle(remoteMessage);
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException("Error when handling gcm message", e);
				}
			} else {
				LOGGER.warn("The GCM message was not resolved");
			}
		} else {
			LOGGER.warn("A GCM message was received, but not resolved is configured");
		}
	}

	public void onMessageSent(String msgId) {
		LOGGER.info("Message sent with id: " + msgId);
	}

	public void onSendError(String msgId, Exception exception) {
		AbstractApplication.get().getExceptionHandler().logWarningException("Send error. Message id: " + msgId, exception);
	}

	public void onDeletedMessages() {
		LOGGER.info("Deleted messages");
	}
}
