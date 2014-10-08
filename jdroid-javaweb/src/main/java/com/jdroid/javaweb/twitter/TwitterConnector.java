package com.jdroid.javaweb.twitter;

import org.slf4j.Logger;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.context.Application;

public class TwitterConnector {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(TwitterConnector.class);
	
	private TwitterFactory twitterFactory;
	
	public TwitterConnector() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(Application.get().getAppContext().getTwitterOAuthConsumerKey());
		cb.setOAuthConsumerSecret(Application.get().getAppContext().getTwitterOAuthConsumerSecret());
		cb.setOAuthAccessToken(Application.get().getAppContext().getTwitterOAuthAccessToken());
		cb.setOAuthAccessTokenSecret(Application.get().getAppContext().getTwitterOAuthAccessTokenSecret());
		twitterFactory = new TwitterFactory(cb.build());
	}
	
	public void tweetSafe(String text) {
		try {
			if (Application.get().getAppContext().isTwitterEnabled()) {
				Status status = twitterFactory.getInstance().updateStatus(text);
				LOGGER.info("Successfully updated the status to [" + status.getText() + "].");
			} else {
				LOGGER.warn("Ignored tweet status [" + text + "].");
			}
		} catch (TwitterException e) {
			LOGGER.error("Error when posting on Twitter", e);
		}
	}
	
}
