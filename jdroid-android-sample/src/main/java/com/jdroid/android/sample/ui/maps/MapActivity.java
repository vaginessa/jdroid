package com.jdroid.android.sample.ui.maps;

import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.google.maps.MapFacadeFragment;

public class MapActivity extends FragmentContainerActivity {

	@Override
	protected Fragment createNewFragment() {
		return MapFacadeFragment.instanceMapFragment(this, getFragmentContainerId(), MapFragment.class, null);
	}
}
