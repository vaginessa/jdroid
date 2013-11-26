package com.jdroid.java.http.cache;

import org.slf4j.Logger;
import com.jdroid.java.parser.Parser;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public enum CachingStrategy {
	
	NO_CACHE {
		
		@Override
		public <T> T execute(CachedWebService cachedWebService, Parser parser) {
			return cachedWebService.executeRequest(parser);
		};
	},
	
	// First try with the cache. If it is a hit, return the cached response, else execute the request
	CACHE_FIRST {
		
		@Override
		public <T> T execute(CachedWebService cachedWebService, Parser parser) {
			T response = cachedWebService.readFromCache(parser);
			if (response == null) {
				response = cachedWebService.executeRequest(parser);
			}
			return response;
		}
	},
	
	// If it is a hit, return the cached response, else return null
	CACHE_ONLY {
		
		@Override
		public <T> T execute(CachedWebService cachedWebService, Parser parser) {
			return cachedWebService.readFromCache(parser);
		}
	},
	
	// Execute the request first. If it fails try with the cache
	REMOTE_FIRST {
		
		@Override
		public <T> T execute(CachedWebService cachedWebService, Parser parser) {
			T response = null;
			try {
				cachedWebService.executeRequest(parser);
			} catch (Exception e) {
				LOGGER.warn("Error when executing request. Cached response will be returned", e);
			}
			if (response == null) {
				response = cachedWebService.readFromCache(parser);
			}
			return response;
		}
	};
	
	private static final Logger LOGGER = LoggerUtils.getLogger(CachingStrategy.class);
	
	public abstract <T> T execute(CachedWebService cachedWebService, Parser parser);
}
