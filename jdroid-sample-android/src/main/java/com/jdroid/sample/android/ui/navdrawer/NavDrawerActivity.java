package com.jdroid.sample.android.ui.navdrawer;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class NavDrawerActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return NavDrawerFragment.class;
	}
}