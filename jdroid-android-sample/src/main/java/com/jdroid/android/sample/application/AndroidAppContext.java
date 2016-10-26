package com.jdroid.android.sample.application;


import com.jdroid.android.context.AppContext;
import com.jdroid.android.firebase.remoteconfig.RemoteConfigParameter;
import com.jdroid.android.sample.BuildConfig;
import com.jdroid.android.sample.firebase.remoteconfig.AndroidRemoteConfigParameter;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.Server;
import com.jdroid.android.sample.api.ApiServer;

import java.util.List;

public class AndroidAppContext extends AppContext {

	public static final String SAMPLE_BANNER_AD_UNIT_ID = "ca-app-pub-4654922738884963/2999432948";
	public static final String SAMPLE_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-4654922738884963/4476166148";
	public static final String SAMPLE_SMALL_NATIVE_AD_EXPRESS_AD_UNIT_ID = "ca-app-pub-4654922738884963/4852674542";
	public static final String SAMPLE_MEDIUM_NATIVE_AD_EXPRESS_AD_UNIT_ID = "ca-app-pub-4654922738884963/9649474141";
	public static final String SAMPLE_LARGE_NATIVE_AD_EXPRESS_AD_UNIT_ID = "ca-app-pub-4654922738884963/2545009741";

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

	@Override
	public List<RemoteConfigParameter> getRemoteConfigParameters() {
		return Lists.<RemoteConfigParameter>newArrayList(AndroidRemoteConfigParameter.values());
	}
}
