package com.jdroid.android.sample.ui.cardview;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class SimpleCardViewActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return SimpleCardViewFragment.class;
	}
}