package com.jdroid.android.sample.ui.twitter;

import android.view.ViewGroup;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.twitter.TwitterHelper;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.SearchTimeline;

import java.util.List;

public abstract class SampleTwitterHelper extends TwitterHelper {

	private ViewGroup tweetContainer;

	@Override
	protected SearchTimeline createSearchTimeline() {
		SearchTimeline.Builder searchTimelineBuilder = new SearchTimeline.Builder();
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("android");
		searchTimelineBuilder.maxItemsPerRequest(15);
		searchTimelineBuilder.query(queryBuilder.toString());
		return searchTimelineBuilder.build();
	}

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
				tweetContainer.addView(new CompactTweetView(fragment.getContext(), each));
			}
			fragment.dismissLoading();
		}
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
