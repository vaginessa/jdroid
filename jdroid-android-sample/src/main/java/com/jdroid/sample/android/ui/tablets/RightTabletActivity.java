package com.jdroid.sample.android.ui.tablets;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class RightTabletActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return RightTabletFragment.class;
	}
}
