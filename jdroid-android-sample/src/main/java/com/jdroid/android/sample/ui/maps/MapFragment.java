package com.jdroid.android.sample.ui.maps;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.jdroid.android.google.maps.AbstractMapFragment;

public class MapFragment extends AbstractMapFragment {
	
	@Override
	protected InfoWindowAdapter getInfoWindowAdapter() {
		return null;
	}
	
	@Override
	protected void onMapSetup() {
	}
	
	@Override
	protected void onMapSetupFinished() {
	}
	
	@Override
	protected Boolean isLocationEnabled() {
		return true;
	}

	@Override
	public AdSize getAdSize() {
		return null;
	}
}
