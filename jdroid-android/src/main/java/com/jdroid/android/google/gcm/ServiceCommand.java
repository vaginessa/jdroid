package com.jdroid.android.google.gcm;

import android.os.Bundle;
import android.support.annotation.WorkerThread;

import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;

import java.io.Serializable;

public abstract class ServiceCommand implements Serializable {

	private Boolean isInstantExecutionRequired = true;
	
	public void start() {
		start(null);
	}

	public final void start(Bundle bundle) {
		CommandWorkerService.runService(bundle, this, isInstantExecutionRequired);
	}

	protected Task.Builder createRetryTaskBuilder() {
		OneoffTask.Builder builder = new OneoffTask.Builder();
		builder.setPersisted(true);
		builder.setExecutionWindow(0, 5);
		builder.setTag(getClass().getSimpleName());
		return builder;
	}

	@WorkerThread
	protected abstract int execute(Bundle bundle);

	@WorkerThread
	protected int executeRetry(Bundle bundle) {
		return execute(bundle);
	}
	
	public void setInstantExecutionRequired(Boolean isInstantExecutionRequired) {
		this.isInstantExecutionRequired = isInstantExecutionRequired;
	}
}
