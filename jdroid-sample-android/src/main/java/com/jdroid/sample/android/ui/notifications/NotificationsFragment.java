package com.jdroid.sample.android.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.utils.NotificationBuilder;
import com.jdroid.android.utils.NotificationUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.sample.android.R;

public class NotificationsFragment extends AbstractFragment {
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.notifications_fragment, container, false);
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
				builder.setSmallIcon(R.drawable.ic_launcher);
				builder.setTicker(R.string.notificationTicker);
				builder.setContentTitle(R.string.notificationTitle);
				builder.setContentText(R.string.notificationDescription);
				builder.setWhen(System.currentTimeMillis());
				builder.setBlueLight();
				builder.setDefaultSound();
				
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
