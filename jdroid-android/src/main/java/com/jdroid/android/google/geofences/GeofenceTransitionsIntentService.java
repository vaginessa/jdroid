package com.jdroid.android.google.geofences;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class GeofenceTransitionsIntentService extends IntentService {

	private final static Logger LOGGER = LoggerUtils.getLogger(GeofenceTransitionsIntentService.class);

	public GeofenceTransitionsIntentService() {
		super(GeofenceTransitionsIntentService.class.getSimpleName());
	}

	public GeofenceTransitionsIntentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		if (geofencingEvent.hasError()) {

			switch (geofencingEvent.getErrorCode()) {
				case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
					// Geofence service is not available now. Typically this is because the user turned off location access in settings > location access.
					LOGGER.error("Geofence service not available.");
				case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
					// Your app has registered more than 100 geofences. Remove unused ones before adding new geofences.
					AbstractApplication.get().getExceptionHandler().logHandledException("Too many geofences");
				case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
					// You have provided more than 5 different PendingIntents to the addGeofences(GoogleApiClient, GeofencingRequest, PendingIntent) call.
					AbstractApplication.get().getExceptionHandler().logHandledException("Too many geofences pending intents");
				default:
					AbstractApplication.get().getExceptionHandler().logHandledException("Unknown geofences error");
			}
		} else {
			// Get the transition type.
			int geofenceTransition = geofencingEvent.getGeofenceTransition();

			if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
				onTransitionEnter(geofencingEvent);
			} else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
				onTransitionDwell(geofencingEvent);
			} else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
				onTransitionExit(geofencingEvent);
			}
		}
	}

	protected void onTransitionEnter(GeofencingEvent geofencingEvent) {
		for (GeofenceTransitionListener each : GeofencesHelper.getGeofenceTransitionListeners()) {
			try {
				each.onTransitionEnter(geofencingEvent);
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			}
		}
	}

	protected void onTransitionDwell(GeofencingEvent geofencingEvent) {
		for (GeofenceTransitionListener each : GeofencesHelper.getGeofenceTransitionListeners()) {
			try {
				each.onTransitionDwell(geofencingEvent);
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			}
		}
	}

	protected void onTransitionExit(GeofencingEvent geofencingEvent) {
		for (GeofenceTransitionListener each : GeofencesHelper.getGeofenceTransitionListeners()) {
			try {
				each.onTransitionExit(geofencingEvent);
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			}
		}
	}
}
