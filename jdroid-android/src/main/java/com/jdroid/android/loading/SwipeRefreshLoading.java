package com.jdroid.android.loading;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import com.jdroid.android.R;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.fragment.FragmentIf;

public class SwipeRefreshLoading implements FragmentLoading {
	
	private Integer colorRes1;
	private Integer colorRes2;
	private Integer colorRes3;
	private Integer colorRes4;
	
	/**
	 * @see com.jdroid.android.loading.FragmentLoading#onViewCreated(com.jdroid.android.fragment.FragmentIf)
	 */
	@Override
	public void onViewCreated(final FragmentIf fragmentIf) {
		// Fix to this behaviour: "everytime you scroll up in that view, the SwipeRefreshLayout fires and updates,
		// making your app unable to scroll up in a list"
		if (fragmentIf instanceof AbstractListFragment) {
			final AbstractListFragment<?> abstractListFragment = (AbstractListFragment<?>)fragmentIf;
			abstractListFragment.getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					int topRowVerticalPosition = (abstractListFragment.getListView().getChildCount() == 0) ? 0
							: abstractListFragment.getListView().getChildAt(0).getTop();
					getSwiteRefreshLayout(fragmentIf).setEnabled(topRowVerticalPosition >= 0);
				}
			});
		}
	}
	
	/**
	 * @see com.jdroid.android.loading.FragmentLoading#show(com.jdroid.android.fragment.FragmentIf)
	 */
	@Override
	public void show(FragmentIf fragmentIf) {
		SwipeRefreshLayout swipeRefreshLayout = getSwiteRefreshLayout(fragmentIf);
		swipeRefreshLayout.setOnRefreshListener(fragmentIf);
		if ((colorRes1 != null) && (colorRes2 != null) && (colorRes3 != null) && (colorRes4 != null)) {
			swipeRefreshLayout.setColorSchemeResources(colorRes1, colorRes2, colorRes3, colorRes4);
		}
		swipeRefreshLayout.setRefreshing(true);
	}
	
	/**
	 * @see com.jdroid.android.loading.FragmentLoading#dismiss(com.jdroid.android.fragment.FragmentIf)
	 */
	@Override
	public void dismiss(FragmentIf fragmentIf) {
		SwipeRefreshLayout swipeRefreshLayout = getSwiteRefreshLayout(fragmentIf);
		swipeRefreshLayout.setRefreshing(false);
	}
	
	public SwipeRefreshLayout getSwiteRefreshLayout(FragmentIf fragmentIf) {
		return fragmentIf.findView(R.id.swipeRefreshLayout);
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
