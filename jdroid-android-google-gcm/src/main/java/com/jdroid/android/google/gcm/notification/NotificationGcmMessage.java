package com.jdroid.android.google.gcm.notification;

import android.os.Bundle;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.gcm.GcmMessage;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;

public class NotificationGcmMessage implements GcmMessage {

	private static final String MESSAGE_KEY = "notificationMessage";

	@Override
	public void handle(String from, Bundle data) {
		NotificationBuilder builder = new NotificationBuilder(getMessageKey(), data);
		builder.setSmallIcon(getSmallIconResId());
		builder.setPublicVisibility();
		builder.setWhen(DateUtils.nowMillis());
		configureBuilder(builder);

		NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
	}

	protected void configureBuilder(NotificationBuilder notificationBuilder) {
		// Do Nothing
	}

	protected int getSmallIconResId() {
		return AbstractApplication.get().getLauncherIconResId();
	}

	@Override
	public String getMessageKey() {
		return MESSAGE_KEY;
	}
}
