package com.jdroid.android.loading;

import android.support.v4.widget.SwipeRefreshLayout;
import com.jdroid.android.R;
import com.jdroid.android.fragment.FragmentIf;

public class SwipeRefreshLoading implements FragmentLoading {
	
	private SwipeRefreshLayout swipeRefreshLayout;
	
	/**
	 * @see com.jdroid.android.loading.FragmentLoading#show(com.jdroid.android.fragment.FragmentIf)
	 */
	@Override
	public void show(FragmentIf fragmentIf) {
		if (swipeRefreshLayout == null) {
			swipeRefreshLayout = fragmentIf.findView(R.id.swipeRefreshLayout);
			swipeRefreshLayout.setOnRefreshListener(fragmentIf);
		}
		swipeRefreshLayout.setRefreshing(true);
	}
	
	/**
	 * @see com.jdroid.android.loading.FragmentLoading#dismiss(com.jdroid.android.fragment.FragmentIf)
	 */
	@Override
	public void dismiss(FragmentIf fragmentIf) {
		swipeRefreshLayout.setRefreshing(false);
	}
	
}
