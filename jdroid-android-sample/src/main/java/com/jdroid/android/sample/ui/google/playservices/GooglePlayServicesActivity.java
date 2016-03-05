package com.jdroid.android.sample.ui.google.playservices;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class GooglePlayServicesActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return GooglePlayServicesFragment.class;
	}
}