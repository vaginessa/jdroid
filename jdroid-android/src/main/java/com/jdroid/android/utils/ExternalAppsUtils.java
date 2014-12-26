package com.jdroid.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.jdroid.android.AbstractApplication;

public class ExternalAppsUtils {
	
	public static final String TWITTER_PACKAGE_NAME = "com.twitter.android";
	public static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
	public static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
	public static final String HANGOUTS_PACKAGE_NAME = "com.google.android.talk";
	
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
	
	public static Drawable getAppIcon(String packageName) {
		try {
			return AbstractApplication.get().getPackageManager().getApplicationIcon(packageName);
		} catch (NameNotFoundException e) {
			return null;
		}
	}
}
