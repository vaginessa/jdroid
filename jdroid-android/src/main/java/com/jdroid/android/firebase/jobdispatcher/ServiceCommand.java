package com.jdroid.android.firebase.jobdispatcher;

import android.os.Bundle;
import android.support.annotation.WorkerThread;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.io.Serializable;

public abstract class ServiceCommand implements Serializable {

	private Boolean isInstantExecutionRequired = true;
	
	public void start() {
		start(null);
	}

	public final void start(Bundle bundle) {
		CommandWorkerService.runService(bundle, this, isInstantExecutionRequired);
	}

	protected Job.Builder createRetryJobBuilder(FirebaseJobDispatcher dispatcher) {
		Job.Builder builder = dispatcher.newJobBuilder();
		builder.setRecurring(false); // one-off job
		builder.setLifetime(Lifetime.FOREVER);
		builder.setTag(getClass().getSimpleName());
		builder.setTrigger(Trigger.executionWindow(0, 5)); // start between 0 and 5 seconds from now
		builder.setReplaceCurrent(false); // don't overwrite an existing job with the same tag
		builder.setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL); // retry with exponential backoff
		return builder;
	}

	@WorkerThread
	protected abstract boolean execute(Bundle bundle);

	@WorkerThread
	protected boolean executeRetry(Bundle bundle) {
		return execute(bundle);
	}
	
	public void setInstantExecutionRequired(Boolean isInstantExecutionRequired) {
		this.isInstantExecutionRequired = isInstantExecutionRequired;
	}
}
