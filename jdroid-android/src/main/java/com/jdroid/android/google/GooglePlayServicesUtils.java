package com.jdroid.android.google;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class GooglePlayServicesUtils {

	private static final Logger LOGGER = LoggerUtils.getLogger(GooglePlayServicesUtils.class);

	private static final String GOOGLE_PLAY_SERVICES = "com.google.android.gms";

	private static String advertisingId;

	public static boolean isGooglePlayServicesAvailable(Context c) {
		return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(c) == ConnectionResult.SUCCESS;
	}

	public static void launchGooglePlayServicesUpdate(Activity c) {
		GooglePlayUtils.launchAppDetails(c, GOOGLE_PLAY_SERVICES);
	}

	public static String getAdvertisingId(Context context) {
		if (advertisingId == null) {
			try {
				advertisingId = AdvertisingIdClient.getAdvertisingIdInfo(context).getId();
			} catch (Exception e) {
				LOGGER.warn("Error getting the advertising id " + e.getMessage());
			}
		}
		return advertisingId;
	}
}
