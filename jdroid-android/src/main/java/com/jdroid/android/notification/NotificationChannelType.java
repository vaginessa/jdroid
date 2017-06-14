package com.jdroid.android.notification;

import android.app.NotificationChannel;

public interface NotificationChannelType {
	
	String getId();
	
	String getName();
	
	int getImportance();
	
	public void config(NotificationChannel notificationChannel);
}
