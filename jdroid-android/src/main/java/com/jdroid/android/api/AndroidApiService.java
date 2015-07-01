package com.jdroid.android.api;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.http.apache.AndroidApacheHttpServiceFactory;
import com.jdroid.java.api.AbstractApiService;
import com.jdroid.java.http.HttpServiceFactory;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.cache.Cache;
import com.jdroid.java.http.mock.AbstractMockHttpService;

import java.io.File;

public abstract class AndroidApiService extends AbstractApiService {

	@Override
	public HttpServiceFactory createHttpServiceFactory() {
		return new AndroidApacheHttpServiceFactory();
	}

	/**
	 * @see com.jdroid.java.api.AbstractApiService#getHttpCacheDirectory(com.jdroid.java.http.cache.Cache)
	 */
	@Override
	protected File getHttpCacheDirectory(Cache cache) {
		return AbstractApplication.get().getFileSystemCacheDirectory(cache);
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#isHttpMockEnabled()
	 */
	@Override
	protected Boolean isHttpMockEnabled() {
		return AbstractApplication.get().getAppContext().isHttpMockEnabled();
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getAbstractMockHttpServiceInstance(java.lang.Object[])
	 */
	@Override
	protected AbstractMockHttpService getAbstractMockHttpServiceInstance(Object... urlSegments) {
		return AbstractApplication.get().getDebugContext().getAbstractMockHttpServiceInstance(urlSegments);
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getServer()
	 */
	@Override
	protected Server getServer() {
		return AbstractApplication.get().getAppContext().getServer();
	}
}
