package com.jdroid.javaweb.api;

import com.jdroid.java.api.AbstractApiService;
import com.jdroid.java.http.mock.AbstractMockWebService;
import com.jdroid.java.http.mock.JsonMockWebService;
import com.jdroid.javaweb.context.Application;

public abstract class ServerApiService extends AbstractApiService {
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#isHttpMockEnabled()
	 */
	@Override
	protected Boolean isHttpMockEnabled() {
		return Application.get().getAppContext().isHttpMockEnabled();
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getAbstractMockWebServiceInstance(java.lang.Object[])
	 */
	@Override
	protected AbstractMockWebService getAbstractMockWebServiceInstance(Object... urlSegments) {
		return new JsonMockWebService(urlSegments) {
			
			@Override
			protected Integer getHttpMockSleepDuration(Object... urlSegments) {
				return Application.get().getAppContext().getHttpMockSleepDuration();
			}
		};
	}
}
