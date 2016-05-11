package com.jdroid.android.integration.google.maps;

import com.jdroid.android.AbstractIntegrationTest;
import com.jdroid.android.domain.GeoLocation;
import com.jdroid.android.google.maps.GoogleMapService;
import com.jdroid.android.google.maps.Route;
import com.jdroid.android.google.maps.RouteMode;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class GoogleMapServiceTest extends AbstractIntegrationTest {

	private GoogleMapService googleMapService;

	@Override
	protected void onSetup() {
		super.onSetup();
		googleMapService = new GoogleMapService();
	}

	@Test
	public void findDirections() {
		GeoLocation originLocation = new GeoLocation(-34.603638, -58.377369);
		GeoLocation destinationLocation = new GeoLocation(-34.602473, -58.380116);

		// Test walking
		Route route = googleMapService.findDirections(originLocation, destinationLocation, RouteMode.WALKING);
		assertNotNull(route);
		assertTrue(route.isValid());
		assertEquals(RouteMode.WALKING, route.getMode());
		assertTrue(route.getPoints().size() > 2);

		// Test driving
		route = googleMapService.findDirections(originLocation, destinationLocation, RouteMode.DRIVING);
		assertNotNull(route);
		assertTrue(route.isValid());
		assertEquals(RouteMode.DRIVING, route.getMode());
		assertTrue(route.getPoints().size() > 2);

		// Test unreachable location walking
		destinationLocation = new GeoLocation(-38.309336, -15.546541);
		route = googleMapService.findDirections(originLocation, destinationLocation, RouteMode.WALKING);
		assertNull(route);

		// Test unreachable location driving
		destinationLocation = new GeoLocation(-38.309336, -15.546541);
		route = googleMapService.findDirections(originLocation, destinationLocation, RouteMode.DRIVING);
		assertNull(route);
	}
}