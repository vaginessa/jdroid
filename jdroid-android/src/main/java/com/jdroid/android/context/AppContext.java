package com.jdroid.android.context;

import android.preference.PreferenceManager;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.http.Server;

import java.util.Locale;

public abstract class AppContext {
	
	public static final String HTTP_MOCK_ENABLED = "httpMockEnabled";
	public static final String HTTP_MOCK_SLEEP = "httpMockSleep";

	// Environment
	private Server server;

	public AppContext() {

		server = findServerByName(getServerName());
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
