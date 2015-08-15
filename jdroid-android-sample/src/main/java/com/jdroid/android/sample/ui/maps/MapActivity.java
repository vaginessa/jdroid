package com.jdroid.android.sample.ui.maps;

import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;

public class MapActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return MapFragment.class;
	}
}
