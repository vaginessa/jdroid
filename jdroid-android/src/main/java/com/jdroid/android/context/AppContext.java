package com.jdroid.android.context;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.debug.ExceptionType;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.http.Server;
import com.jdroid.java.utils.PropertiesUtils;

import java.util.Locale;
import java.util.Set;

public abstract class AppContext {
	
	public static final String USER_DATA_MOCKED = "userDataMocked";
	public static final String ADS_ENABLED = "adsEnabled";
	private static final String FIRST_SESSION_TIMESTAMP = "firstSessionTimestamp";
	public static final String HTTP_MOCK_ENABLED = "httpMockEnabled";
	public static final String HTTP_MOCK_SLEEP = "httpMockSleep";
	public static final String HTTP_MOCK_CRASH_TYPE = "httpMockCrashType";
	
	// Environment
	private Server server;
	private String serverApiVersion;
	
	// Ads
	private Boolean adsEnabled;

	public AppContext() {

		server = findServerByName(getServerName());
		serverApiVersion = PropertiesUtils.getStringProperty("server.api.version");

		adsEnabled = areAdsEnabledByDefault();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get());
		if (!sharedPreferences.contains(ADS_ENABLED)) {
			Editor editor = sharedPreferences.edit();
			editor.putBoolean(ADS_ENABLED, adsEnabled);
			editor.commit();
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
			Class<?> clazz = defaultServer.getClass().getDeclaringClass() != null ? defaultServer.getClass().getDeclaringClass()
					: defaultServer.getClass();
			return (T)defaultServer.instance(PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
				clazz.getSimpleName(), defaultServer.getName()).toUpperCase(Locale.US));
		}
	}

	protected abstract String getServerName();
	
	/**
	 * @return The Google project ID acquired from the API console
	 */
	public String getGoogleProjectId() {
		return null;
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

	public abstract String getBuildType();

	/**
	 * @return Whether the application is running on a production environment
	 */
	public Boolean isProductionEnvironment() {
		return getBuildType().equals("release");
	}

	public Boolean areAdsEnabledByDefault() {
		return false;
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
		return Sets.newHashSet();
	}
	
	/**
	 * @return The AdMob Publisher ID
	 */
	public String getAdUnitId() {
		return null;
	}
	
	/**
	 * @return Whether the application has Google Analytics enabled or not
	 */
	public abstract Boolean isGoogleAnalyticsEnabled();
	
	/**
	 * @return The Google Analytics Tracking ID
	 */
	public abstract String getGoogleAnalyticsTrackingId();
	
	public abstract Boolean isGoogleAnalyticsDebugEnabled();
	
	public Boolean isHttpMockEnabled() {
		return !isProductionEnvironment()
				&& PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(
					HTTP_MOCK_ENABLED, false);
	}
	
	public Integer getHttpMockSleepDuration() {
		return PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(HTTP_MOCK_SLEEP,
			false) ? 10 : null;
	}
	
	public ExceptionType getHttpMockExceptionType() {
		return ExceptionType.find(PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
			HTTP_MOCK_CRASH_TYPE, null));
	}
	
	public Boolean isUserDataMocked() {
		return PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(USER_DATA_MOCKED,
			false);
	}
	
	public abstract String getLocalIp();

	public String getInstallationSource() {
		return "GooglePlay";
	}
	
	public abstract Boolean isCrashlyticsEnabled();
	
	public abstract Boolean isCrashlyticsDebugEnabled();
	
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
		return serverApiVersion;
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
