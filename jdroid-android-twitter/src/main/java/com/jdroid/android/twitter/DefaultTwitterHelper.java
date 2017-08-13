package com.jdroid.android.twitter;

import android.content.Context;
import android.view.ViewGroup;

import com.jdroid.android.fragment.AbstractFragment;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.BaseTweetView;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import java.util.List;

public abstract class DefaultTwitterHelper extends TwitterHelper {

	private ViewGroup tweetContainer;

	@Override
	protected void onStartLoadingTweets() {
		AbstractFragment fragment = getAbstractFragment();
		if (fragment != null) {
			fragment.showLoading();
		}
	}

	@Override
	protected void onSuccess(List<Tweet> tweets) {
		AbstractFragment fragment = getAbstractFragment();
		if (fragment != null) {
			tweetContainer.removeAllViews();
			for(Tweet each : tweets) {
				tweetContainer.addView(createTweetView(fragment.getContext(), each));
			}
			fragment.dismissLoading();
		}
	}
	
	protected BaseTweetView createTweetView(Context context, Tweet tweet) {
		return new CompactTweetView(context, tweet);
	}

	@Override
	protected void onFailure() {
		AbstractFragment fragment = getAbstractFragment();
		if (fragment != null) {
			fragment.dismissLoading();
		}
	}

	public void setTweetContainer(ViewGroup tweetContainer) {
		this.tweetContainer = tweetContainer;
	}
}
