package com.jdroid.android.context;

import com.jdroid.android.utils.SharedPreferencesHelper;

public class UsageStats {

	private static final String USAGE_STATS = "usageStats";
	private static final String APP_LOADS = "appLoads";
	private static final String FIRST_APP_LOAD_TIMESTAMP = "firstAppLoadTimestamp";
	private static final String LAST_CRASH_TIMESTAMP = "lastCrashTimestamp";

	private static SharedPreferencesHelper sharedPreferencesHelper;

	private static Long appLoads;
	private static Long firstAppLoadTimestamp;
	private static Long lastCrashTimestamp;
	private static Long lastStopTime = System.currentTimeMillis();

	public static void incrementAppLoad() {
		loadAppLoad(true);
		loadFirstAppLoadTimestamp(true);
	}

	public static Long getAppLoads() {
		loadAppLoad(false);
		return appLoads;
	}

	private static synchronized void loadAppLoad(Boolean increment) {
		if (appLoads == null) {
			appLoads = getSharedPreferencesHelper().loadPreferenceAsLong(APP_LOADS, 0L);
		}

		if (increment) {
			appLoads++;
			getSharedPreferencesHelper().savePreferenceAsync(APP_LOADS, appLoads);
		}
	}

	public static Long getLastStopTime() {
		return lastStopTime;
	}

	public static void setLastStopTime() {
		lastStopTime = System.currentTimeMillis();
	}

	public static void setLastCrashTimestamp() {
		lastCrashTimestamp = System.currentTimeMillis();
		getSharedPreferencesHelper().savePreferenceAsync(LAST_CRASH_TIMESTAMP, lastCrashTimestamp);
	}

	public static Long getLastCrashTimestamp() {
		loadLastCrashTimestamp();
		return lastCrashTimestamp;
	}

	public static synchronized void loadLastCrashTimestamp() {
		if (lastCrashTimestamp == null) {
			lastCrashTimestamp = getSharedPreferencesHelper().loadPreferenceAsLong(LAST_CRASH_TIMESTAMP, 0L);
		}
	}

	public static Long getFirstAppLoadTimestamp() {
		loadFirstAppLoadTimestamp(false);
		return firstAppLoadTimestamp;
	}

	public static synchronized void loadFirstAppLoadTimestamp(Boolean init) {
		if (firstAppLoadTimestamp == null) {
			firstAppLoadTimestamp = getSharedPreferencesHelper().loadPreferenceAsLong(FIRST_APP_LOAD_TIMESTAMP);
			if (init && firstAppLoadTimestamp == null) {
				firstAppLoadTimestamp = System.currentTimeMillis();
				getSharedPreferencesHelper().savePreferenceAsync(FIRST_APP_LOAD_TIMESTAMP, firstAppLoadTimestamp);
			}
		}
	}

	public static void reset() {
		getSharedPreferencesHelper().removeAllPreferences();
	}

	public static void simulateHeavyUsage() {
		reset();

		appLoads = 100L;
		getSharedPreferencesHelper().savePreferenceAsync(APP_LOADS, appLoads);

		firstAppLoadTimestamp = 0L;
		getSharedPreferencesHelper().savePreferenceAsync(FIRST_APP_LOAD_TIMESTAMP, firstAppLoadTimestamp);

		lastCrashTimestamp = 0L;
		getSharedPreferencesHelper().savePreferenceAsync(LAST_CRASH_TIMESTAMP, lastCrashTimestamp);
	}

	private static SharedPreferencesHelper getSharedPreferencesHelper() {
		if (sharedPreferencesHelper == null) {
			sharedPreferencesHelper = SharedPreferencesHelper.get(USAGE_STATS);
		}
		return sharedPreferencesHelper;
	}
}
