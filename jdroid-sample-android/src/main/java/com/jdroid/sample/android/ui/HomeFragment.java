package com.jdroid.sample.android.ui;

import android.os.Bundle;
import android.view.View;
import com.jdroid.android.contextual.ContextualItemsAdapter;
import com.jdroid.android.fragment.AbstractListFragment;

public class HomeFragment extends AbstractListFragment<HomeItem> {
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setListAdapter(new ContextualItemsAdapter(getActivity(), HomeItem.values()));
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(HomeItem item) {
		item.startActivity(getActivity());
	}
	
}
