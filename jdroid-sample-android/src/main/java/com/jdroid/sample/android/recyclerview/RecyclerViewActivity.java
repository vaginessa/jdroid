package com.jdroid.sample.android.recyclerview;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class RecyclerViewActivity extends FragmentContainerActivity {

	/**
	 * @see FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return RecyclerViewFragment.class;
	}
}