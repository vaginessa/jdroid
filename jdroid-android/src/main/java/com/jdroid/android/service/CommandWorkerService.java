package com.jdroid.android.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.Task;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;

import org.slf4j.Logger;

public class CommandWorkerService extends WorkerService {

	private final static Logger LOGGER = LoggerUtils.getLogger(CommandWorkerService.class);

	final static String COMMAND_EXTRA = "com.jdroid.android.service.CommandWorkerService.command";

	protected static void runService(Context context, Bundle bundle, ServiceCommand serviceCommand) {
		LOGGER.info("Scheduling Worker Service for " + serviceCommand.getClass().getSimpleName());
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.putExtra(CommandWorkerService.COMMAND_EXTRA, serviceCommand.getClass().getName());
		CommandWorkerService.runIntentInService(context, intent, CommandWorkerService.class);
	}

	@Override
	protected String getTrackingLabel(Intent intent) {
		String serviceCommandExtra = intent.getStringExtra(COMMAND_EXTRA);
		return serviceCommandExtra == null ? getTrackingVariable(intent) : serviceCommandExtra.substring(serviceCommandExtra.lastIndexOf(".") + 1);
	}

	@Override
	protected void doExecute(Intent intent) {
		String serviceCommandExtra = intent.getStringExtra(COMMAND_EXTRA);
		if (serviceCommandExtra != null) {
			ServiceCommand serviceCommand = ReflectionUtils.newInstance(serviceCommandExtra);
			int result = serviceCommand.execute(intent.getExtras());
			LOGGER.info(serviceCommand.getClass().getSimpleName() + " executed with result " + result);
			if (result == GcmNetworkManager.RESULT_RESCHEDULE) {
				startGcmTaskService(intent.getExtras(), serviceCommand);
			}
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("Service command not found on " + getClass().getSimpleName());
		}
	}

	private void startGcmTaskService(Bundle bundle, ServiceCommand serviceCommand) {
		LOGGER.info("Scheduling GCM Task Service for " + serviceCommand.getClass().getSimpleName());

		Task.Builder builder = serviceCommand.createRetryTaskBuilder();
		builder.setExtras(bundle);
		builder.setService(CommandGcmTaskService.class);
		GcmNetworkManager.getInstance(AbstractApplication.get()).schedule(builder.build());
	}
}
