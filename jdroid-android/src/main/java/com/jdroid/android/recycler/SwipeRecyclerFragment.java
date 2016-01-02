package com.jdroid.android.recycler;

import com.jdroid.android.R;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.SwipeRefreshLoading;

public class SwipeRecyclerFragment extends AbstractRecyclerFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.swipe_recycler_fragment;
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return new SwipeRefreshLoading();
	}
}
