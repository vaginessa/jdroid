package com.jdroid.android.sample.ui.twitter;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.SwipeRefreshLoading;
import com.jdroid.android.sample.R;

public class TwitterFragment extends AbstractFragment implements SwipeRefreshLayout.OnRefreshListener {

	private SampleTwitterHelper twitterHelper;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.twitter_fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		twitterHelper = new SampleTwitterHelper() {
			@Override
			public AbstractFragment getAbstractFragment() {
				return TwitterFragment.this;
			}
		};
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		twitterHelper.setTweetContainer((ViewGroup)findView(R.id.tweetContainer));
		twitterHelper.loadTweets();
	}

	@Override
	public void onRefresh() {
		twitterHelper.loadTweets();
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return new SwipeRefreshLoading();
	}
}
