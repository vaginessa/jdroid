package com.jdroid.android.utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.intent.IntentUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.java.utils.ValidationUtils;

import java.util.List;

public class AndroidUtils {
	
	public static Boolean isEmulator() {
		return "google_sdk".equals(Build.PRODUCT);
	}
	
	public static String getAndroidId() {
		return Secure.getString(AbstractApplication.get().getContentResolver(), Secure.ANDROID_ID);
	}
	
	/**
	 * @return The version name of the application
	 */
	public static String getVersionName() {
		return getPackageInfo().versionName;
	}
	
	/**
	 * @return The version code of the application
	 */
	public static Integer getVersionCode() {
		return getPackageInfo().versionCode;
	}
	
	/**
	 * @return The application id of the application
	 */
	public static String getApplicationId() {
		return getPackageInfo().packageName;
	}
	
	/**
	 * @return The name of the application
	 */
	public static String getApplicationName() {
		Context context = AbstractApplication.get();
		ApplicationInfo applicationInfo = AndroidUtils.getApplicationInfo();
		return context.getPackageManager().getApplicationLabel(applicationInfo).toString();
	}
	
	public static PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			Context context = AbstractApplication.get();
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// Do Nothing
		}
		return info;
	}
	
	public static ApplicationInfo getApplicationInfo() {
		ApplicationInfo info = null;
		try {
			Context context = AbstractApplication.get();
			info = context.getPackageManager().getApplicationInfo(context.getPackageName(),
				PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// Do Nothing
		}
		return info;
	}
	
	public static void showSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}
	
	public static void hideSoftInput(View view) {
		((InputMethodManager)AbstractApplication.get().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
			view.getWindowToken(), 0);
	}
	
	public static void scrollToBottom(final ScrollView scroll) {
		if (scroll != null) {
			scroll.post(new Runnable() {
				
				@Override
				public void run() {
					scroll.fullScroll(View.FOCUS_DOWN);
				}
			});
		}
	}
	
	public static String getNetworkOperatorName() {
		TelephonyManager manager = (TelephonyManager)AbstractApplication.get().getSystemService(
			Context.TELEPHONY_SERVICE);
		return manager.getNetworkOperatorName();
	}
	
	public static String getSimOperatorName() {
		TelephonyManager manager = (TelephonyManager)AbstractApplication.get().getSystemService(
			Context.TELEPHONY_SERVICE);
		return manager.getSimOperatorName();
	}
	
	/**
	 * @return The HEAP size in MegaBytes
	 */
	public static Integer getHeapSize() {
		return ((ActivityManager)AbstractApplication.get().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
	}
	
	/**
	 * @return The available storage in MegaBytes
	 */
	@SuppressWarnings("deprecation")
	public static Long getAvailableInternalDataSize() {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		long size = (long)stat.getAvailableBlocks() * (long)stat.getBlockSize();
		return size / FileUtils.BYTES_TO_MB;
	}
	
	/**
	 * @return The total storage in MegaBytes
	 */
	@SuppressWarnings("deprecation")
	public static Long getTotalInternalDataSize() {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		long size = (long)stat.getBlockCount() * (long)stat.getBlockSize();
		return size / FileUtils.BYTES_TO_MB;
	}
	
	/**
	 * Checks if the application is installed on the SD card.
	 * 
	 * @return <code>true</code> if the application is installed on the sd card
	 */
	public static Boolean isInstalledOnSdCard() {
		return (getPackageInfo().applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;
	}
	
	public static Boolean isMediaMounted() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}
	
	public static String getDeviceManufacturer() {
		return android.os.Build.MANUFACTURER;
	}
	
	public static Integer getApiLevel() {
		return android.os.Build.VERSION.SDK_INT;
	}
	
	public static Boolean isPreKitkat() {
		return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
	}

	public static Boolean isPreLollipop() {
		return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
	}

	public static String getPlatformVersion() {
		return android.os.Build.VERSION.RELEASE;
	}
	
	public static Boolean hasCamera() {
		return IntentUtils.isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE);
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

	@RequiresPermission(Manifest.permission.GET_ACCOUNTS)
	public static List<String> getAccountsEmails() {
		List<String> emails = Lists.newArrayList();
		for (Account account : AccountManager.get(AbstractApplication.get()).getAccounts()) {
			if (ValidationUtils.isValidEmail(account.name) && !emails.contains(account.name)) {
				emails.add(account.name);
			}
		}
		return emails;
	}

	@RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
	public static String getMacAddress() {
		WifiManager wifiManager = (WifiManager)AbstractApplication.get().getSystemService(Context.WIFI_SERVICE);
		return wifiManager.getConnectionInfo().getMacAddress();
	}
	
	public static String getDeviceUUID() {
		String uuid = AndroidUtils.getAndroidId();
		// Use the Android ID unless it's broken, in which case fallback on deviceId, unless it's not available, then
		// fallback on a random number
		if (StringUtils.isBlank(uuid) || "9774d56d682e549c".equals(uuid)) {
			TelephonyManager telephonyManager = ((TelephonyManager)AbstractApplication.get().getSystemService(
				Context.TELEPHONY_SERVICE));
			String deviceId = telephonyManager != null ? telephonyManager.getDeviceId() : null;
			uuid = StringUtils.isNotBlank(deviceId) ? deviceId : AbstractApplication.get().getInstallationId();
		}
		return uuid;
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
	
}
