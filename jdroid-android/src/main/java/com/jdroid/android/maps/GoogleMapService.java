package com.jdroid.android.maps;

import com.jdroid.android.api.AndroidApiService;
import com.jdroid.android.domain.GeoLocation;
import com.jdroid.java.http.DefaultServer;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.mock.AbstractMockWebService;

public class GoogleMapService extends AndroidApiService {
	
	private static final Server GMAPS_API = new DefaultServer("maps.googleapis.com/maps/api");
	
	private static final Object DIRECTIONS = "directions";
	private static final String OUTPUT_FORMAT = "json";
	private static final String ORIGIN = "origin";
	private static final String DESTINATION = "destination";
	private static final String SENSOR = "sensor";
	private static final String MODE = "mode";
	
	public Route findDirections(GeoLocation source, GeoLocation destination, RouteMode mode) {
		WebService webservice = newGetService(DIRECTIONS, OUTPUT_FORMAT);
		webservice.addQueryParameter(ORIGIN, toHttpParam(source));
		webservice.addQueryParameter(DESTINATION, toHttpParam(destination));
		webservice.addQueryParameter(SENSOR, "true");
		webservice.addQueryParameter(MODE, mode.getArgName());
		Route route = webservice.execute(new GoogleRouteParser());
		if (route != null) {
			route.setMode(mode);
		}
		return route;
	}
	
	private String toHttpParam(GeoLocation geoLocation) {
		return Double.toString(geoLocation.getLatitude()) + "," + Double.toString(geoLocation.getLongitude());
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getServer()
	 */
	@Override
	protected Server getServer() {
		return GMAPS_API;
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getAbstractMockWebServiceInstance(java.lang.Object[])
	 */
	@Override
	protected AbstractMockWebService getAbstractMockWebServiceInstance(Object... urlSegments) {
		return null;
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#isHttpMockEnabled()
	 */
	@Override
	protected Boolean isHttpMockEnabled() {
		return false;
	}
}
