package com.jdroid.sample.android.ui.datetime;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class DateTimeActivity extends FragmentContainerActivity {
	
	/**
	 * @see FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return DateTimeFragment.class;
	}
}
