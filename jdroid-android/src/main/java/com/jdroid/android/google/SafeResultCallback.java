package com.jdroid.android.google;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.jdroid.android.application.AbstractApplication;

public abstract class SafeResultCallback<R extends Result> implements ResultCallback<R> {

	@Override
	public final void onResult(@NonNull R result) {
		try {
			onSafeResult(result);
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
	}

	public abstract void onSafeResult(@NonNull R result);
}
