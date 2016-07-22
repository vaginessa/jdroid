package com.jdroid.android.sample.ui.imageloader;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class ImageLoaderActivity extends FragmentContainerActivity {
	
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return ImageLoaderFragment.class;
	}
}
