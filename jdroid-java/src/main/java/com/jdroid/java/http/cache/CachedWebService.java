package com.jdroid.java.http.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import org.slf4j.Logger;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.post.EntityEnclosingWebService;
import com.jdroid.java.parser.Parser;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.Hasher;
import com.jdroid.java.utils.LoggerUtils;

public abstract class CachedWebService implements WebService, EntityEnclosingWebService {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(CachedWebService.class);
	
	private WebService webService;
	private CachingStrategy cachingStrategy;
	private Long timeToLive;
	private Cache cache;
	
	public CachedWebService(WebService webService, Cache cache) {
		this(webService, cache, null, null);
	}
	
	public CachedWebService(WebService webService, Cache cache, CachingStrategy cachingStrategy) {
		this(webService, cache, cachingStrategy, null);
	}
	
	public CachedWebService(WebService webService, Cache cache, CachingStrategy cachingStrategy, Long timeToLive) {
		this.webService = webService;
		this.cache = cache;
		this.cachingStrategy = cachingStrategy != null ? cachingStrategy : CachingStrategy.NO_CACHE;
		this.timeToLive = timeToLive;
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#execute(com.jdroid.java.parser.Parser)
	 */
	@Override
	public <T> T execute(Parser parser) {
		LOGGER.debug("Executing cache strategy: " + cachingStrategy + " for url: " + getUrl());
		return cachingStrategy.execute(this, parser);
	}
	
	protected abstract File getHttpCacheDirectory(Cache cache);
	
	/**
	 * @see com.jdroid.java.http.WebService#execute()
	 */
	@Override
	public <T> T execute() {
		return webService.execute();
	}
	
	@SuppressWarnings({ "resource", "unchecked" })
	public <T> T readFromCache(Parser parser) {
		T response = null;
		File cacheFile = new File(getHttpCacheDirectory(cache), generateCacheFileName());
		if (cacheFile.exists()) {
			
			long diff = System.currentTimeMillis() - cacheFile.lastModified();
			if ((timeToLive == null) || ((diff >= 0) && (diff < timeToLive))) {
				FileInputStream fileInputStream = null;
				try {
					fileInputStream = new FileInputStream(cacheFile);
					response = (T)(parser.parse(fileInputStream));
					LOGGER.info("Reading http request from cache: " + cacheFile.getAbsolutePath());
				} catch (FileNotFoundException e) {
					LOGGER.warn("Error when opening cache file: " + cacheFile.getAbsolutePath(), e);
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
		return webService.execute(new CacheParser(parser, cacheFile));
	}
	
	protected String generateCacheFileName() {
		return generateCacheFileName(getUrlSuffix());
	}
	
	public static String generateCacheFileName(String key) {
		return Hasher.SHA_1.hash(key) + ".cache";
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value) {
		webService.addHeader(name, value);
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addQueryParameter(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addQueryParameter(String name, Object value) {
		webService.addQueryParameter(name, value);
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addQueryParameter(java.lang.String, java.util.Collection)
	 */
	@Override
	public void addQueryParameter(String name, Collection<?> values) {
		webService.addQueryParameter(name, values);
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addUrlSegment(java.lang.Object)
	 */
	@Override
	public void addUrlSegment(Object segment) {
		webService.addUrlSegment(segment);
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#addHttpWebServiceProcessor(com.jdroid.java.http.HttpWebServiceProcessor)
	 */
	@Override
	public void addHttpWebServiceProcessor(HttpWebServiceProcessor httpWebServiceProcessor) {
		webService.addHttpWebServiceProcessor(httpWebServiceProcessor);
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#setConnectionTimeout(java.lang.Integer)
	 */
	@Override
	public void setConnectionTimeout(Integer connectionTimeout) {
		webService.setConnectionTimeout(connectionTimeout);
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#setUserAgent(java.lang.String)
	 */
	@Override
	public void setUserAgent(String userAgent) {
		webService.setUserAgent(userAgent);
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#setSsl(java.lang.Boolean)
	 */
	@Override
	public void setSsl(Boolean ssl) {
		webService.setSsl(ssl);
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#getUrl()
	 */
	@Override
	public String getUrl() {
		return webService.getUrl();
	}
	
	/**
	 * @see com.jdroid.java.http.WebService#getUrlSuffix()
	 */
	@Override
	public String getUrlSuffix() {
		return webService.getUrlSuffix();
	}
	
	/**
	 * @see com.jdroid.java.http.post.EntityEnclosingWebService#setEntity(java.lang.String)
	 */
	@Override
	public void setEntity(String content) {
		((EntityEnclosingWebService)webService).setEntity(content);
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
	
	/**
	 * @param cachingStrategy the cachingStrategy to set
	 */
	public void setCachingStrategy(CachingStrategy cachingStrategy) {
		this.cachingStrategy = cachingStrategy;
	}
}
