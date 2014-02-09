package com.jdroid.android.api;

import java.io.File;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.api.AbstractApacheApiService;
import com.jdroid.java.http.cache.Cache;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AndroidApiService extends AbstractApacheApiService {
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getHttpCacheDirectory(com.jdroid.java.http.cache.Cache)
	 */
	@Override
	protected File getHttpCacheDirectory(Cache cache) {
		return AbstractApplication.get().getFileSystemCacheDirectory(cache);
	}
}
