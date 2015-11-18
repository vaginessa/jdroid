package com.jdroid.android.sample.gcm;

import android.os.Bundle;

import com.jdroid.android.google.gcm.GcmMessage;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.utils.DateTimeFormat;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.NumberUtils;

import java.util.Date;

/**
 * GCM Message types
 */
public enum AndroidGcmMessage implements GcmMessage {
	
	SAMPLE_MESSAGE("sampleMessage") {
		
		@Override
		public void handle(String from, Bundle data) {
			NotificationBuilder builder = new NotificationBuilder();
			builder.setNotificationName("pushNotification");
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setTicker(R.string.notificationTicker);
			builder.setContentTitle(R.string.notificationTitle);
			String description = LocalizationUtils.getString(R.string.notificationDescription);
			Long timestamp = NumberUtils.getLong(data.getString("timestamp"));
			if (timestamp != null) {
				description = DateUtils.format(new Date(timestamp), DateTimeFormat.YYYYMMDDHHMMSSSSS_DATE_FORMAT);
			}
			builder.setContentText(description);
			builder.setWhen(System.currentTimeMillis());

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
	
	public Boolean isNotificationEnabled() {
		return SharedPreferencesHelper.get().loadPreferenceAsBoolean(messageKey, true);
	}
	
}
