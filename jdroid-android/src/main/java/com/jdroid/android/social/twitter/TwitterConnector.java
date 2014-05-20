package com.jdroid.android.social.twitter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.utils.GooglePlayUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class TwitterConnector {
	
	public static void openProfile(String account) {
		try {
			Intent intent = new Intent();
			intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity");
			intent.putExtra("screen_name", account);
			AbstractApplication.get().getCurrentActivity().startActivity(intent);
		} catch (ActivityNotFoundException e) {
			GooglePlayUtils.showDownloadDialog(R.string.twitter, "com.twitter.android");
		}
	}
}
