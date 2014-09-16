package com.jdroid.android.api;

import java.io.File;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.debug.mocks.AndroidJsonMockWebService;
import com.jdroid.android.http.AndroidHttpClientFactory;
import com.jdroid.java.api.AbstractApacheApiService;
import com.jdroid.java.http.apache.HttpClientFactory;
import com.jdroid.java.http.cache.Cache;
import com.jdroid.java.http.mock.AbstractMockWebService;

public abstract class AndroidApiService extends AbstractApacheApiService {
	
	/**
	 * @see com.jdroid.java.api.AbstractApacheApiService#getHttpClientFactoryInstance()
	 */
	@Override
	protected HttpClientFactory getHttpClientFactoryInstance() {
		return AndroidHttpClientFactory.get();
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
	 * @see com.jdroid.java.api.AbstractApiService#getAbstractMockWebServiceInstance(java.lang.Object[])
	 */
	@Override
	protected AbstractMockWebService getAbstractMockWebServiceInstance(Object... urlSegments) {
		return new AndroidJsonMockWebService(urlSegments);
	}
}
