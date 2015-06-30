package com.jdroid.javaweb.api;

import com.jdroid.java.api.AbstractApiService;
import com.jdroid.java.http.mock.AbstractMockHttpService;
import com.jdroid.java.http.mock.JsonMockHttpService;
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
	 * @see com.jdroid.java.api.AbstractApiService#getAbstractMockHttpServiceInstance(java.lang.Object[])
	 */
	@Override
	protected AbstractMockHttpService getAbstractMockHttpServiceInstance(Object... urlSegments) {
		return new JsonMockHttpService(urlSegments) {
			
			@Override
			protected Integer getHttpMockSleepDuration(Object... urlSegments) {
				return Application.get().getAppContext().getHttpMockSleepDuration();
			}
		};
	}
}
