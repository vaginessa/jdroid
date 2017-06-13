package com.jdroid.android.sample.application;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.jdroid.android.notification.NotificationChannelType;
import com.jdroid.android.sample.R;

public enum AndroidNotificationChannelType implements NotificationChannelType {
	
	LOW_IMPORTANCE("lowImportance", R.string.lowImportanceNotificationChannelName, NotificationManager.IMPORTANCE_LOW) {
		public void config(NotificationChannel notificationChannel) {
			notificationChannel.setShowBadge(false);
		}
	},
	DEFAULT_IMPORTANCE("defaultImportance", R.string.defaultImportanceNotificationChannelName, NotificationManager.IMPORTANCE_DEFAULT),
	HIGH_IMPORTANCE("highImportance", R.string.highImportanceNotificationChannelName, NotificationManager.IMPORTANCE_HIGH) {
		public void config(NotificationChannel notificationChannel) {
			notificationChannel.setShowBadge(true);
		}
	};
	
	private String id;
	private int nameResId;
	private int importance;
	
	AndroidNotificationChannelType(String id, int nameResId, int importance) {
		this.id = id;
		this.nameResId = nameResId;
		this.importance = importance;
	}
	
	@Override
	public void config(NotificationChannel notificationChannel) {
		// Do nothing
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public int getNameResId() {
		return nameResId;
	}
	
	@Override
	public int getImportance() {
		return importance;
	}
}
