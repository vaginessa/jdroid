package com.jdroid.android.debug.facebook.stetho;

import android.content.Context;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;

import org.slf4j.Logger;

public class StethoAppModule extends AbstractAppModule {

	private static final Logger LOGGER = LoggerUtils.getLogger(StethoAppModule.class);

	private static final String STETHO = "com.facebook.stetho.Stetho";
	private static final String INITIALIZE_WITH_DEFAULTS = "initializeWithDefaults";

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			ReflectionUtils.invokeStaticMethod(STETHO, INITIALIZE_WITH_DEFAULTS,
					Lists.<Class<?>>newArrayList(Context.class), Lists.<Object>newArrayList(AbstractApplication.get()));
			LOGGER.info("Stetho initialized");
		} catch (Exception e) {
			LOGGER.error("Error creating StethoAppModule", e);
		}
	}
}
