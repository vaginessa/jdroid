package com.jdroid.android.firebase.fcm.notification;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.fcm.FcmMessage;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.NumberUtils;
import com.jdroid.java.utils.StringUtils;

public class NotificationMessage implements FcmMessage {

	public static final String TICKER = "ticker";
	public static final String CONTENT_TITLE = "contentTitle";
	public static final String CONTENT_TEXT = "contentText";
	public static final String SOUND_ENABLED = "soundEnabled";
	public static final String VIBRATION_ENABLED = "vibrationEnabled";
	public static final String LIGHT_ENABLED = "lightEnabled";
	public static final String URL = "url";

	private static final String MESSAGE_KEY = "notificationMessage";

	@Override
	public void handle(RemoteMessage remoteMessage) {
		NotificationBuilder builder = new NotificationBuilder(getMessageKey());

		String ticker = remoteMessage.getData().get(TICKER);
		if (StringUtils.isNotEmpty(ticker)) {
			builder.setTicker(ticker);
		} else {
			throw new UnexpectedException("Missing " + TICKER + " extra for " + getMessageKey());
		}

		String contentTitle = remoteMessage.getData().get(CONTENT_TITLE);
		if (StringUtils.isNotEmpty(contentTitle)) {
			builder.setContentTitle(contentTitle);
		} else {
			throw new UnexpectedException("Missing " + CONTENT_TITLE + " extra for " + getMessageKey());
		}

		String contentText = remoteMessage.getData().get(CONTENT_TEXT);
		if (StringUtils.isNotEmpty(contentText)) {
			builder.setContentText(contentText);
		} else {
			throw new UnexpectedException("Missing " + CONTENT_TEXT + " extra for " + getMessageKey());
		}

		if (NumberUtils.getBoolean(remoteMessage.getData().get(SOUND_ENABLED), false)) {
			builder.setDefaultSound();
		}
		if (NumberUtils.getBoolean(remoteMessage.getData().get(VIBRATION_ENABLED), false)) {
			builder.setDefaultVibration();
		}
		if (NumberUtils.getBoolean(remoteMessage.getData().get(LIGHT_ENABLED), false)) {
			builder.setWhiteLight();
		}

		String url = remoteMessage.getData().get(URL);
		if (StringUtils.isNotEmpty(url)) {
			builder.setSingleTopUrl(url);
		} else {
			throw new UnexpectedException("Missing " + URL + " extra for " + getMessageKey());
		}

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
