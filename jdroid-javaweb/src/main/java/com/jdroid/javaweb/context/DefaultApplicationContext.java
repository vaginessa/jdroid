package com.jdroid.javaweb.context;

/**
 * The {@link DefaultApplicationContext}
 */
public class DefaultApplicationContext {
	
	private String appName;
	private String appVersion;
	private String appHomePath;
	
	private String googleServerApiKey;
	private String adminToken;
	
	private Boolean httpMockEnabled;
	private Integer httpMockSleepDuration;
	
	public String getAppName() {
		return appName;
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	/**
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return appVersion;
	}
	
	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	public void setHttpMockSleepDuration(Integer httpMockSleepDuration) {
		this.httpMockSleepDuration = httpMockSleepDuration;
	}
	
	/**
	 * @param httpMockEnabled the httpMockEnabled to set
	 */
	public void setHttpMockEnabled(Boolean httpMockEnabled) {
		this.httpMockEnabled = httpMockEnabled;
	}
	
	public Boolean isHttpMockEnabled() {
		return httpMockEnabled;
	}
	
	public Integer getHttpMockSleepDuration() {
		return httpMockSleepDuration;
	}
	
	public String getGoogleServerApiKey() {
		return googleServerApiKey;
	}
	
	public void setGoogleServerApiKey(String googleServerApiKey) {
		this.googleServerApiKey = googleServerApiKey;
	}
	
	/**
	 * @return the adminToken
	 */
	public String getAdminToken() {
		return adminToken;
	}
	
	/**
	 * @param adminToken the adminToken to set
	 */
	public void setAdminToken(String adminToken) {
		this.adminToken = adminToken;
	}
	
	/**
	 * @return the appHomePath
	 */
	public String getAppHomePath() {
		return appHomePath;
	}
	
	/**
	 * @param appHomePath the appHomePath to set
	 */
	public void setAppHomePath(String appHomePath) {
		this.appHomePath = appHomePath;
	}
}
