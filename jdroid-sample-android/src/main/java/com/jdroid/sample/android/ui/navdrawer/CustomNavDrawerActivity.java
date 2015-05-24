package com.jdroid.sample.android.ui.navdrawer;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class CustomNavDrawerActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return CustomNavDrawerFragment.class;
	}
}
