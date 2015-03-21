package com.jdroid.sample.android;


import com.jdroid.android.context.AppContext;

public class AndroidAppContext extends AppContext {

	public static final String SAMPLE_BANNER_AD_UNIT_ID = "ca-app-pub-4654922738884963/2999432948";
	public static final String SAMPLE_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-4654922738884963/4476166148";

	@Override
	public String getContactUsEmail() {
		return "jdroidsoft@gmail.com";
	}
}
