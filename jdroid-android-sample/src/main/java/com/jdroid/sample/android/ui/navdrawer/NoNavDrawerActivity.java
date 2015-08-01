package com.jdroid.sample.android.ui.navdrawer;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class NoNavDrawerActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return NoNavDrawerFragment.class;
	}

	@Override
	public Boolean isNavDrawerEnabled() {
		return false;
	}
}