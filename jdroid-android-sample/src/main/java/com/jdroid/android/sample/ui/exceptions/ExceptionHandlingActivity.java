package com.jdroid.android.sample.ui.exceptions;

import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;

public class ExceptionHandlingActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return ExceptionHandlingFragment.class;
	}
}
