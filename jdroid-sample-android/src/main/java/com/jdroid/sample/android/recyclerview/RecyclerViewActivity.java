package com.jdroid.sample.android.recyclerview;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.sample.android.R;

public class RecyclerViewActivity extends FragmentContainerActivity {

	/**
	 * @see FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return RecyclerViewFragment.class;
	}

	@Override
	public Integer getMenuResourceId() {
		return R.menu.recycler_menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return getFragment().onOptionsItemSelected(item);
	}
}