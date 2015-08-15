package com.jdroid.android.sample.ui.imageloader;

import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;

public class ImageLoaderActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return ImageLoaderFragment.class;
	}
}
