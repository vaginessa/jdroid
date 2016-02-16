package com.jdroid.android.sample.ui.usecases;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class UseCasesActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return UseCasesFragment.class;
	}
}
