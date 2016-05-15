package com.jdroid.android.sample.ui.fab;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.sample.ui.http.HttpFragment;

public class FabActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return FabFragment.class;
	}
}