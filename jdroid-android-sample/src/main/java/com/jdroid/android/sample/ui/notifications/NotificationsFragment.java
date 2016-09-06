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
import com.jdroid.java.collections.Maps;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;

import java.util.Map;

public class NotificationsFragment extends AbstractFragment {

	private EditText notificationName;
	private EditText ticker;
	private EditText contentTitle;
	private EditText contentText;
	private EditText urlEditText;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.notifications_fragment;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		notificationName = findView(R.id.notificationName);
		notificationName.setText("myNotification");

		ticker = findView(R.id.ticker);
		ticker.setText("Ticker example");

		contentTitle = findView(R.id.contentTitle);
		contentTitle.setText("Title example");

		contentText = findView(R.id.contentText);
		contentText.setText("Description example");

		findView(R.id.sendNotification).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				NotificationBuilder builder = new NotificationBuilder(notificationName.getText().toString());
				builder.setSmallIcon(AbstractApplication.get().getLauncherIconResId());
				builder.setTicker(ticker.getText().toString());
				builder.setContentTitle(contentTitle.getText().toString());
				builder.setContentText(contentText.getText().toString());
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
		urlEditText.setText("http://jdroidframework.com/uri");

		findView(R.id.sendNotificationFromBundle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Map<String, String> data = Maps.newHashMap();
				data.put(NotificationBuilder.TICKER, ticker.getText().toString());
				data.put(NotificationBuilder.CONTENT_TITLE, contentTitle.getText().toString());
				data.put(NotificationBuilder.CONTENT_TEXT, urlEditText.getText().toString());
				data.put(NotificationBuilder.URL, urlEditText.getText().toString());

				NotificationBuilder builder = new NotificationBuilder("notificationFromBundle", data);
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
