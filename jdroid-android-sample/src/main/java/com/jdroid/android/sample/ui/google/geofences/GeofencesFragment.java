package com.jdroid.android.sample.ui.google.geofences;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.geofences.GeofencesHelper;
import com.jdroid.android.sample.R;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.NumberUtils;

import java.util.List;

public class GeofencesFragment extends AbstractFragment {

	private static final Long GEOFENCE_RADIUS_IN_METERS = 300L;
	private static final Long GEOFENCE_EXPIRATION_IN_MILLISECONDS = DateUtils.MILLIS_PER_DAY;

	private static final String GEOFENCE_ID = "sampleGeofenceId";

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.geofences_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final EditText geofenceRadius = findView(R.id.geofenceRadius);
		geofenceRadius.setText(GEOFENCE_RADIUS_IN_METERS.toString());

		final EditText geofenceExpiration = findView(R.id.geofenceExpiration);
		geofenceExpiration.setText(GEOFENCE_EXPIRATION_IN_MILLISECONDS.toString());

		findView(R.id.addGeofence).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				final List<Geofence> geofences = Lists.newArrayList();
				Geofence.Builder builder = new Geofence.Builder();
				builder.setRequestId(GEOFENCE_ID);
				builder.setCircularRegion(-34.608861, -58.370833, NumberUtils.getFloat(geofenceRadius.getText().toString()));
				builder.setExpirationDuration(NumberUtils.getLong(geofenceExpiration.getText().toString()));
				builder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
				geofences.add(builder.build());

				GeofencesHelper.addGeofences(GeofencesFragment.this, GeofencingRequest.INITIAL_TRIGGER_ENTER, geofences);
			}
		});

		findView(R.id.removeGeofence).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GeofencesHelper.removeGeofence(getActivityIf(), GEOFENCE_ID);
			}
		});

	}
}
