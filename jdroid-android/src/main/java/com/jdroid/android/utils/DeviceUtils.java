package com.jdroid.android.utils;

import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;

import com.facebook.device.yearclass.YearClass;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.intent.IntentUtils;
import com.jdroid.java.utils.StringUtils;

public class DeviceUtils {

	private static final String DEVICE_YEAR_CLASS = "DeviceYearClass";

	private static Integer deviceYearClass = YearClass.CLASS_UNKNOWN;

	public static Integer getDeviceYearClass() {
		if (deviceYearClass != null) {
			deviceYearClass = SharedPreferencesHelper.get().loadPreferenceAsInteger(DEVICE_YEAR_CLASS, YearClass.CLASS_UNKNOWN);
			//Try again if device was previously unknown.
			if (deviceYearClass == YearClass.CLASS_UNKNOWN) {
				deviceYearClass = YearClass.get(AbstractApplication.get());
				SharedPreferencesHelper.get().savePreferenceAsync(DEVICE_YEAR_CLASS, deviceYearClass);
			}
		}
		return deviceYearClass;
	}

	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}

	public static String getDeviceManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	public static String getDeviceName() {
		String manufacturer = getDeviceManufacturer();
		String model = getDeviceModel();
		if ((model != null) && model.startsWith(manufacturer)) {
			return StringUtils.capitalize(model);
		} else if (manufacturer != null) {
			return StringUtils.capitalize(manufacturer) + " " + model;
		} else {
			return "Unknown";
		}
	}

	public static Boolean hasCamera() {
		return IntentUtils.isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE);
	}

	public static String getDeviceType() {
		if (ScreenUtils.is10Inches()) {
			return "10\" tablet";
		} else if (ScreenUtils.isBetween7And10Inches()) {
			return "7\" tablet";
		} else {
			return "phone";
		}
	}

	public static Boolean isEmulator() {
		return "google_sdk".equals(Build.PRODUCT);
	}

	public static String getAndroidId() {
		return Settings.Secure.getString(AbstractApplication.get().getContentResolver(), Settings.Secure.ANDROID_ID);
	}
}
