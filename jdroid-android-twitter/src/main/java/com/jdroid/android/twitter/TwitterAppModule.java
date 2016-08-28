package com.jdroid.android.twitter;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.TweetUi;

import java.util.List;

import io.fabric.sdk.android.Kit;

public class TwitterAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = TwitterAppModule.class.getName();

	public static TwitterAppModule get() {
		return (TwitterAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private String twitterOauthConsumerKey;
	private String twitterOauthConsumerSecret;

	public TwitterAppModule(String twitterOauthConsumerKey, String twitterOauthConsumerSecret) {
		this.twitterOauthConsumerKey = twitterOauthConsumerKey;
		this.twitterOauthConsumerSecret = twitterOauthConsumerSecret;
	}

	@Override
	public List<Kit> getFabricKits() {
		TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterOauthConsumerKey, twitterOauthConsumerSecret);
		return Lists.<Kit>newArrayList(new TwitterCore(authConfig), new TweetUi());
	}

}