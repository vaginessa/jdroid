package com.jdroid.android.context;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.http.Server;
import com.jdroid.java.utils.StringUtils;

import java.util.Locale;
import java.util.Set;

public abstract class AppContext {
	
	public static final String ADS_ENABLED = "adsEnabled";
	private static final String FIRST_SESSION_TIMESTAMP = "firstSessionTimestamp";
	public static final String HTTP_MOCK_ENABLED = "httpMockEnabled";
	public static final String HTTP_MOCK_SLEEP = "httpMockSleep";

	// Environment
	private Server server;

	// Ads
	private Boolean adsEnabled;

	public AppContext() {

		server = findServerByName(getServerName());

		adsEnabled = areAdsEnabledByDefault();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get());
		if (!sharedPreferences.contains(ADS_ENABLED)) {
			Editor editor = sharedPreferences.edit();
			editor.putBoolean(ADS_ENABLED, adsEnabled);
			editor.apply();
		}
	}
	
	protected Server findServerByName(String name) {
		return null;
	}
	
	public Server getServer() {
		return getServer(server);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Server> T getServer(Server defaultServer) {
		if (isProductionEnvironment() || !displayDebugSettings()) {
			return (T)defaultServer;
		} else {
			Class<?> clazz = defaultServer.getClass().getEnclosingClass() != null ? defaultServer.getClass().getEnclosingClass()
					: defaultServer.getClass();
			return (T)defaultServer.instance(PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
				clazz.getSimpleName(), defaultServer.getName()).toUpperCase(Locale.US));
		}
	}

	protected String getServerName() {
		return AbstractApplication.get().getBuildConfigValue("SERVER_NAME", null);
	}
	
	/**
	 * @return The registered Facebook app ID that is used to identify this application for Facebook.
	 */
	public String getFacebookAppId() {
		return null;
	}
	
	/**
	 * @return Whether the application should display the debug settings
	 */
	public Boolean displayDebugSettings() {
		return !isProductionEnvironment();
	}

	public Boolean isLoggingEnabled() {
		return !isProductionEnvironment();
	}

	public String getBuildType() {
		return AbstractApplication.get().getBuildConfigValue("BUILD_TYPE");
	}

	public String getBuildTime() {
		return AbstractApplication.get().getBuildConfigValue("BUILD_TIME");
	}

	/**
	 * @return Whether the application is running on a production environment
	 */
	public Boolean isProductionEnvironment() {
		return getBuildType().equals("release");
	}

	public Boolean areAdsEnabledByDefault() {
		return AbstractApplication.get().getBuildConfigValue("ADS_ENABLED", false);
	}
	
	/**
	 * @return Whether the application has ads enabled or not
	 */
	public Boolean areAdsEnabled() {
		return PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(ADS_ENABLED,
				adsEnabled);
	}
	
	/**
	 * @return The MD5-hashed ID of the devices that should display mocked ads
	 */
	public Set<String> getTestDevicesIds() {
		String testDevicesIds = AbstractApplication.get().getBuildConfigValue("ADS_TEST_DEVICES_IDS", null);
		return testDevicesIds != null ? Sets.newHashSet(StringUtils.splitToCollectionWithCommaSeparator(testDevicesIds)) : Sets.<String>newHashSet();
	}
	
	/**
	 * @return The AdMob Publisher ID
	 */
	public String getDefaultAdUnitId() {
		return AbstractApplication.get().getBuildConfigValue("AD_UNIT_ID", null);
	}
	
	/**
	 * @return Whether the application has Google Analytics enabled or not
	 */
	public Boolean isGoogleAnalyticsEnabled() {
		return AbstractApplication.get().getBuildConfigValue("GOOGLE_ANALYTICS_ENABLED", false);
	}
	
	/**
	 * @return The Google Analytics Tracking ID
	 */
	public String getGoogleAnalyticsTrackingId() {
		return AbstractApplication.get().getBuildConfigValue("GOOGLE_ANALYTICS_TRACKING_ID", null);
	}
	
	public  Boolean isGoogleAnalyticsDebugEnabled() {
		return AbstractApplication.get().getBuildConfigValue("GOOGLE_ANALYTICS_DEBUG_ENABLED", false);
	}
	
	public Boolean isHttpMockEnabled() {
		return !isProductionEnvironment()
				&& PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(
					HTTP_MOCK_ENABLED, false);
	}
	
	public Integer getHttpMockSleepDuration() {
		return PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(HTTP_MOCK_SLEEP,
			false) ? 10 : null;
	}
	
	public String getLocalIp() {
		return AbstractApplication.get().getBuildConfigValue("LOCAL_IP", null);
	}


	public String getInstallationSource() {
		return AbstractApplication.get().getBuildConfigValue("INSTALLATION_SOURCE", "GooglePlay");
	}

	public Boolean isChromeInstallationSource() {
		return getInstallationSource().equals("Chrome");
	}
	
	public void saveFirstSessionTimestamp() {
		Long firstSessionTimestamp = getFirstSessionTimestamp();
		if (firstSessionTimestamp == null) {
			SharedPreferencesHelper.get().savePreference(FIRST_SESSION_TIMESTAMP, System.currentTimeMillis());
		}
	}
	
	public Long getFirstSessionTimestamp() {
		return SharedPreferencesHelper.get().loadPreferenceAsLong(FIRST_SESSION_TIMESTAMP);
	}
	
	public Long getDaysSinceFirstSession() {
		Long firstSessionTimestamp = getFirstSessionTimestamp();
		if (firstSessionTimestamp != null) {
			return (System.currentTimeMillis() - getFirstSessionTimestamp()) / DateUtils.DAY_IN_MILLIS;
		} else {
			return 0L;
		}
	}
	
	public String getServerApiVersion() {
		return null;
	}
	
	public String getWebsite() {
		return null;
	}

	public String getContactUsEmail() {
		return null;
	}

	public String getTwitterAccount() {
		return null;
	}
	
	public String getFacebookPageId() {
		return null;
	}
	
	public String getGooglePlusCommunityId() {
		return null;
	}
}
