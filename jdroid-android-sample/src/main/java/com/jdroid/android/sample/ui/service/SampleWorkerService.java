package com.jdroid.android.sample.ui.service;

import android.content.Intent;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.R;
import com.jdroid.android.service.WorkerService;
import com.jdroid.java.utils.IdGenerator;

public class SampleWorkerService extends WorkerService {

	@Override
	protected void doExecute(Intent intent) {

		NotificationBuilder builder = new NotificationBuilder();
		builder.setNotificationName("myNotification");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setTicker(R.string.notificationTicker);
		builder.setContentTitle(getClass().getSimpleName());
		builder.setContentText(intent.getExtras().get("a").toString());

		NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
	}

	public static void runIntentInService(Intent intent) {
		WorkerService.runIntentInService(AbstractApplication.get(), intent, SampleWorkerService.class, false);
	}
}
