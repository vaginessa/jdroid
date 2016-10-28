package com.jdroid.android.sample.ui.service;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.service.ServiceCommand;
import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.utils.IdGenerator;

public class SampleServiceCommand2 extends ServiceCommand {

	@Override
	protected int execute(Bundle bundle) {
		return GcmNetworkManager.RESULT_RESCHEDULE;
	}

	@Override
	protected int executeRetry(Bundle bundle) {
		Boolean fail = bundle.getBoolean("fail");
		if (fail) {
			throw new ConnectionException("Failing service");
		} else {
			NotificationBuilder builder = new NotificationBuilder("myNotification");
			builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());
			builder.setTicker("Sample Ticker");
			builder.setContentTitle(getClass().getSimpleName());
			builder.setContentText(bundle.get("a").toString());

			NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
			return GcmNetworkManager.RESULT_SUCCESS;
		}
	}
}
