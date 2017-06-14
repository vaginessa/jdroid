package com.jdroid.android.sample.application;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.jdroid.android.notification.NotificationChannelType;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.LocalizationUtils;

@SuppressLint("NewApi")
public enum AndroidNotificationChannelType implements NotificationChannelType {
	
	LOW_IMPORTANCE("lowImportance", R.string.lowImportanceNotificationChannelName) {
		
		@Override
		public int getImportance() {
			return NotificationManager.IMPORTANCE_LOW;
		}
		
		@Override
		public void config(NotificationChannel notificationChannel) {
			notificationChannel.setShowBadge(false);
		}
	},
	DEFAULT_IMPORTANCE("defaultImportance", R.string.defaultImportanceNotificationChannelName) {
		
		@Override
		public int getImportance() {
			return NotificationManager.IMPORTANCE_DEFAULT;
		}
		
	},
	HIGH_IMPORTANCE("highImportance", R.string.highImportanceNotificationChannelName) {
		
		@Override
		public int getImportance() {
			return NotificationManager.IMPORTANCE_HIGH;
		}
		
		@Override
		public void config(NotificationChannel notificationChannel) {
			notificationChannel.setShowBadge(true);
		}
	};
	
	private String id;
	private int nameResId;
	
	AndroidNotificationChannelType(String id, int nameResId) {
		this.id = id;
		this.nameResId = nameResId;
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
	public String getName() {
		return LocalizationUtils.getString(nameResId);
	}
}
