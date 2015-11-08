package com.jdroid.android.google;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class GooglePlayServicesUtils {

	private static final Logger LOGGER = LoggerUtils.getLogger(GooglePlayServicesUtils.class);

	private static final String GOOGLE_PLAY_SERVICES = "com.google.android.gms";

	private static String advertisingId;

	public static GooglePlayServicesResponse verifyGooglePlayServices(Activity activity) {
		Integer resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		if (resultCode == ConnectionResult.SUCCESS) {
			return new GooglePlayServicesResponse();
		} else {
			LOGGER.info("Google Play Services result code: " +  resultCode);
			Dialog dialog = null;
			if (resultCode != ConnectionResult.SERVICE_MISSING) {
				dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, activity, 0);
				if (dialog != null) {
					//This dialog will help the user update to the latest GooglePlayServices
					dialog.show();
				}
			}
			return new GooglePlayServicesResponse(dialog, resultCode);
		}
	}

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

	public static class GooglePlayServicesResponse {

		private Boolean isAvailable;
		private Dialog dialog;
		private Integer resultCode;

		public GooglePlayServicesResponse() {
			isAvailable = true;
		}

		public GooglePlayServicesResponse(Dialog dialog, Integer resultCode) {
			isAvailable = false;
			this.dialog = dialog;
			this.resultCode = resultCode;
		}

		public Boolean isAvailable() {
			return isAvailable;
		}

		public Dialog getDialog() {
			return dialog;
		}

		public Integer getResultCode() {
			return resultCode;
		}
	}
}
