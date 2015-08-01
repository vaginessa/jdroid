package com.jdroid.sample.android.ui.analytics;

import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;

public class AnalyticsActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return AnalyticsFragment.class;
	}
}
