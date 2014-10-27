package com.jdroid.sample.android.ui.maps;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.jdroid.android.maps.AbstractMapFragment;

public class MapFragment extends AbstractMapFragment {
	
	/**
	 * @see com.jdroid.android.maps.AbstractMapFragment#getInfoWindowAdapter()
	 */
	@Override
	protected InfoWindowAdapter getInfoWindowAdapter() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.maps.AbstractMapFragment#onMapSetup()
	 */
	@Override
	protected void onMapSetup() {
	}
	
	/**
	 * @see com.jdroid.android.maps.AbstractMapFragment#onMapSetupFinished()
	 */
	@Override
	protected void onMapSetupFinished() {
	}
	
	/**
	 * @see com.jdroid.android.maps.AbstractMapFragment#isLocationEnabled()
	 */
	@Override
	protected Boolean isLocationEnabled() {
		return true;
	}
	
}
