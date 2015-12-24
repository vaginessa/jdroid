package com.jdroid.android.sample.ui.service;

import android.content.Intent;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.TaskParams;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.R;
import com.jdroid.android.service.ServiceCommand;
import com.jdroid.java.utils.IdGenerator;

public class SampleServiceCommand1 extends ServiceCommand {

	@Override
	protected int execute(Intent intent) {
		return GcmNetworkManager.RESULT_RESCHEDULE;
	}

	@Override
	protected int executeRetry(TaskParams taskParams) {
		NotificationBuilder builder = new NotificationBuilder();
		builder.setNotificationName("myNotification");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setTicker(R.string.notificationTicker);
		builder.setContentTitle(getClass().getSimpleName());
		builder.setContentText(taskParams.getExtras().get("a").toString());

		NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
		return GcmNetworkManager.RESULT_SUCCESS;
	}
}
