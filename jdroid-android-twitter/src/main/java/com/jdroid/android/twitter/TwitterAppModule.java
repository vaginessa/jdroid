package com.jdroid.android.twitter;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.TweetUi;

import org.slf4j.Logger;

import java.util.List;

import io.fabric.sdk.android.Kit;

public class TwitterAppModule extends AbstractAppModule {

	private final static Logger LOGGER = LoggerUtils.getLogger(TwitterAppModule.class);

	public static final String MODULE_NAME = TwitterAppModule.class.getName();

	public static TwitterAppModule get() {
		return (TwitterAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private TwitterAppContext twitterAppContext;

	public TwitterAppModule() {
		twitterAppContext = createTwitterAppContext();
	}

	protected TwitterAppContext createTwitterAppContext() {
		return new TwitterAppContext();
	}

	public TwitterAppContext getTwitterAppContext() {
		return twitterAppContext;
	}

	@Override
	public List<Kit> createFabricKits() {
		String twitterOauthConsumerKey = twitterAppContext.getTwitterOauthConsumerKey();
		String twitterOauthConsumerSecret = twitterAppContext.getTwitterOauthConsumerSecret();
		if (twitterOauthConsumerKey == null || twitterOauthConsumerSecret == null) {
			LOGGER.error("Missing TWITTER_OAUTH_CONSUMER_KEY or TWITTER_OAUTH_CONSUMER_SECRET");
			return Lists.newArrayList();
		} else {
			TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterOauthConsumerKey, twitterOauthConsumerSecret);
			return Lists.<Kit>newArrayList(new TwitterCore(authConfig), new TweetUi());
		}
	}

}