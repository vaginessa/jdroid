package com.jdroid.android.glide;

import android.support.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jdroid.android.application.AbstractApplication;

public class LoggingRequestListener<R> implements RequestListener<R> {
	
	@Override
	public boolean onLoadFailed(@Nullable GlideException glideException, Object model, Target target, boolean isFirstResource) {
		if (glideException != null) {
			for (Throwable throwable : glideException.getRootCauses()) {
				AbstractApplication.get().getExceptionHandler().logHandledException(throwable);
			}
		}
		return false;
	}
	
	@Override
	public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
		return false;
	}
}
