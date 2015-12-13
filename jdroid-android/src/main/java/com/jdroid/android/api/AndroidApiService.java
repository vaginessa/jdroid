package com.jdroid.android.api;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.api.AbstractApiService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.cache.Cache;
import com.jdroid.java.http.mock.AbstractMockHttpService;

import java.io.File;

public abstract class AndroidApiService extends AbstractApiService {

	@Override
	protected File getHttpCacheDirectory(Cache cache) {
		return AbstractApplication.get().getCacheManager().getFileSystemCacheDirectory(cache);
	}
	
	@Override
	protected Boolean isHttpMockEnabled() {
		return AbstractApplication.get().getDebugContext().isHttpMockEnabled();
	}
	
	@Override
	protected AbstractMockHttpService getAbstractMockHttpServiceInstance(Object... urlSegments) {
		return AbstractApplication.get().getDebugContext().getAbstractMockHttpServiceInstance(urlSegments);
	}
	
	@Override
	protected Server getServer() {
		return AbstractApplication.get().getAppContext().getServer();
	}
}
