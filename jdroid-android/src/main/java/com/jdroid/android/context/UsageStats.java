package com.jdroid.android.context;

import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class UsageStats {

	private static final Logger LOGGER = LoggerUtils.getLogger(UsageStats.class);

	private final static String USAGE_STATS = "usageStats";
	private final static String APP_LOADS = "appLoads";

	private static Long appLoads;
	private static Long lastStopTime = System.currentTimeMillis();

	public static void incrementAppLoad() {
		loadAppLoad();
		appLoads++;
		SharedPreferencesHelper.get(USAGE_STATS).savePreferenceAsync(APP_LOADS, appLoads);
	}

	public static Long getAppLoads() {
		loadAppLoad();
		return appLoads;
	}

	private static synchronized void loadAppLoad() {
		if (appLoads == null) {
			appLoads = SharedPreferencesHelper.get(USAGE_STATS).loadPreferenceAsLong(APP_LOADS, 0L);
		}
	}

	public static Long getLastStopTime() {
		return lastStopTime;
	}

	public static void setLastStopTime() {
		lastStopTime = System.currentTimeMillis();
	}
}
