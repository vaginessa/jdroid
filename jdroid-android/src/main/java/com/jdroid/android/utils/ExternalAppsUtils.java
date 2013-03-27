package com.jdroid.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class ExternalAppsUtils {
	
	public static boolean isAppInstalled(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}
	
	public static void launchOrDownloadApp(Context context, String packageName) {
		if (isAppInstalled(context, packageName)) {
			launchExternalApp(context, packageName);
		} else {
			GooglePlayUtils.launchAppDetails(context, packageName);
		}
	}
	
	public static void launchExternalApp(Context context, String packageName) {
		Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(launchIntent);
	}
}
