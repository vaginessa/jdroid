package com.jdroid.android.sample.gcm;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.gcm.GcmMessage;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.java.date.DateTimeFormat;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.NumberUtils;

import java.util.Date;

/**
 * GCM Message types
 */
public enum AndroidGcmMessage implements GcmMessage {
	
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
	
	AndroidGcmMessage(String messageKey) {
		this.messageKey = messageKey;
	}
	
	@Override
	public String getMessageKey() {
		return messageKey;
	}
}
