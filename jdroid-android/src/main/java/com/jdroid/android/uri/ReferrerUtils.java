package com.jdroid.android.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.appindexing.AndroidAppUri;

public class ReferrerUtils {

	private static final String EXTRA_REFERRER = "android.intent.extra.REFERRER";

	public static String getReferrerCategory(Activity activity) {
		String referrerCategory = null;
		Uri referrerUri = ActivityCompat.getReferrer(activity);
		if (referrerUri != null) {
			if (referrerUri.getScheme().equals("http") || referrerUri.getScheme().equals("https")) {
				referrerCategory = "http://";
				// App was opened from a browser
				String host = referrerUri.getHost();
				if (host.equals("www.google.com")) {
					referrerCategory += "google.com";
				} else {
					referrerCategory += host;
				}

			} else if (referrerUri.getScheme().equals("android-app")) {
				referrerCategory = "android-app://";
				// App was opened from another app
				// Google Search App: com.google.android.googlequicksearchbox
				// Google Crawler App: com.google.appcrawler
				AndroidAppUri appUri = AndroidAppUri.newAndroidAppUri(referrerUri);
				referrerCategory += appUri.getPackageName();
			} else {
				referrerCategory = referrerUri.toString();
			}
		}
		return referrerCategory;
	}

	public static void setReferrer(Intent intent, Uri referrer) {
		intent.putExtra(EXTRA_REFERRER, referrer);
	}
	public static void setReferrer(Intent intent, String referrer) {
		if (referrer != null) {
			setReferrer(intent, Uri.parse(referrer));
		}
	}
}
