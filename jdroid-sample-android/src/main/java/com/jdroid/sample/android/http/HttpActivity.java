package com.jdroid.sample.android.http;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.sample.android.imageloader.ImageLoaderFragment;

public class HttpActivity extends FragmentContainerActivity {

	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return HttpFragment.class;
	}
}