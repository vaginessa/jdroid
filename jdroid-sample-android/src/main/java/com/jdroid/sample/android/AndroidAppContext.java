package com.jdroid.sample.android;


import com.jdroid.android.context.AppContext;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.utils.StringUtils;

import java.util.Set;

public class AndroidAppContext extends AppContext {

	@Override
	protected String getServerName() {
		return BuildConfig.SERVER_NAME;
	}

	@Override
	public String getBuildType() {
		return BuildConfig.BUILD_TYPE;
	}

	@Override
	public Boolean isGoogleAnalyticsEnabled() {
		return BuildConfig.GOOGLE_ANALYTICS_ENABLED;
	}

	@Override
	public String getGoogleAnalyticsTrackingId() {
		return BuildConfig.GOOGLE_ANALYTICS_TRACKING_ID;
	}

	@Override
	public Boolean isGoogleAnalyticsDebugEnabled() {
		return BuildConfig.GOOGLE_ANALYTICS_DEBUG_ENABLED;
	}

	@Override
	public String getLocalIp() {
		return BuildConfig.LOCAL_IP;
	}

	@Override
	public Boolean isCrashlyticsEnabled() {
		return BuildConfig.CRASHLYTICS_ENABLED;
	}

	@Override
	public Boolean isCrashlyticsDebugEnabled() {
		return BuildConfig.CRASHLYTICS_DEBUG_ENABLED;
	}

	@Override
	public String getInstallationSource() {
		return BuildConfig.INSTALLATION_SOURCE;
	}

	@Override
	public Boolean areAdsEnabledByDefault() {
		return BuildConfig.ADS_ENABLED;
	}

	@Override
	public String getAdUnitId() {
		return BuildConfig.AD_UNIT_ID;
	}

	@Override
	public Set<String> getTestDevicesIds() {
		return Sets.newHashSet(StringUtils.splitToCollection(BuildConfig.ADS_TEST_DEVICES_IDS));
	}

	@Override
	public String getContactUsEmail() {
		return "jdroidsoft@gmail.com";
	}
}
