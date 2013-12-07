package com.jdroid.android.api;

import java.io.File;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.api.AbstractApacheApiService;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AndroidApiService extends AbstractApacheApiService {
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getHttpCacheDirectory()
	 */
	@Override
	protected File getHttpCacheDirectory() {
		return AbstractApplication.get().getHttpCacheDirectory();
	}
}
