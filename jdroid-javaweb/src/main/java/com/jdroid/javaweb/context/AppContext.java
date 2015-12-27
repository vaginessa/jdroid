package com.jdroid.javaweb.context;

import com.jdroid.java.date.DateUtils;

/**
 * The {@link AppContext}
 */
public class AppContext {
	
	private String appName;
	private String appVersion;
	private String appHomePath;

	private String buildType;

	private String buildTime;
	private String apiVersion;
	private String minApiVersion;
	
	private String googleServerApiKey;
	private String adminToken;
	
	private Boolean httpMockEnabled;
	private Integer httpMockSleepDuration;
	
	private String twitterOAuthConsumerKey;
	private String twitterOAuthConsumerSecret;
	private String twitterOAuthAccessToken;
	private String twitterOAuthAccessTokenSecret;
	private Boolean twitterEnabled;

	private String rollBarAccessToken;
	private Boolean rollBarEnabled;

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
	
	/**
	 * @return the apiVersion
	 */
	public String getApiVersion() {
		return apiVersion;
	}
	
	/**
	 * @param apiVersion the apiVersion to set
	 */
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	
	/**
	 * @return the minApiVersion
	 */
	public String getMinApiVersion() {
		return minApiVersion;
	}
	
	/**
	 * @param minApiVersion the minApiVersion to set
	 */
	public void setMinApiVersion(String minApiVersion) {
		this.minApiVersion = minApiVersion;
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
	
	/**
	 * @return the twitterOAuthConsumerKey
	 */
	public String getTwitterOAuthConsumerKey() {
		return twitterOAuthConsumerKey;
	}
	
	/**
	 * @param twitterOAuthConsumerKey the twitterOAuthConsumerKey to set
	 */
	public void setTwitterOAuthConsumerKey(String twitterOAuthConsumerKey) {
		this.twitterOAuthConsumerKey = twitterOAuthConsumerKey;
	}
	
	/**
	 * @return the twitterOAuthConsumerSecret
	 */
	public String getTwitterOAuthConsumerSecret() {
		return twitterOAuthConsumerSecret;
	}
	
	/**
	 * @param twitterOAuthConsumerSecret the twitterOAuthConsumerSecret to set
	 */
	public void setTwitterOAuthConsumerSecret(String twitterOAuthConsumerSecret) {
		this.twitterOAuthConsumerSecret = twitterOAuthConsumerSecret;
	}
	
	/**
	 * @return the twitterOAuthAccessToken
	 */
	public String getTwitterOAuthAccessToken() {
		return twitterOAuthAccessToken;
	}
	
	/**
	 * @param twitterOAuthAccessToken the twitterOAuthAccessToken to set
	 */
	public void setTwitterOAuthAccessToken(String twitterOAuthAccessToken) {
		this.twitterOAuthAccessToken = twitterOAuthAccessToken;
	}
	
	/**
	 * @return the twitterOAuthAccessTokenSecret
	 */
	public String getTwitterOAuthAccessTokenSecret() {
		return twitterOAuthAccessTokenSecret;
	}
	
	/**
	 * @param twitterOAuthAccessTokenSecret the twitterOAuthAccessTokenSecret to set
	 */
	public void setTwitterOAuthAccessTokenSecret(String twitterOAuthAccessTokenSecret) {
		this.twitterOAuthAccessTokenSecret = twitterOAuthAccessTokenSecret;
	}
	
	/**
	 * @return the twitterEnabled
	 */
	public Boolean isTwitterEnabled() {
		return twitterEnabled;
	}
	
	/**
	 * @param twitterEnabled the twitterEnabled to set
	 */
	public void setTwitterEnabled(Boolean twitterEnabled) {
		this.twitterEnabled = twitterEnabled;
	}

	public String getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(String buildTime) {
		this.buildTime = buildTime;
	}

	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}

	public String getBuildType() {
		return buildType;
	}

	public String getRollBarAccessToken() {
		return rollBarAccessToken;
	}

	public void setRollBarAccessToken(String rollBarAccessToken) {
		this.rollBarAccessToken = rollBarAccessToken;
	}

	public Boolean isRollBarEnabled() {
		return rollBarEnabled;
	}

	public void setRollBarEnabled(Boolean rollBarEnabled) {
		this.rollBarEnabled = rollBarEnabled;
	}

	public Long getDeviceUpdateRequiredDuration() {
		return DateUtils.MILLIS_PER_DAY * 7;
	}
}
