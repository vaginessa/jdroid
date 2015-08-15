package com.jdroid.android.sample.ui.hero;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class HeroActivity extends FragmentContainerActivity {

	/**
	 * @see FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return HeroFragment.class;
	}
}