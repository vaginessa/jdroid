package com.jdroid.android.sample.ui.recyclerview;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class SimpleRecyclerActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return SimpleRecyclerFragment.class;
	}
}