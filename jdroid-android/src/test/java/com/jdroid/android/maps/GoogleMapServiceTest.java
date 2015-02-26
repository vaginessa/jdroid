package com.jdroid.android.maps;

import com.jdroid.android.CustomRobolectricRunner;
import com.jdroid.android.domain.GeoLocation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(CustomRobolectricRunner.class)
public class GoogleMapServiceTest {

	private GoogleMapService googleMapService;

	@Before
	public void setUp() throws Exception {
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
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