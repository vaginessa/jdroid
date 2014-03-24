package com.jdroid.android.utils;

import java.util.List;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.java.utils.ValidationUtils;

/**
 * 
 * @author Maxi Rosson
 */
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
	 * @return The package name of the application
	 */
	public static String getPackageName() {
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
	 * Gets the {@link WindowManager} from the context.
	 * 
	 * @return {@link WindowManager} The window manager.
	 */
	public static WindowManager getWindowManager() {
		return (WindowManager)AbstractApplication.get().getSystemService(Context.WINDOW_SERVICE);
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
		long size = stat.getAvailableBlocks() * stat.getBlockSize();
		return size / FileUtils.BYTES_TO_MB;
	}
	
	/**
	 * @return The total storage in MegaBytes
	 */
	@SuppressWarnings("deprecation")
	public static Long getTotalInternalDataSize() {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		long size = stat.getBlockCount() * stat.getBlockSize();
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
	
	public static String getPlatformVersion() {
		return android.os.Build.VERSION.RELEASE;
	}
	
	public static Boolean isGoogleTV() {
		return AbstractApplication.get().getPackageManager().hasSystemFeature("com.google.android.tv");
	}
	
	public static Boolean hasCamera() {
		return IntentUtils.isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE);
	}
	
	public static Boolean hasGallery() {
		return !isGoogleTV();
	}
	
	public static Integer getSmallestScreenWidthDp() {
		Configuration config = AbstractApplication.get().getResources().getConfiguration();
		return config.smallestScreenWidthDp;
	}
	
	public static Boolean is10InchesOrBigger() {
		return AndroidUtils.getSmallestScreenWidthDp() >= 720;
	}
	
	public static Boolean is7InchesOrBigger() {
		return AndroidUtils.getSmallestScreenWidthDp() >= 600;
	}
	
	public static Boolean isBetween7And10Inches() {
		Integer smallestScreenWidthDp = AndroidUtils.getSmallestScreenWidthDp();
		return (smallestScreenWidthDp >= 600) && (smallestScreenWidthDp < 720);
	}
	
	public static String getDeviceType() {
		if (AndroidUtils.is10InchesOrBigger()) {
			return "10\" tablet";
		} else if (AndroidUtils.isBetween7And10Inches()) {
			return "7\" tablet";
		} else {
			return "phone";
		}
	}
	
	public static Boolean isLdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_LOW;
	}
	
	public static Boolean isMdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM;
	}
	
	public static Boolean isHdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_HIGH;
	}
	
	public static Boolean isXhdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH;
	}
	
	public static Boolean isTVdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_TV;
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static Boolean isXXhdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_XXHIGH;
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static Boolean isXXXhdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_XXXHIGH;
	}
	
	public static String getScreenDensity() {
		String density = StringUtils.EMPTY;
		if (AndroidUtils.isLdpiDensity()) {
			density = "ldpi";
		} else if (AndroidUtils.isMdpiDensity()) {
			density = "mdpi";
		} else if (AndroidUtils.isHdpiDensity()) {
			density = "hdpi";
		} else if (AndroidUtils.isXhdpiDensity()) {
			density = "xhdpi";
		} else if (AndroidUtils.isTVdpiDensity()) {
			density = "tvdpi";
		} else if (AndroidUtils.isXXhdpiDensity()) {
			density = "xxhdpi";
		} else if (AndroidUtils.isXXXhdpiDensity()) {
			density = "xxxhdpi";
		}
		return density;
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
	
	public static void startSkypeCall(String username) {
		Intent skypeIntent = new Intent("android.intent.action.VIEW");
		skypeIntent.setData(Uri.parse("skype:" + username));
		AbstractApplication.get().startActivity(skypeIntent);
	}
	
	public static List<String> getAccountsEmails() {
		List<String> emails = Lists.newArrayList();
		for (Account account : AccountManager.get(AbstractApplication.get()).getAccounts()) {
			if (ValidationUtils.isValidEmail(account.name)) {
				if (!emails.contains(account.name)) {
					emails.add(account.name);
				}
			}
		}
		return emails;
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
	
	/**
	 * This method converts dp unit to equivalent pixels, depending on device density.
	 * 
	 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return An int value to represent px equivalent to dp depending on device density
	 */
	public static int convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return (int)(dp * (metrics.densityDpi / 160f));
	}
	
	public static int convertDimenToPixel(int dimenResId) {
		return (int)AbstractApplication.get().getResources().getDimension(dimenResId);
	}
}
