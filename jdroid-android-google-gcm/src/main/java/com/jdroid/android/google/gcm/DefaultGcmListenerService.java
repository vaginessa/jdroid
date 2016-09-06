package com.jdroid.android.google.gcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * Service to handle Google Cloud Messaging (GCM) messages.
 */
public class DefaultGcmListenerService extends FirebaseMessagingService {

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		AbstractGcmAppModule.get().getGcmListenerResolver().onMessageReceived(remoteMessage);
	}

	@Override
	public void onMessageSent(String msgId) {
		AbstractGcmAppModule.get().getGcmListenerResolver().onMessageSent(msgId);
	}

	@Override
	public void onSendError(String msgId, Exception exception) {
		AbstractGcmAppModule.get().getGcmListenerResolver().onSendError(msgId, exception);
	}

	@Override
	public void onDeletedMessages() {
		AbstractGcmAppModule.get().getGcmListenerResolver().onDeletedMessages();
	}
}
