package com.jdroid.android.notification;

import android.app.NotificationChannel;

public interface NotificationChannelType {
	
	String getChannelId();
	
	String getName();
	
	int getImportance();
	
	/*
	 * Return false if the notification channel should be created. Return true if the notification channel was already
	 * created in the past, and should be removed because is currently deprecated.
	 */
	Boolean isDeprecated();
	
	public void config(NotificationChannel notificationChannel);
}
