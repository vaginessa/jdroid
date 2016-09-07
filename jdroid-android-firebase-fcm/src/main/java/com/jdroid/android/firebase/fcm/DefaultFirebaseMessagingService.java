package com.jdroid.android.firebase.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * Service to handle FCM messages.
 */
public class DefaultFirebaseMessagingService extends FirebaseMessagingService {

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		AbstractFcmAppModule.get().getFcmListenerResolver().onMessageReceived(remoteMessage);
	}

	@Override
	public void onMessageSent(String msgId) {
		AbstractFcmAppModule.get().getFcmListenerResolver().onMessageSent(msgId);
	}

	@Override
	public void onSendError(String msgId, Exception exception) {
		AbstractFcmAppModule.get().getFcmListenerResolver().onSendError(msgId, exception);
	}

	@Override
	public void onDeletedMessages() {
		AbstractFcmAppModule.get().getFcmListenerResolver().onDeletedMessages();
	}
}
