package com.jdroid.android.firebase.fcm.notification;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.StringUtils;

public class NotificationFcmMessage extends NotificationMessage {

	private static final String MESSAGE_KEY = "notificationFcmMessage";

	@Override
	protected void initTicker(RemoteMessage remoteMessage, NotificationBuilder builder) {
		builder.setTicker(remoteMessage.getNotification().getTitle());
	}

	@Override
	protected void initContentTitle(RemoteMessage remoteMessage, NotificationBuilder builder) {
		builder.setContentTitle(remoteMessage.getNotification().getTitle());
	}

	@Override
	protected void initContentText(RemoteMessage remoteMessage, NotificationBuilder builder) {
		builder.setContentText(remoteMessage.getNotification().getBody());
	}

	@Override
	protected void initContentIntent(RemoteMessage remoteMessage, NotificationBuilder builder) {
		RemoteMessage.Notification notification = remoteMessage.getNotification();
		String url = notification.getClickAction() != null ? notification.getClickAction() : remoteMessage.getData().get(URL);
		if (StringUtils.isNotEmpty(url)) {
			builder.setSingleTopUrl(url);
		} else {
			throw new UnexpectedException("Missing " + URL + " extra for " + getMessageKey());
		}
	}

	@Override
	public String getMessageKey() {
		return MESSAGE_KEY;
	}
}
