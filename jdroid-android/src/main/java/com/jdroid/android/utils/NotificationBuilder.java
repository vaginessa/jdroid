package com.jdroid.android.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.StringUtils;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 
 * @author Maxi Rosson
 */
public class NotificationBuilder {
	
	private NotificationCompat.Builder builder;
	
	public NotificationBuilder() {
		Context context = AbstractApplication.get();
		builder = new NotificationCompat.Builder(context);
		builder.setAutoCancel(true);
	}
	
	public Notification build() {
		return builder.build();
	}
	
	public void setSmallIcon(int icon) {
		builder.setSmallIcon(icon);
	}
	
	public void setTicker(int tickerId, Object... args) {
		setTicker(LocalizationUtils.getString(tickerId, args));
	}
	
	public void setTicker(String tickerText) {
		builder.setTicker(tickerText);
	}
	
	public void setContentTitle(int contentTitleId, Object... args) {
		setContentTitle(LocalizationUtils.getString(contentTitleId, args));
	}
	
	public void setContentTitle(String contentTitle) {
		builder.setContentTitle(contentTitle);
	}
	
	public void setContentText(int contentTextId, Object... args) {
		setContentText(LocalizationUtils.getString(contentTextId, args));
	}
	
	public void setContentText(String contentText) {
		builder.setContentText(contentText);
	}
	
	public void setContentIntentSingleTop(Class<?> clazz) {
		setContentIntentSingleTop(clazz, null);
	}
	
	public void setContentIntentSingleTop(Class<?> clazz, Bundle notificationBundle) {
		Intent notificationIntent = clazz != null ? new Intent(AbstractApplication.get(), clazz) : new Intent();
		if (notificationBundle != null) {
			notificationIntent.replaceExtras(notificationBundle);
		}
		setContentIntentSingleTop(notificationIntent);
	}
	
	public void setContentIntentSingleTop(Intent notificationIntent) {
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		builder.setContentIntent(PendingIntent.getActivity(AbstractApplication.get(), IdGenerator.getIntId(),
			notificationIntent, 0));
	}
	
	public void setWhen(Long when) {
		if (when != null) {
			builder.setWhen(when);
		}
	}
	
	public void setLargeIcon(Bitmap largeIcon) {
		if (largeIcon != null) {
			builder.setLargeIcon(largeIcon);
		}
	}
	
	public void setLargeIcon(String largeIconUrl) {
		if (largeIconUrl != null) {
			builder.setLargeIcon(createLargeIconBitmap(largeIconUrl));
		}
	}
	
	public void setInProgress(int notificationIcon, int progress, int contentTitle, int actionText) {
		builder.setOngoing(true);
		
		Context context = AbstractApplication.get();
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_inprogress);
		remoteViews.setTextViewText(R.id.title, context.getString(contentTitle));
		remoteViews.setTextViewText(R.id.action, context.getString(actionText));
		remoteViews.setTextViewText(R.id.progressPercentage, progress + "%");
		remoteViews.setProgressBar(R.id.progressBar, 100, progress, false);
		remoteViews.setImageViewResource(R.id.icon, notificationIcon);
		builder.setContent(remoteViews);
	}
	
	public void setBlueLight() {
		builder.setLights(Color.parseColor("blue"), 1000, 5000);
	}
	
	public void setSound(int soundResId) {
		Uri notificationSound = Uri.parse("android.resource://" + AndroidUtils.getPackageName() + "/" + soundResId);
		if (notificationSound != null) {
			builder.setSound(notificationSound);
		}
	}
	
	public void setDefaultSound() {
		Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		if (notificationSound != null) {
			builder.setSound(notificationSound);
		}
	}
	
	private Bitmap createLargeIconBitmap(String largeIconUrl) {
		Bitmap largeIconBitmap = null;
		if (StringUtils.isNotEmpty(largeIconUrl)) {
			
			int height = AndroidUtils.convertDimenToPixel(android.R.dimen.notification_large_icon_height);
			int width = AndroidUtils.convertDimenToPixel(android.R.dimen.notification_large_icon_width);
			
			largeIconBitmap = ImageLoaderUtils.loadBitmap(largeIconUrl, ImageScaleType.EXACTLY, width, height);
		}
		return largeIconBitmap;
	}
	
}
