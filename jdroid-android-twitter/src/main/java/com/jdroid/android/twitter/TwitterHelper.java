package com.jdroid.android.twitter;

import android.support.annotation.MainThread;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;

import java.util.List;

// TODO Add support to rotation. The helper is called on each screen rotation
public abstract class TwitterHelper {

	public void loadTweets() {
		onStartLoadingTweets();
		SearchTimeline searchTimeline = createSearchTimeline();
		searchTimeline.next(null, new Callback<TimelineResult<Tweet>>() {
			@Override
			public void success(Result<TimelineResult<Tweet>> result) {
				try {
					TwitterHelper.this.onSuccess(result.data.items);
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				}
			}

			@Override
			public void failure(TwitterException e) {
				Boolean connectionError = false;
				if (e instanceof TwitterApiException) {
					TwitterApiException twitterApiException = (TwitterApiException)e;
					if (e.getMessage().equals("Unable to resolve host \"api.twitter.com\": No address associated with hostname")) {
						connectionError = true;
					} else if (e.getMessage().startsWith("failed to connect to ")) {
						connectionError = true;
					} else {
						String errorMessage = twitterApiException.getErrorCode() + " " + twitterApiException.getErrorMessage();
						AbstractApplication.get().getAnalyticsSender().trackErrorBreadcrumb(errorMessage);
					}
				}
				if (!connectionError) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				}
				TwitterHelper.this.onFailure();
			}
		});
	}

	protected abstract SearchTimeline createSearchTimeline();

	@MainThread
	protected  void onStartLoadingTweets() {
		// Do Nothing
	}

	@MainThread
	protected abstract void onSuccess(List<Tweet> tweets);

	@MainThread
	protected void onFailure() {
		// Do Nothing
	}

	public abstract AbstractFragment getAbstractFragment();
}
