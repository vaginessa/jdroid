package com.jdroid.android.sample.ui.service;

import android.os.Bundle;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.utils.IdGenerator;

public class SampleServiceCommand2 extends ServiceCommand {

	@Override
	protected boolean execute(Bundle bundle) {
		return true;
	}

	@Override
	protected boolean executeRetry(Bundle bundle) {
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
			return false;
		}
	}
}
