package com.jdroid.android.sample.application;


import com.jdroid.android.context.AppContext;
import com.jdroid.android.sample.BuildConfig;
import com.jdroid.java.http.Server;
import com.jdroid.android.sample.api.ApiServer;

public class AndroidAppContext extends AppContext {

	public static final String SAMPLE_BANNER_AD_UNIT_ID = "ca-app-pub-4654922738884963/2999432948";
	public static final String SAMPLE_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-4654922738884963/4476166148";
	public static final String SAMPLE_NATIVE_AD_EXPRESS_AD_UNIT_ID = "ca-app-pub-4654922738884963/4852674542";

	@Override
	protected Server findServerByName(String name) {
		return ApiServer.valueOf(name);
	}

	@Override
	public String getServerApiVersion() {
		return "1.0";
	}

	@Override
	public String getWebsite() {
		return "http://www.jdroidframework.com";
	}

	@Override
	public String getContactUsEmail() {
		return "jdroidsoft@gmail.com";
	}

	@Override
	public String getTwitterAccount() {
		return "jdroidframework";
	}

	public String getFirebaseAuthToken() {
		return BuildConfig.FIREBASE_AUTH_TOKEN;
	}
}
