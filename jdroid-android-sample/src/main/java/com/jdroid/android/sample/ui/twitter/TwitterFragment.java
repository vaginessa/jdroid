package com.jdroid.android.sample.ui.twitter;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.twitter.DefaultTwitterHelper;
import com.jdroid.android.twitter.TwitterListFragment;

public class TwitterFragment extends TwitterListFragment {

	@Override
	protected DefaultTwitterHelper createTwitterHelper() {
		return new SampleTwitterHelper() {
			@Override
			public AbstractFragment getAbstractFragment() {
				return TwitterFragment.this;
			}
		};
	}
}
