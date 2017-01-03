package com.jdroid.android.twitter;

import com.jdroid.android.context.AbstractAppContext;

public class TwitterAppContext extends AbstractAppContext {

	public String getTwitterOauthConsumerKey() {
		return getBuildConfigValue("TWITTER_OAUTH_CONSUMER_KEY");
	}

	public String getTwitterOauthConsumerSecret() {
		return getBuildConfigValue("TWITTER_OAUTH_CONSUMER_SECRET");
	}

}