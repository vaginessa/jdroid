package com.jdroid.android.service;

import android.os.Bundle;
import android.support.annotation.WorkerThread;

import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.Serializable;

public abstract class ServiceCommand implements Serializable {

	private final static Logger LOGGER = LoggerUtils.getLogger(ServiceCommand.class);

	public void start() {
		start(null);
	}

	public final void start(Bundle bundle) {
		CommandWorkerService.runService(bundle, this, requiresInstantExecution());
	}

	protected Task.Builder createRetryTaskBuilder() {
		OneoffTask.Builder builder = new OneoffTask.Builder();
		builder.setPersisted(true);
		builder.setExecutionWindow(0, 5);
		builder.setTag(getClass().getSimpleName());
		return builder;
	}

	protected Boolean requiresInstantExecution() {
		return true;
	}

	@WorkerThread
	protected abstract int execute(Bundle bundle);

	@WorkerThread
	protected int executeRetry(Bundle bundle) {
		return execute(bundle);
	}
}
