package com.jdroid.android.context;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.http.Server;

import java.util.Locale;

public abstract class AppContext extends AbstractAppContext {
	
	// Environment
	private Server defaultServer;

	protected Server findServerByName(String name) {
		return null;
	}
	
	public Server getServer() {
		if (defaultServer == null) {
			defaultServer = findServerByName(getServerName());
		}
		return getServer(defaultServer);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Server> T getServer(Server defaultServer) {
		if (isProductionEnvironment() || !displayDebugSettings()) {
			return (T)defaultServer;
		} else {
			Class<?> clazz = defaultServer.getClass().getEnclosingClass() != null ? defaultServer.getClass().getEnclosingClass()
					: defaultServer.getClass();
			return (T)defaultServer.instance(SharedPreferencesHelper.get().loadPreference(
					clazz.getSimpleName(), defaultServer.getName()).toUpperCase(Locale.US));
		}
	}

	protected String getServerName() {
		return getBuildConfigValue("SERVER_NAME", null);
	}
	
	/**
	 * @return Whether the application should display the debug settings
	 */
	public Boolean displayDebugSettings() {
		return !isProductionEnvironment();
	}

	public String getBuildType() {
		return getBuildConfigValue("BUILD_TYPE");
	}

	public String getBuildTime() {
		return getBuildConfigValue("BUILD_TIME", null);
	}

	public Boolean isStrictModeEnabled() {
		return !isProductionEnvironment() && getBuildConfigBoolean("STRICT_MODE_ENABLED", false);
	}

	/**
	 * @return Whether the application is running on a production environment
	 */
	public Boolean isProductionEnvironment() {
		return getBuildType().equals("release");
	}

	public String getLocalIp() {
		return getBuildConfigValue("LOCAL_IP", null);
	}

	public String getInstallationSource() {
		return getBuildConfigValue("INSTALLATION_SOURCE", "GooglePlay");
	}

	public Boolean isChromeInstallationSource() {
		return getInstallationSource().equals("Chrome");
	}

	/*
	 * Used by Google Sign In
	 */
	public String getServerClientId() {
		return getBuildConfigValue("GOOGLE_SERVER_CLIENT_ID", null);
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

	public String getAppInviteTitle() {
		return LocalizationUtils.getString(R.string.jdroid_appInviteTitle, LocalizationUtils.getString(R.string.jdroid_appName));
	}

	public String getAppInviteMessage() {
		return LocalizationUtils.getString(R.string.jdroid_appInviteMessage, LocalizationUtils.getString(R.string.jdroid_appName));
	}

	public String getAppInviteDeeplink() {
		return AbstractApplication.get().getAppContext().getWebsite();
	}
}
