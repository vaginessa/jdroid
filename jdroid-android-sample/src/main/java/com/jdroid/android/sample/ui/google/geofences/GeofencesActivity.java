package com.jdroid.android.sample.ui.google.geofences;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class GeofencesActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return GeofencesFragment.class;
	}

	@Override
	public Boolean isLocationServicesEnabled() {
		return true;
	}
}