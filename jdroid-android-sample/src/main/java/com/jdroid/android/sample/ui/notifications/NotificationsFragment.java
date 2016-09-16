package com.jdroid.android.sample.ui.notifications;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.R;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;

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

		urlEditText = findView(R.id.url);
		urlEditText.setText("http://jdroidframework.com/uri");

		findView(R.id.sendNotification).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				NotificationBuilder builder = new NotificationBuilder(notificationName.getText().toString());
				builder.setSmallIcon(AbstractApplication.get().getLauncherIconResId());
				builder.setTicker(ticker.getText().toString());
				builder.setContentTitle(contentTitle.getText().toString());
				builder.setContentText(contentText.getText().toString());
				builder.setUrl(urlEditText.getText().toString());
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
