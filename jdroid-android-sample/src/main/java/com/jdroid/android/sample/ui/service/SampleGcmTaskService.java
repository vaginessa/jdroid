package com.jdroid.android.sample.ui.service;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.TaskParams;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.sample.R;
import com.jdroid.android.service.AbstractGcmTaskService;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.IdGenerator;

public class SampleGcmTaskService extends AbstractGcmTaskService {

	@Override
	public int doRunTask(TaskParams taskParams) {

		Boolean fail = taskParams.getExtras().getBoolean("fail");
		if (fail) {
			throw new UnexpectedException("Failing service");
		} else {
			NotificationBuilder builder = new NotificationBuilder("myNotification");
			builder.setSmallIcon(AbstractApplication.get().getLauncherIconResId());
			builder.setTicker(R.string.notificationTicker);
			builder.setContentTitle(getClass().getSimpleName());
			builder.setContentText(taskParams.getExtras().get("a").toString());

			NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
			return GcmNetworkManager.RESULT_SUCCESS;
		}
	}

	public static void runIntentInService(Bundle bundle) {
		OneoffTask.Builder builder = new OneoffTask.Builder();
		builder.setPersisted(true);
		builder.setExecutionWindow(0, 5);
		builder.setExtras(bundle);
		builder.setTag(SampleGcmTaskService.class.getSimpleName());
		builder.setService(SampleGcmTaskService.class);
		GcmNetworkManager.getInstance(AbstractApplication.get()).schedule(builder.build());
	}
}
