package com.jdroid.java.http.cache;

import org.slf4j.Logger;
import com.jdroid.java.concurrent.ExecutorUtils;
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
	
	// First try with the cache. If it is a hit, return the cached response and then execute an asynchronous request. If
	// is is not a hit, execute the request
	CACHE_FIRST_ASYNCH_REMOTE {
		
		@Override
		public <T> T execute(final CachedWebService cachedWebService, final Parser parser) {
			T response = cachedWebService.readFromCache(parser);
			if (response == null) {
				response = cachedWebService.executeRequest(parser);
			} else {
				ExecutorUtils.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
							cachedWebService.executeRequest(parser);
						} catch (Exception e) {
							LOGGER.warn("Error when executing asynch request.", e);
							// TODO We should logg the exception on the handler
							// AbstractApplication.get().getExceptionHandler().logHandledException(e);
						}
					}
				});
			}
			return response;
		}
	},
	
	// First try with the cache. If it is a hit (doesn't matter if it is expired or not), return the cached response and
	// then execute an asynchronous request. If
	// is is not a hit, execute the request only if the cache doesn't exist or it is expired
	CACHE_FORCED_FIRST_ASYNCH_REMOTE {
		
		@Override
		public <T> T execute(final CachedWebService cachedWebService, final Parser parser) {
			Long originalTimeToLive = cachedWebService.getTimeToLive();
			cachedWebService.setTimeToLive(null);
			T response = cachedWebService.readFromCache(parser);
			if (response == null) {
				response = cachedWebService.executeRequest(parser);
			} else {
				cachedWebService.setTimeToLive(originalTimeToLive);
				ExecutorUtils.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
							CachingStrategy.CACHE_FIRST.execute(cachedWebService, parser);
						} catch (Exception e) {
							LOGGER.warn("Error when executing asynch request.", e);
							// TODO We should logg the exception on the handler
							// AbstractApplication.get().getExceptionHandler().logHandledException(e);
						}
					}
				});
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
				response = cachedWebService.executeRequest(parser);
			} catch (Exception e) {
				LOGGER.warn("Error when executing request. Cached response will be returned", e);
				// TODO We should logg the exception on the handler
				// AbstractApplication.get().getExceptionHandler().logHandledException(e);
			}
			if (response == null) {
				response = cachedWebService.readFromCache(parser);
			}
			return response;
		}
	};
	
	private static final Logger LOGGER = LoggerUtils.getLogger(CachingStrategy.class);
	
	public abstract <T> T execute(CachedWebService cacheWebService, Parser parser);
}
