package com.jdroid.android.sample.ui.google.plus;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class GooglePlusActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return GooglePlusFragment.class;
	}
}