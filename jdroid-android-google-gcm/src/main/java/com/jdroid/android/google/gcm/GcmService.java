package com.jdroid.android.google.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.service.WorkerService;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

/**
 * Base {@link IntentService} to handle Google Cloud Messaging (GCM) messages.
 * 
 * You need to add the following permissions to your manifest:
 * 
 * <pre>
 * &lt;permission android:name="[app package].permission.C2D_MESSAGE" android:protectionLevel="signature" />
 * &lt;uses-permission android:name="[app package].permission.C2D_MESSAGE" />
 * &lt;uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
 * &lt;uses-permission android:name="android.permission.WAKE_LOCK" />
 * &lt;uses-permission android:name="android.permission.GET_ACCOUNTS" />
 * </pre>
 */
public class GcmService extends WorkerService {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(GcmService.class);
	
	/**
	 * Constructor that does not set a sender id, useful when the sender id is context-specific.
	 */
	public GcmService() {
		super(GcmService.class.getSimpleName());
	}
	
	/**
	 * Called when a cloud message has been received.
	 * 
	 * @param context application's context.
	 * @param intent intent containing the message payload as extras.
	 */
	public void onMessage(Context context, Intent intent) {
		GcmMessageResolver gcmResolver = AbstractApplication.get().getGcmContext().getGcmResolver();
		if (gcmResolver != null) {
			GcmMessage gcmMessage = gcmResolver.resolve(intent);
			if (gcmMessage != null) {
				gcmMessage.handle(intent);
			}
		} else {
			LOGGER.warn("A GCM message was received, but not resolved is configured");
		}
	}
	
	/**
	 * Called to indicate that the server deleted some pending messages because they were collapsible.
	 * 
	 * @param context application's context.
	 */
	protected void onDeletedMessages(Context context, Intent intent) {
		LOGGER.warn("onDeletedMessages");
	}
	
	/**
	 * 
	 * @param context application's context.
	 */
	protected void onError(Context context, Intent intent) {
		LOGGER.warn("onError");
	}
	
	/**
	 * @see com.jdroid.android.service.WorkerService#doExecute(android.content.Intent)
	 */
	@Override
	protected void doExecute(Intent intent) {
		Context context = getApplicationContext();
		
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		String messageType = gcm.getMessageType(intent);
		
		if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			onError(context, intent);
			LOGGER.debug("Send error");
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			LOGGER.debug("Received deleted messages");
			onDeletedMessages(context, intent);
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
			LOGGER.debug("Received message");
			onMessage(context, intent);
		}
		
	}
	
	/**
	 * Called from the broadcast receiver.
	 * <p>
	 * Will process the received intent, call handleMessage(), registered(), etc. in background threads, with a wake
	 * lock, while keeping the service alive.
	 * 
	 * @param intent
	 */
	public static void start(Intent intent) {
		WorkerService.runIntentInService(AbstractApplication.get(), intent, GcmService.class, true);
	}
}
