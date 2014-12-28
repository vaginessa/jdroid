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
import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.StringUtils;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class NotificationBuilder {
	
	public static final String NOTIFICATION_NAME = "notificationName";
	
	private String notificationName;
	private NotificationCompat.Builder builder;
	
	public NotificationBuilder() {
		Context context = AbstractApplication.get();
		builder = new NotificationCompat.Builder(context);
		builder.setAutoCancel(true);
		setColor(R.color.colorPrimary);
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
		
		AppLoadingSource.NOTIFICATION.flagIntent(notificationIntent);
		if (notificationName != null) {
			AbstractApplication.get().getAnalyticsSender().trackNotificationDisplayed(notificationName);
			notificationIntent.putExtra(NOTIFICATION_NAME, notificationName);
		}
		
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
	
	public void setColor(int colorResId) {
		builder.setColor(AbstractApplication.get().getResources().getColor(colorResId));
	}
	
	public void setColor(String color) {
		builder.setColor(Color.parseColor(color));
	}
	
	public void setWhiteLight() {
		setLight("white");
	}
	
	public void setBlueLight() {
		setLight("blue");
	}
	
	private void setLight(String color) {
		builder.setLights(Color.parseColor(color), 1000, 5000);
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
	
	public void setMaxPriority() {
		builder.setPriority(NotificationCompat.PRIORITY_MAX);
	}
	
	public void setHighPriority() {
		builder.setPriority(NotificationCompat.PRIORITY_HIGH);
	}
	
	public void setLowPriority() {
		builder.setPriority(NotificationCompat.PRIORITY_LOW);
	}
	
	public void setMinPriority() {
		builder.setPriority(NotificationCompat.PRIORITY_MIN);
	}
	
	public void setCategory(String category) {
		builder.setCategory(category);
	}
	
	public void setPublicVisibility() {
		builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
	}
	
	public void setPrivateVisibility() {
		builder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
	}
	
	public void setSecretVisibility() {
		builder.setVisibility(NotificationCompat.VISIBILITY_SECRET);
	}
	
	public void addPerson(String uri) {
		builder.addPerson(uri);
	}
	
	public void setDefaultVibration() {
		long[] defaultPattern = { 1000, 1000 };
		builder.setVibrate(defaultPattern);
	}
	
	private Bitmap createLargeIconBitmap(String largeIconUrl) {
		Bitmap largeIconBitmap = null;
		if (StringUtils.isNotEmpty(largeIconUrl)) {
			
			largeIconBitmap = ImageLoaderUtils.loadBitmap(largeIconUrl, ImageScaleType.EXACTLY,
				NotificationUtils.getNotificationLargeIconWidthPx(),
				NotificationUtils.getNotificationLargeIconHeightPx(), null);
		}
		return largeIconBitmap;
	}
	
	public void setNotificationName(String notificationName) {
		this.notificationName = notificationName;
	}
	
	public void setBigTextStyle(String title, String text) {
		if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(text)) {
			NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
			bigTextStyle.setBigContentTitle(title);
			bigTextStyle.bigText(text);
			builder.setStyle(bigTextStyle);
		}
	}
	
	public void addStartActivityAction(int icon, int titleId, Intent intent) {
		addStartActivityAction(icon, LocalizationUtils.getString(titleId), intent);
	}
	
	public void addStartActivityAction(int icon, String title, Intent intent) {
		PendingIntent pendingIntent = PendingIntent.getActivity(AbstractApplication.get(),
			IdGenerator.getRandomIntId(), intent, 0);
		builder.addAction(icon, title, pendingIntent);
	}
	
	public void addStartServiceAction(int icon, int titleId, Intent intent) {
		addStartServiceAction(icon, LocalizationUtils.getString(titleId), intent);
	}
	
	public void addStartServiceAction(int icon, String title, Intent intent) {
		PendingIntent pendingIntent = PendingIntent.getService(AbstractApplication.get(), IdGenerator.getRandomIntId(),
			intent, 0);
		builder.addAction(icon, title, pendingIntent);
	}
	
}
