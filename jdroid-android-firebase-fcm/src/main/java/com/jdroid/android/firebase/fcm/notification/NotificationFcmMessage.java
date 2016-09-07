package com.jdroid.android.firebase.fcm.notification;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.fcm.FcmMessage;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;

public class NotificationFcmMessage implements FcmMessage {

	private static final String MESSAGE_KEY = "notificationMessage";

	@Override
	public void handle(RemoteMessage remoteMessage) {
		NotificationBuilder builder = new NotificationBuilder(getMessageKey(), remoteMessage.getData());
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
