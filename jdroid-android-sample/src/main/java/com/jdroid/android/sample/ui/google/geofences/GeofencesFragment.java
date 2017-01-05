package com.jdroid.android.sample.ui.google.geofences;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.geofences.GeofencesHelper;
import com.jdroid.android.sample.R;
import com.jdroid.android.snackbar.SnackbarBuilder;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.NumberUtils;

import java.util.List;

public class GeofencesFragment extends AbstractFragment implements View.OnFocusChangeListener {

	private static final Double LATITUDE = -34.608861;
	private static final Double LONGITUDE = -58.370833;
	private static final Long GEOFENCE_RADIUS_IN_METERS = 300L;
	private static final Long GEOFENCE_EXPIRATION_IN_MILLISECONDS = DateUtils.MILLIS_PER_DAY;

	private static final String GEOFENCE_ID = "sampleGeofenceId";

	private MapView mapView;

	private EditText latitude;
	private EditText longitude;
	private EditText geofenceRadius;

	private GoogleMap googleMap;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.geofences_fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
		LatLng latLng1 = new LatLng(LATITUDE, LONGITUDE);
		boundsBuilder.include(latLng1);

		GoogleMapOptions options = new GoogleMapOptions();
		options.camera(CameraPosition.fromLatLngZoom(boundsBuilder.build().getCenter(), 15));
		options.mapType(GoogleMap.MAP_TYPE_NORMAL);
		mapView = new MapView(getActivity(), options);
		mapView.onCreate(savedInstanceState);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (googleMap != null) {
			googleMap.clear();
			drawGeofence(googleMap);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		latitude = findView(R.id.latitude);
		latitude.setText(LATITUDE.toString());
		latitude.setOnFocusChangeListener(this);

		longitude = findView(R.id.longitude);
		longitude.setText(LONGITUDE.toString());
		longitude.setOnFocusChangeListener(this);

		geofenceRadius = findView(R.id.geofenceRadius);
		geofenceRadius.setText(GEOFENCE_RADIUS_IN_METERS.toString());
		geofenceRadius.setOnFocusChangeListener(this);

		final EditText geofenceExpiration = findView(R.id.geofenceExpiration);
		geofenceExpiration.setText(GEOFENCE_EXPIRATION_IN_MILLISECONDS.toString());

		ViewGroup mapContainer = findView(R.id.mapContainer);

		mapContainer.addView(mapView);

		mapView.onCreate(savedInstanceState);
		mapView.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(final GoogleMap googleMap) {
				GeofencesFragment.this.googleMap = googleMap;
				googleMap.getUiSettings().setMapToolbarEnabled(false);
				googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
					@Override
					public void onMapLoaded() {
						drawGeofence(googleMap);
						googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
							@Override
							public void onMapLongClick(LatLng latLng) {
								latitude.setText(Double.valueOf(latLng.latitude).toString());
								longitude.setText(Double.valueOf(latLng.longitude).toString());

								googleMap.clear();
								drawGeofence(googleMap);
							}
						});
					}
				});
			}
		});

		findView(R.id.addGeofence).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				final List<Geofence> geofences = Lists.newArrayList();
				Geofence.Builder builder = new Geofence.Builder();
				builder.setRequestId(GEOFENCE_ID);
				builder.setCircularRegion(getLatitude(), getLongitude(), getGeofenceRadius());
				builder.setExpirationDuration(NumberUtils.getLong(geofenceExpiration.getText().toString()));
				builder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
				geofences.add(builder.build());

				GeofencesHelper.addGeofences(GeofencesFragment.this, GeofencingRequest.INITIAL_TRIGGER_ENTER, geofences, new GeofencesHelper.GeofenceResultCallback() {
					@Override
					public void onSuccessResult(@NonNull Status result) {
						SnackbarBuilder snackbarBuilder = new SnackbarBuilder();
						snackbarBuilder.setDescription("Geofence created");
						snackbarBuilder.build(getActivity()).show();
					}

					@Override
					protected void onGeofenceServiceNotAvailable() {
						SnackbarBuilder snackbarBuilder = new SnackbarBuilder();
						snackbarBuilder.setDescription("Geofence service not available");
						snackbarBuilder.build(getActivity()).show();
					}

					@Override
					protected void onUnexpectedError(@NonNull Status result) {
						SnackbarBuilder snackbarBuilder = new SnackbarBuilder();
						snackbarBuilder.setDescription("Unexpected error creating Geofence");
						snackbarBuilder.build(getActivity()).show();
					}
				});
			}
		});

		findView(R.id.removeGeofence).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GeofencesHelper.removeGeofence(getActivityIf(), GEOFENCE_ID, new GeofencesHelper.GeofenceResultCallback() {
					@Override
					public void onSuccessResult(@NonNull Status result) {
						SnackbarBuilder snackbarBuilder = new SnackbarBuilder();
						snackbarBuilder.setDescription("Geofence removed");
						snackbarBuilder.build(getActivity()).show();
					}

					@Override
					protected void onGeofenceServiceNotAvailable() {
						SnackbarBuilder snackbarBuilder = new SnackbarBuilder();
						snackbarBuilder.setDescription("Geofence service not available");
						snackbarBuilder.build(getActivity()).show();
					}

					@Override
					protected void onUnexpectedError(@NonNull Status result) {
						SnackbarBuilder snackbarBuilder = new SnackbarBuilder();
						snackbarBuilder.setDescription("Unexpected error removing Geofence");
						snackbarBuilder.build(getActivity()).show();
					}
				});
			}
		});

	}

	private void drawGeofence(GoogleMap googleMap) {
		LatLng latLng1 = new LatLng(getLatitude(), getLongitude());
		MarkerOptions options1 = new MarkerOptions();
		options1.position(latLng1);
		options1.anchor(0.5f, 0.5f);
		options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
		googleMap.addMarker(options1);

		CircleOptions circleOptions = new CircleOptions();
		circleOptions.center(latLng1);
		circleOptions.radius(getGeofenceRadius().doubleValue());
		circleOptions.strokeColor(Color.GRAY);
		googleMap.addCircle(circleOptions);

		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 15));
	}


	@Override
	public void onStart() {
		super.onStart();

		mapView.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();

		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();

		mapView.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();

		mapView.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		mapView.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

		mapView.onLowMemory();
	}

	private Double getLatitude() {
		return NumberUtils.getDouble(latitude.getText().toString());
	}

	private Double getLongitude() {
		return NumberUtils.getDouble(longitude.getText().toString());
	}

	private Float getGeofenceRadius() {
		return NumberUtils.getFloat(geofenceRadius.getText().toString());
	}
}
