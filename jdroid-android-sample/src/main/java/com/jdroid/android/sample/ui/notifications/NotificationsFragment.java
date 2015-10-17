package com.jdroid.android.sample.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.home.HomeActivity;
import com.jdroid.java.utils.IdGenerator;

public class NotificationsFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.notifications_fragment;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findView(R.id.createNotification).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				NotificationBuilder builder = new NotificationBuilder();
				builder.setNotificationName("myNotification");
				builder.setSmallIcon(R.drawable.ic_launcher);
				builder.setTicker(R.string.notificationTicker);
				builder.setContentTitle(R.string.notificationTitle);
				builder.setContentText(R.string.notificationDescription);
				builder.setWhen(System.currentTimeMillis());
				builder.setBlueLight();
				builder.setDefaultSound();

				Intent intent = new Intent();
				intent.setClass(getActivity(), HomeActivity.class);
				builder.setContentIntent(intent);
				
				NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
			}
		});
		findView(R.id.cancelNotifications).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NotificationUtils.cancelAllNotifications();
			}
		});
	}
}
