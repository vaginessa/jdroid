package com.jdroid.android.twitter;

import android.content.Context;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.fabric.FabricAppLifecycleCallback;
import com.jdroid.java.utils.LoggerUtils;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.TweetUi;

import org.slf4j.Logger;

public class TwitterAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(TwitterAppLifecycleCallback.class);
	
	@Override
	public void onCreate(Context context) {
		String twitterOauthConsumerKey = TwitterAppContext.getTwitterOauthConsumerKey();
		String twitterOauthConsumerSecret = TwitterAppContext.getTwitterOauthConsumerSecret();
		if (twitterOauthConsumerKey == null || twitterOauthConsumerSecret == null) {
			LOGGER.error("Missing TWITTER_OAUTH_CONSUMER_KEY or TWITTER_OAUTH_CONSUMER_SECRET");
		} else {
			TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterOauthConsumerKey, twitterOauthConsumerSecret);
			FabricAppLifecycleCallback.addFabricKit(new TwitterCore(authConfig));
			FabricAppLifecycleCallback.addFabricKit(new TweetUi());
		}
	}
}
