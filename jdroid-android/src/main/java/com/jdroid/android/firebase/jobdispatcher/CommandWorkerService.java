package com.jdroid.android.firebase.jobdispatcher;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.service.WorkerService;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;

import org.slf4j.Logger;

public class CommandWorkerService extends WorkerService {

	private final static Logger LOGGER = LoggerUtils.getLogger(CommandWorkerService.class);

	final static String COMMAND_EXTRA = "com.jdroid.android.firebase.jobdispatcher.CommandWorkerService.command";

	protected static void runService(Bundle bundle, ServiceCommand serviceCommand, Boolean requiresInstantExecution) {
		if (requiresInstantExecution && (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || !AbstractApplication.get().isInBackground())) {
			startWorkerService(bundle, serviceCommand);
		} else {
			startJobService(bundle, serviceCommand);
		}
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
			boolean retry;
			try {
				retry = serviceCommand.execute(intent.getExtras());
				LOGGER.info(serviceCommand.getClass().getSimpleName() + " executed. Retry: " + retry);
			} catch (ConnectionException e) {
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
				retry = true;
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
				retry = false;
			}
			if (retry) {
				startJobService(intent.getExtras(), serviceCommand);
			}
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("Service command not found on " + getClass().getSimpleName());
		}
	}

	private static void startWorkerService(Bundle bundle, ServiceCommand serviceCommand) {
		LOGGER.info("Starting Worker Service for " + serviceCommand.getClass().getSimpleName());
		Intent intent = new Intent();
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		intent.putExtra(CommandWorkerService.COMMAND_EXTRA, serviceCommand.getClass().getName());
		WorkerService.runIntentInService(AbstractApplication.get(), intent, CommandWorkerService.class);
	}

	private static void startJobService(Bundle bundle, ServiceCommand serviceCommand) {
		LOGGER.info("Scheduling Job Service for " + serviceCommand.getClass().getSimpleName());

		if (bundle == null) {
			bundle = new Bundle();
		}
		bundle.putSerializable(CommandWorkerService.COMMAND_EXTRA, serviceCommand.getClass().getName());
		
		FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(AbstractApplication.get()));
		Job.Builder builder = serviceCommand.createRetryJobBuilder(dispatcher);
		builder.setExtras(bundle);
		builder.setService(CommandJobService.class);
		dispatcher.mustSchedule(builder.build());
	}
}
