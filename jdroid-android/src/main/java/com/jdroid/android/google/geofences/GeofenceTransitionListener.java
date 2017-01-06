package com.jdroid.android.google.geofences;

import com.google.android.gms.location.GeofencingEvent;

public interface GeofenceTransitionListener {

	void onTransitionEnter(GeofencingEvent geofencingEvent);

	void onTransitionDwell(GeofencingEvent geofencingEvent);

	void onTransitionExit(GeofencingEvent geofencingEvent);
}
