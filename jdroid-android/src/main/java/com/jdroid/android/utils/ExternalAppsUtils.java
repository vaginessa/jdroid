package com.jdroid.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.jdroid.android.AbstractApplication;

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
	
	/**
	 * Launch packageName app or open Google Play to download.
	 * 
	 * @param context
	 * @param packageName
	 * @return true if app is installed, false otherwise.
	 */
	public static boolean launchOrDownloadApp(Context context, String packageName) {
		boolean isAppInstalled = isAppInstalled(context, packageName);
		if (isAppInstalled) {
			launchExternalApp(context, packageName);
		} else {
			GooglePlayUtils.launchAppDetails(context, packageName);
		}
		return isAppInstalled;
	}
	
	public static void launchExternalApp(Context context, String packageName) {
		Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(launchIntent);
	}
	
	public static void startSkypeCall(String username) {
		Intent skypeIntent = new Intent(Intent.ACTION_VIEW);
		skypeIntent.setData(Uri.parse("skype:" + username + "?call"));
		skypeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		AbstractApplication.get().startActivity(skypeIntent);
	}
	
	public static void openUrl(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		AbstractApplication.get().startActivity(intent);
	}
}
