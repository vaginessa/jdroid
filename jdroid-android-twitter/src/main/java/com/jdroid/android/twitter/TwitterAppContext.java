package com.jdroid.android.twitter;

import static com.jdroid.android.context.BuildConfigUtils.getBuildConfigValue;

public class TwitterAppContext {

	public static String getTwitterOauthConsumerKey() {
		return getBuildConfigValue("TWITTER_OAUTH_CONSUMER_KEY");
	}

	public static String getTwitterOauthConsumerSecret() {
		return getBuildConfigValue("TWITTER_OAUTH_CONSUMER_SECRET");
	}

}