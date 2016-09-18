package com.jdroid.android.sample.firebase.fcm;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.fcm.FcmMessage;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.java.date.DateTimeFormat;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.NumberUtils;

import java.util.Date;

/**
 * FCM Message types
 */
public enum AndroidFcmMessage implements FcmMessage {
	
	SAMPLE_MESSAGE("sampleMessage") {
		
		@Override
		public void handle(RemoteMessage remoteMessage) {
			NotificationBuilder builder = new NotificationBuilder("pushNotification");
			builder.setSmallIcon(AbstractApplication.get().getLauncherIconResId());
			builder.setTicker("Sample Ticker");
			builder.setContentTitle("Sample Content Title");
			String description = "Sample Content Description";
			Long timestamp = NumberUtils.getLong(remoteMessage.getData().get("timestamp"));
			if (timestamp != null) {
				description = DateUtils.format(new Date(timestamp), DateTimeFormat.YYYYMMDDHHMMSSSSS);
			}
			builder.setContentText(description);
			builder.setWhen(DateUtils.nowMillis());

			NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
		}
	};
	
	private String messageKey;
	
	AndroidFcmMessage(String messageKey) {
		this.messageKey = messageKey;
	}
	
	@Override
	public String getMessageKey() {
		return messageKey;
	}
}
