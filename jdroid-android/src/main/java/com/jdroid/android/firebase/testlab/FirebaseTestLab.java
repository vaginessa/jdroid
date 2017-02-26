package com.jdroid.android.firebase.testlab;

import android.provider.Settings;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class FirebaseTestLab {

	private final static Logger LOGGER = LoggerUtils.getLogger(FirebaseTestLab.class);

	private static Boolean isRunningInstrumentedTests;

	public static Boolean isRunningInstrumentedTests() {
		if (isRunningInstrumentedTests == null) {
			try {
				String testLabSetting = Settings.System.getString(AbstractApplication.get().getContentResolver(), "firebase.test.lab");
				isRunningInstrumentedTests = "true".equals(testLabSetting);
			} catch (Exception e) {
				isRunningInstrumentedTests = false;
				LOGGER.warn("Error when getting firebase.test.lab system property", e);
			} finally {
				LOGGER.debug("Running Firebase Tests Labs: " + isRunningInstrumentedTests);
			}
		}
		return isRunningInstrumentedTests;
	}

}
