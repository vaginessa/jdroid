package com.jdroid.android.sample.ui.loading;

import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;

public class NonBlockingLoadingActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return NonBlockingLoadingFragment.class;
	}
}