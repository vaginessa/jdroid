package com.jdroid.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;

import com.google.android.gms.appindexing.AndroidAppUri;

public class ReferrerUtils {

	private static final String EXTRA_REFERRER = "android.intent.extra.REFERRER";
	private static final String REFERRER_NAME = "android.intent.extra.REFERRER_NAME";

	public static String getReferrerCategory(Activity activity) {
		String referrerCategory = null;
		Uri referrerUri = getReferrer(activity);
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

	/** Returns the referrer who started the Activity. */
	public static Uri getReferrer(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
			return activity.getReferrer();
		}
		return getReferrerCompatible(activity);
	}

	/** Returns the referrer on devices running SDK versions lower than 22. */
	private static Uri getReferrerCompatible(Activity activity) {
		Intent intent = activity.getIntent();
		Uri referrerUri = intent.getParcelableExtra(EXTRA_REFERRER);
		if (referrerUri != null) {
			return referrerUri;
		}
		String referrer = intent.getStringExtra(REFERRER_NAME);
		if (referrer != null) {
			// Try parsing the referrer URL; if it's invalid, return null
			try {
				return Uri.parse(referrer);
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}

	public static void setReferrer(Intent intent, String referrer) {
		intent.putExtra(EXTRA_REFERRER, Uri.parse(referrer));
	}
}
