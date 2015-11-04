package com.jdroid.android.rating;

import android.content.Context;

import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;

public class RatingHelper {

	private static final String RATE_ME_CLICK_TIMESTAMP = "rateMeClickTimestamp";

	public static void rateMe(Context context) {
		SharedPreferencesHelper.get().savePreferenceAsync(RATE_ME_CLICK_TIMESTAMP, System.currentTimeMillis());
		GooglePlayUtils.launchAppDetails(context);
	}

	public static Boolean isRateMeClicked() {
		return SharedPreferencesHelper.get().hasPreference(RATE_ME_CLICK_TIMESTAMP);
	}
}
