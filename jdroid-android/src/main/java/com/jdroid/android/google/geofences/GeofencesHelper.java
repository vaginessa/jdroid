package com.jdroid.android.google.geofences;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.jdroid.android.R;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.android.google.SafeResultCallback;
import com.jdroid.android.permission.PermissionHelper;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

public class GeofencesHelper {

	private final static Logger LOGGER = LoggerUtils.getLogger(GeofencesHelper.class);

	private static List<GeofenceTransitionListener> geofenceTransitionListeners = Lists.newArrayList();

	public static void addGeofences(final FragmentIf fragmentIf, final int initialTrigger, final List<Geofence> geofences, final GeofenceResultCallback geofenceResultCallback) {

		PermissionHelper locationPermissionHelper = PermissionHelper.createLocationPermissionHelper((Fragment)fragmentIf);
		// TODO
		locationPermissionHelper.setAppInfoDialogMessageResId(R.string.jdroid_appInviteMessage);
		locationPermissionHelper.setOnRequestPermissionsResultListener(new PermissionHelper.OnRequestPermissionsResultListener() {
			@Override
			public void onRequestPermissionsGranted() {
				addGeofencesInternal(fragmentIf, initialTrigger, geofences, geofenceResultCallback);
			}

			@Override
			public void onRequestPermissionsDenied() {
				// Nothing to do
			}
		});
		Boolean locationPermissionGranted = locationPermissionHelper.checkPermission(false);
		if (locationPermissionGranted) {
			addGeofencesInternal(fragmentIf, initialTrigger, geofences, geofenceResultCallback);
		}
	}

	@SuppressWarnings("MissingPermission")
	private static void addGeofencesInternal(FragmentIf fragmentIf, final int initialTrigger, final List<Geofence> geofences, GeofenceResultCallback geofenceResultCallback) {
		GeofencingRequest.Builder geofencingRequestBuilder = new GeofencingRequest.Builder();
		geofencingRequestBuilder.setInitialTrigger(initialTrigger);
		geofencingRequestBuilder.addGeofences(geofences);
		GeofencingRequest geofencingRequest = geofencingRequestBuilder.build();

		Intent intent = new Intent(AbstractApplication.get(), GeofenceTransitionsIntentService.class);
		// We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
		// calling addGeofences() and removeGeofences().
		PendingIntent geofencePendingIntent = PendingIntent.getService(AbstractApplication.get(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		LocationServices.GeofencingApi.addGeofences(fragmentIf.getActivityIf().getGoogleApiClient(), geofencingRequest, geofencePendingIntent).setResultCallback(geofenceResultCallback);
	}

	public static void removeGeofence(ActivityIf activityIf, String geoFenceId, GeofenceResultCallback geofenceResultCallback) {
		LocationServices.GeofencingApi.removeGeofences(activityIf.getGoogleApiClient(), Lists.newArrayList(geoFenceId)).setResultCallback(geofenceResultCallback);
	}

	public static List<GeofenceTransitionListener> getGeofenceTransitionListeners() {
		return geofenceTransitionListeners;
	}

	public static void clearGeofenceTransitionListeners() {
		geofenceTransitionListeners.clear();
	}

	public static void addGeofenceTransitionListener(GeofenceTransitionListener geofenceTransitionListener) {
		if (geofenceTransitionListener != null && !geofenceTransitionListeners.contains(geofenceTransitionListener)) {
			geofenceTransitionListeners.add(geofenceTransitionListener);
		}
	}

	public static void removeGeofenceTransitionListener(GeofenceTransitionListener geofenceTransitionListener) {
		geofenceTransitionListeners.remove(geofenceTransitionListener);
	}

	public static abstract class GeofenceResultCallback extends SafeResultCallback<Status> {

		@Override
		public final void onFailedResult(@NonNull Status result) {
			switch (result.getStatusCode()) {
				case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
					// Geofence service is not available now. Typically this is because the user turned off location access in settings > location access.
					LOGGER.warn("Geofence service not available.");
					onGeofenceServiceNotAvailable();
				case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
					// Your app has registered more than 100 geofences. Remove unused ones before adding new geofences.
					AbstractApplication.get().getExceptionHandler().logHandledException("Too many geofences");
					onUnexpectedError(result);
				case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
					// You have provided more than 5 different PendingIntents to the addGeofences(GoogleApiClient, GeofencingRequest, PendingIntent) call.
					AbstractApplication.get().getExceptionHandler().logHandledException("Too many geofences pending intents");
					onUnexpectedError(result);
				default:
					AbstractApplication.get().getExceptionHandler().logHandledException("Unknown geofences error");
					onUnexpectedError(result);
			}
		}

		protected abstract void onGeofenceServiceNotAvailable();

		protected void onUnexpectedError(@NonNull Status result) {
			// Do nothing
		}
	}
}
