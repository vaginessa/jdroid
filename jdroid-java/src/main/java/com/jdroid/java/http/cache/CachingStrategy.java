package com.jdroid.java.http.cache;

import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.parser.Parser;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public enum CachingStrategy {
	
	NO_CACHE {
		
		@Override
		public <T> T execute(CachedHttpService cachedHttpService, Parser parser) {
			return cachedHttpService.executeRequest(parser);
		}
	},
	
	// First try with the cache. If it is a hit, return the cached response, else execute the request
	CACHE_FIRST {
		
		@Override
		public <T> T execute(CachedHttpService cachedHttpService, Parser parser) {
			T response = cachedHttpService.readFromCache(parser);
			if (response == null) {
				response = cachedHttpService.executeRequest(parser);
			}
			return response;
		}
	},
	
	// First try with the cache. If it is a hit, return the cached response and then execute an asynchronous request. If
	// is is not a hit, execute the request
	CACHE_FIRST_ASYNCH_REMOTE {
		
		@Override
		public <T> T execute(final CachedHttpService cachedHttpService, final Parser parser) {
			T response = cachedHttpService.readFromCache(parser);
			if (response == null) {
				response = cachedHttpService.executeRequest(parser);
			} else {
				ExecutorUtils.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
							cachedHttpService.executeRequest(parser);
						} catch (Exception e) {
							LoggerUtils.logHandledException(LOGGER, e);
						}
					}
				});
			}
			return response;
		}
	},

	// First try with the cache. If it is a hit, return the cached response, else execute the request.
	// If the request fail and the cache is expired but available, return also the cached response.
	CACHE_FIRST_WITH_CACHE_FORCED_ON_FAIL {

		@Override
		public <T> T execute(CachedHttpService cachedHttpService, Parser parser) {
			T response = cachedHttpService.readFromCache(parser);
			if (response == null) {
				try {
					response = cachedHttpService.executeRequest(parser);
				} catch (Exception e) {
					LoggerUtils.logHandledException(LOGGER, e);
				}
			}
			if (response == null) {
				Long originalTimeToLive = cachedHttpService.getTimeToLive();
				cachedHttpService.setTimeToLive(null);
				response = cachedHttpService.readFromCache(parser);
				cachedHttpService.setTimeToLive(originalTimeToLive);
			}
			if (response == null && parser != null) {
				throw new UnexpectedException("The service was expecting a response, but it was null (CachingStrategy.CACHE_FIRST_WITH_CACHE_FORCED_ON_FAIL)");
			}
			return response;
		}
	},
	
	// If there isn't anything on the cache, then execute the request caching the response.
	// If there is something in the cache (doesn't matter if it is expired or not) return the cache content. Then
	// execute an asynchronous request (only if the cache is expired) caching the response.
	CACHE_FORCED_FIRST_ASYNCH_REMOTE {
		
		@Override
		public <T> T execute(final CachedHttpService cachedHttpService, final Parser parser) {
			Long originalTimeToLive = cachedHttpService.getTimeToLive();
			cachedHttpService.setTimeToLive(null);
			T response = cachedHttpService.readFromCache(parser);
			if (response == null) {
				response = cachedHttpService.executeRequest(parser);
			} else {
				cachedHttpService.setTimeToLive(originalTimeToLive);
				ExecutorUtils.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
							// FIXME: the response is read an parsed again from cache when the cache is not expired
							CachingStrategy.CACHE_FIRST.execute(cachedHttpService, parser);
						} catch (Exception e) {
							LoggerUtils.logHandledException(LOGGER, e);
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
		public <T> T execute(CachedHttpService cachedHttpService, Parser parser) {
			return cachedHttpService.readFromCache(parser);
		}
	},
	
	// Execute the request first. If it fails try with the cache
	REMOTE_FIRST {
		
		@Override
		public <T> T execute(CachedHttpService cachedHttpService, Parser parser) {
			T response = null;
			try {
				response = cachedHttpService.executeRequest(parser);
			} catch (Exception e) {
				LoggerUtils.logHandledException(LOGGER, e);
			}
			if (response == null) {
				response = cachedHttpService.readFromCache(parser);
			}
			if (response == null && parser != null) {
				throw new UnexpectedException("The service was expecting a response, but it was null (CachingStrategy.REMOTE_FIRST)");
			}
			return response;
		}
	};
	
	private static final Logger LOGGER = LoggerUtils.getLogger(CachingStrategy.class);
	
	public abstract <T> T execute(CachedHttpService cachedHttpService, Parser parser);
}
