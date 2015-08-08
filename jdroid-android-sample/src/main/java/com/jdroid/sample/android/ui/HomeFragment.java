package com.jdroid.sample.android.ui;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.recycler.AbstractRecyclerFragment;

public class HomeFragment extends AbstractRecyclerFragment<HomeItem> {
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setAdapter(new HomeAdapter(HomeItem.values()));
	}
	
	@Override
	public void onItemSelected(HomeItem item, View view) {
		item.startActivity(getActivity());
	}
	
}
