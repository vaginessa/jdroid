package com.jdroid.javaweb.context;


/**
 * The {@link DefaultApplicationContext}
 */
public class DefaultApplicationContext {
	
	private String appName;
	private String appVersion;
	
	private String googleServerApiKey;
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
}
