package com.jdroid.java.http.cache;

import com.jdroid.java.date.DateUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.parser.Parser;
import com.jdroid.java.http.post.BodyEnclosingHttpService;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.Hasher;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

public abstract class CachedHttpService implements BodyEnclosingHttpService {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(CachedHttpService.class);
	
	private HttpService httpService;
	private CachingStrategy cachingStrategy;
	private Long timeToLive;
	private Cache cache;
	
	public CachedHttpService(HttpService httpService, Cache cache) {
		this(httpService, cache, null, null);
	}
	
	public CachedHttpService(HttpService httpService, Cache cache, CachingStrategy cachingStrategy) {
		this(httpService, cache, cachingStrategy, null);
	}
	
	public CachedHttpService(HttpService httpService, Cache cache, CachingStrategy cachingStrategy, Long timeToLive) {
		this.httpService = httpService;
		this.cache = cache;
		this.cachingStrategy = cachingStrategy != null ? cachingStrategy : CachingStrategy.NO_CACHE;
		this.timeToLive = timeToLive;
	}
	
	/**
	 * @see HttpService#execute(Parser)
	 */
	@Override
	public <T> T execute(Parser parser) {
		LOGGER.debug("Executing cache strategy: " + cachingStrategy + " for url: " + getUrl());
		return cachingStrategy.execute(this, parser);
	}
	
	protected abstract File getHttpCacheDirectory(Cache cache);
	
	@Override
	public void execute() {
		httpService.execute();
	}
	
	@SuppressWarnings({ "resource", "unchecked" })
	public <T> T readFromCache(Parser parser) {
		T response = null;
		File cacheFile = new File(getHttpCacheDirectory(cache), generateCacheFileName());
		if (cacheFile.exists() && (cacheFile.length() > 0)) {
			
			long diff = DateUtils.nowMillis() - cacheFile.lastModified();
			if ((timeToLive == null) || ((diff >= 0) && (diff < timeToLive))) {
				FileInputStream fileInputStream = null;
				try {
					fileInputStream = new FileInputStream(cacheFile);
					response = (T)(parser.parse(fileInputStream));
					LOGGER.info("Reading http request from cache: " + cacheFile.getAbsolutePath());
				} catch (FileNotFoundException e) {
					LoggerUtils.logHandledException(LOGGER, new UnexpectedException("Error when opening cache file: " + cacheFile.getAbsolutePath(), e));
				} catch (Exception e) {
					LoggerUtils.logHandledException(LOGGER, e);
				} finally {
					FileUtils.safeClose(fileInputStream);
				}
			}
		} else {
			LOGGER.info("Http request not present on cache: " + cacheFile.getAbsolutePath());
		}
		return response;
	}
	
	public <T> T executeRequest(Parser parser) {
		String cacheFileName = generateCacheFileName();
		File cacheFile = new File(getHttpCacheDirectory(cache), cacheFileName);
		
		// TODO Se if we should save the cache when the request fails
		return httpService.execute(new CacheParser(parser, cacheFile));
	}
	
	protected String generateCacheFileName() {
		return generateCacheFileName(getUrlSuffix());
	}
	
	public static String generateCacheFileName(String key) {
		return Hasher.SHA_1.hash(key) + ".cache";
	}
	
	/**
	 * @see HttpService#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value) {
		httpService.addHeader(name, value);
	}
	
	/**
	 * @see HttpService#addQueryParameter(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addQueryParameter(String name, Object value) {
		httpService.addQueryParameter(name, value);
	}
	
	/**
	 * @see HttpService#addQueryParameter(java.lang.String, java.util.Collection)
	 */
	@Override
	public void addQueryParameter(String name, Collection<?> values) {
		httpService.addQueryParameter(name, values);
	}
	
	/**
	 * @see HttpService#addUrlSegment(java.lang.Object)
	 */
	@Override
	public void addUrlSegment(Object segment) {
		httpService.addUrlSegment(segment);
	}
	
	/**
	 * @see HttpService#addHttpServiceProcessor(HttpServiceProcessor)
	 */
	@Override
	public void addHttpServiceProcessor(HttpServiceProcessor httpServiceProcessor) {
		httpService.addHttpServiceProcessor(httpServiceProcessor);
	}
	
	/**
	 * @see HttpService#setConnectionTimeout(java.lang.Integer)
	 */
	@Override
	public void setConnectionTimeout(Integer connectionTimeout) {
		httpService.setConnectionTimeout(connectionTimeout);
	}

	@Override
	public void setReadTimeout(Integer readTimeout) {
		httpService.setReadTimeout(readTimeout);
	}

	@Override
	public void setWriteTimeout(Integer writeTimeout) {
		httpService.setWriteTimeout(writeTimeout);
	}

	/**
	 * @see HttpService#setUserAgent(java.lang.String)
	 */
	@Override
	public void setUserAgent(String userAgent) {
		httpService.setUserAgent(userAgent);
	}
	
	/**
	 * @see HttpService#setSsl(java.lang.Boolean)
	 */
	@Override
	public void setSsl(Boolean ssl) {
		httpService.setSsl(ssl);
	}
	
	/**
	 * @see HttpService#getUrl()
	 */
	@Override
	public String getUrl() {
		return httpService.getUrl();
	}
	
	/**
	 * @see HttpService#getUrlSuffix()
	 */
	@Override
	public String getUrlSuffix() {
		return httpService.getUrlSuffix();
	}
	
	/**
	 * @see BodyEnclosingHttpService#setBody(String)
	 */
	@Override
	public void setBody(String body) {
		((BodyEnclosingHttpService)httpService).setBody(body);
	}
	
	/**
	 * @param timeToLive the timeToLive to set
	 */
	public void setTimeToLive(Long timeToLive) {
		this.timeToLive = timeToLive;
	}
	
	/**
	 * @return the timeToLive
	 */
	public Long getTimeToLive() {
		return timeToLive;
	}

	@Override
	public String getHeaderValue(String key) {
		return httpService.getHeaderValue(key);
	}

	@Override
	public HttpResponseWrapper getHttpResponseWrapper() {
		return httpService.getHttpResponseWrapper();
	}
}
