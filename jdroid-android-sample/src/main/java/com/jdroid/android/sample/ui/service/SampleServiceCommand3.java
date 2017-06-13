package com.jdroid.android.sample.ui.service;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.gcm.ServiceCommand;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.application.AndroidNotificationChannelType;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.utils.IdGenerator;

public class SampleServiceCommand3 extends ServiceCommand {

	@Override
	protected int execute(Bundle bundle) {
		Boolean fail = bundle.getBoolean("fail");
		if (fail) {
			throw new ConnectionException("Failing service");
		} else {
			NotificationBuilder builder = new NotificationBuilder("myNotification", AndroidNotificationChannelType.DEFAULT_IMPORTANCE);
			builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());
			builder.setTicker("Sample Ticker");
			builder.setContentTitle(getClass().getSimpleName());
			builder.setContentText(bundle.get("a").toString());

			NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
			return GcmNetworkManager.RESULT_SUCCESS;
		}
	}

	@Override
	protected int executeRetry(Bundle bundle) {
		NotificationBuilder builder = new NotificationBuilder("myNotification", AndroidNotificationChannelType.DEFAULT_IMPORTANCE);
		builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());
		builder.setTicker("Sample Ticker");
		builder.setContentTitle(getClass().getSimpleName());
		builder.setContentText(bundle.get("a").toString());

		NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
		return GcmNetworkManager.RESULT_SUCCESS;
	}
}
