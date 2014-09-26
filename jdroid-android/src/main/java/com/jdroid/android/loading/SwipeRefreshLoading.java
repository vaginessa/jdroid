package com.jdroid.android.loading;

import android.support.v4.widget.SwipeRefreshLayout;
import com.jdroid.android.R;
import com.jdroid.android.fragment.FragmentIf;

public class SwipeRefreshLoading implements FragmentLoading {
	
	private SwipeRefreshLayout swipeRefreshLayout;
	
	private Integer colorRes1;
	private Integer colorRes2;
	private Integer colorRes3;
	private Integer colorRes4;
	
	/**
	 * @see com.jdroid.android.loading.FragmentLoading#show(com.jdroid.android.fragment.FragmentIf)
	 */
	@Override
	public void show(FragmentIf fragmentIf) {
		if (swipeRefreshLayout == null) {
			swipeRefreshLayout = fragmentIf.findView(R.id.swipeRefreshLayout);
			swipeRefreshLayout.setOnRefreshListener(fragmentIf);
			if ((colorRes1 != null) && (colorRes2 != null) && (colorRes3 != null) && (colorRes4 != null)) {
				swipeRefreshLayout.setColorSchemeResources(colorRes1, colorRes2, colorRes3, colorRes4);
			}
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
	
	public void setColorRes1(Integer colorRes1) {
		this.colorRes1 = colorRes1;
	}
	
	public void setColorRes2(Integer colorRes2) {
		this.colorRes2 = colorRes2;
	}
	
	public void setColorRes3(Integer colorRes3) {
		this.colorRes3 = colorRes3;
	}
	
	public void setColorRes4(Integer colorRes4) {
		this.colorRes4 = colorRes4;
	}
	
}
