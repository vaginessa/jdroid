package com.jdroid.android.sample.ui.service;

import android.content.Intent;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.TaskParams;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.service.ServiceCommand;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.IdGenerator;

public class SampleServiceCommand1 extends ServiceCommand {

	@Override
	protected int execute(Intent intent) {
		return GcmNetworkManager.RESULT_RESCHEDULE;
	}

	@Override
	protected int executeRetry(TaskParams taskParams) {
		Boolean fail = taskParams.getExtras().getBoolean("fail");
		if (fail) {
			throw new UnexpectedException("Failing service");
		} else {
			NotificationBuilder builder = new NotificationBuilder("myNotification");
			builder.setSmallIcon(AbstractApplication.get().getLauncherIconResId());
			builder.setTicker("Sample Ticker");
			builder.setContentTitle(getClass().getSimpleName());
			builder.setContentText(taskParams.getExtras().get("a").toString());

			NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
			return GcmNetworkManager.RESULT_SUCCESS;
		}
	}
}
