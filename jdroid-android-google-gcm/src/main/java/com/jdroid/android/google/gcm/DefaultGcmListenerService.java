package com.jdroid.android.google.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Service to handle Google Cloud Messaging (GCM) messages.
 */
public class DefaultGcmListenerService extends GcmListenerService {
	
	@Override
	public void onMessageReceived(String from, Bundle data) {
		AbstractGcmAppModule.get().getGcmListenerResolver().onMessageReceived(from, data);
	}

	@Override
	public void onMessageSent(String msgId) {
		AbstractGcmAppModule.get().getGcmListenerResolver().onMessageSent(msgId);
	}

	@Override
	public void onSendError(String msgId, String error) {
		AbstractGcmAppModule.get().getGcmListenerResolver().onSendError(msgId, error);
	}

	@Override
	public void onDeletedMessages() {
		AbstractGcmAppModule.get().getGcmListenerResolver().onDeletedMessages();
	}
}
