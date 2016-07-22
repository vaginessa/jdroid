package com.jdroid.android.sample.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.home.HomeActivity;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;

public class NotificationsFragment extends AbstractFragment {

	private EditText urlEditText;
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.notifications_fragment;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findView(R.id.createNotification).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				NotificationBuilder builder = new NotificationBuilder("myNotification");
				builder.setSmallIcon(AbstractApplication.get().getLauncherIconResId());
				builder.setTicker(R.string.notificationTicker);
				builder.setContentTitle(R.string.notificationTitle);
				builder.setContentText(R.string.notificationDescription);
				builder.setWhen(DateUtils.nowMillis());
				builder.setBlueLight();
				builder.setDefaultSound();

				Intent intent = new Intent();
				intent.setClass(getActivity(), HomeActivity.class);
				builder.setContentIntent(intent);
				
				NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
			}
		});

		urlEditText = findView(R.id.url);
		urlEditText.setText("http://jdroidframework.com/images");

		findView(R.id.createNotificationFromBundle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Bundle bundle = new Bundle();
				bundle.putString(NotificationBuilder.TICKER, getString(R.string.notificationTicker));
				bundle.putString(NotificationBuilder.CONTENT_TITLE, getString(R.string.notificationTitle));
				bundle.putString(NotificationBuilder.CONTENT_TEXT, getString(R.string.notificationDescription));
				bundle.putString(NotificationBuilder.URL, urlEditText.getText().toString());

				NotificationBuilder builder = new NotificationBuilder("notificationFromBundle", bundle);
				builder.setSmallIcon(AbstractApplication.get().getLauncherIconResId());
				builder.setWhen(DateUtils.nowMillis());
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
