package com.jdroid.android.notification;

import android.app.NotificationChannel;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.jdroid.android.application.AbstractApplication;

public class NotificationChannelFactory {
	
	@RequiresApi(api = Build.VERSION_CODES.O)
	public static NotificationChannel createNotificationChannel(NotificationChannelType notificationChannelType) {
		CharSequence name = AbstractApplication.get().getString(notificationChannelType.getNameResId());
		NotificationChannel notificationChannel = new NotificationChannel(notificationChannelType.getId(), name, notificationChannelType.getImportance());
		notificationChannelType.config(notificationChannel);
		return notificationChannel;
	}
}
