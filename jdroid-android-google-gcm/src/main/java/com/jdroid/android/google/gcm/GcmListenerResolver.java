package com.jdroid.android.google.gcm;

import android.os.Bundle;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class GcmListenerResolver {

	private static final Logger LOGGER = LoggerUtils.getLogger(GcmListenerResolver.class);

	/**
	 * Called when message is received.
	 *
	 * @param from SenderID of the sender.
	 * @param data Data bundle containing message data as key/value pairs. For Set of keys use data.keySet().
	 */
	public void onMessageReceived(String from, Bundle data) {
		LOGGER.info("Message received from : " + from + ", with data: " + data.toString());

		GcmMessageResolver gcmResolver = AbstractGcmAppModule.get().getGcmMessageResolver(from);
		if (gcmResolver != null) {
			GcmMessage gcmMessage = null;
			try {
				gcmMessage = gcmResolver.resolve(from, data);
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException("Error when resolving gcm message", e);
			}

			if (gcmMessage != null) {
				try {
					gcmMessage.handle(from, data);
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

	public void onSendError(String msgId, String error) {
		AbstractApplication.get().getExceptionHandler().logWarningException("Send error. Message id: " + msgId + ", Error: " + error);
	}

	public void onDeletedMessages() {
		LOGGER.info("Deleted messages");
	}
}
