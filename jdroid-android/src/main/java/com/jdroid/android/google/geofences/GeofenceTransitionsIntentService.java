package com.jdroid.android.google.geofences;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

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

			// Test that the reported transition was of interest.
			if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
					geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

				// Get the geofences that were triggered. A single event can trigge multiple geofences.
				List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

				NotificationBuilder builder = new NotificationBuilder("sampleGeoFenceNotification");
				builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());
				builder.setContentTitle("Geofence triggered");
				builder.setContentText(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ? "Enter" : "Exit");

				NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);

			} else {
				// Log the error.
			}
		}

	}
}
