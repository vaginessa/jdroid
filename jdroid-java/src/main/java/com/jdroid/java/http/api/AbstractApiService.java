package com.jdroid.java.http.api;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceFactory;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.MultipartHttpService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.cache.Cache;
import com.jdroid.java.http.cache.CachedHttpService;
import com.jdroid.java.http.cache.CachingStrategy;
import com.jdroid.java.http.mock.AbstractMockHttpService;
import com.jdroid.java.http.post.BodyEnclosingHttpService;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.java.marshaller.MarshallerProvider;
import com.jdroid.java.utils.ReflectionUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

public abstract class AbstractApiService {

	private HttpServiceFactory httpServiceFactory;

	public AbstractApiService() {
		httpServiceFactory = createHttpServiceFactory();
	}
	
	// GET
	
	protected HttpService newGetService(Object... urlSegments) {
		return newGetService(false, urlSegments);
	}

	protected HttpService newGetService(List<HttpServiceProcessor> processors, Object... urlSegments) {
		return newGetService(false, processors, urlSegments);
	}
	
	protected HttpService newGetService(Boolean mocked, Object... urlSegments) {
		return newGetService(mocked, getHttpServiceProcessors(), urlSegments);
	}

	protected HttpService newGetService(Boolean mocked, List<HttpServiceProcessor> processors, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockHttpServiceInstance(urlSegments);
		} else {
			return httpServiceFactory.newGetService(getServer(), Lists.newArrayList(urlSegments), processors);
		}
	}

	protected HttpService newCachedGetService(Cache cache, CachingStrategy cachingStrategy, Long timeToLive,
											 List<HttpServiceProcessor> processors, Object... urlSegments) {
		return newCachedGetService(httpServiceFactory.newGetService(getServer(), Lists.newArrayList(urlSegments), processors), cache,
				cachingStrategy, timeToLive);
	}

	protected HttpService newCachedGetService(Cache cache, CachingStrategy cachingStrategy, Long timeToLive,
											 Object... urlSegments) {
		return newCachedGetService(newGetService(urlSegments), cache, cachingStrategy, timeToLive);
	}

	private HttpService newCachedGetService(HttpService httpService, Cache cache, CachingStrategy cachingStrategy,
										   Long timeToLive) {
		return newCachedhttpService(httpService, cache, cachingStrategy, timeToLive);
	}
	
	// POST
	
	protected BodyEnclosingHttpService newPostService(Object... urlSegments) {
		return newPostService(false, urlSegments);
	}
	
	protected BodyEnclosingHttpService newPostService(Boolean mocked, Object... urlSegments) {
		return newPostService(mocked, getHttpServiceProcessors(), urlSegments);
	}

	protected BodyEnclosingHttpService newPostService(Boolean mocked, List<HttpServiceProcessor> processors, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockHttpServiceInstance(urlSegments);
		} else {
			return httpServiceFactory.newPostService(getServer(), Lists.newArrayList(urlSegments), processors);
		}
	}

	// POST MULTIPART
	
	protected MultipartHttpService newMultipartPostService(Object... urlSegments) {
		return newMultipartPostService(false, urlSegments);
	}

	protected MultipartHttpService newMultipartPostService(Boolean mocked, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockHttpServiceInstance(urlSegments);
		} else {
			return httpServiceFactory.newMultipartPostService(getServer(), Lists.newArrayList(urlSegments),
					getHttpServiceProcessors());
		}
	}
	
	// POST FORM
	
	protected BodyEnclosingHttpService newFormPostService(Object... urlSegments) {
		return newFormPostService(false, urlSegments);
	}
	
	protected BodyEnclosingHttpService newFormPostService(Boolean mocked, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockHttpServiceInstance(urlSegments);
		} else {
			return httpServiceFactory.newFormPostService(getServer(), Lists.newArrayList(urlSegments), getHttpServiceProcessors());
		}
	}
	
	// PUT
	
	protected BodyEnclosingHttpService newPutService(Object... urlSegments) {
		return newPutService(false, urlSegments);
	}

	protected BodyEnclosingHttpService newPutService(Boolean mocked, Object... urlSegments) {
		return newPutService(mocked, getHttpServiceProcessors(), urlSegments);
	}

	protected BodyEnclosingHttpService newPutService(Boolean mocked, List<HttpServiceProcessor> processors, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockHttpServiceInstance(urlSegments);
		} else {
			return httpServiceFactory.newPutService(getServer(), Lists.newArrayList(urlSegments), processors);
		}
	}

	protected BodyEnclosingHttpService newCachedPutService(Cache cache, CachingStrategy cachingStrategy,
															Long timeToLive, Object... urlSegments) {
		HttpService httpService = newPutService(urlSegments);
		return newCachedhttpService(httpService, cache, cachingStrategy, timeToLive);
	}

	// PUT MULTIPART
	
	protected MultipartHttpService newMultipartPutService(Object... urlSegments) {
		return newMultipartPutService(false, urlSegments);
	}
	
	protected MultipartHttpService newMultipartPutService(Boolean mocked, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockHttpServiceInstance(urlSegments);
		} else {
			return httpServiceFactory.newMultipartPutService(getServer(), Lists.newArrayList(urlSegments),
					getHttpServiceProcessors());
		}
	}
	
	// DELETE
	
	protected HttpService newDeleteService(Object... urlSegments) {
		return newDeleteService(false, urlSegments);
	}

	protected HttpService newDeleteService(List<HttpServiceProcessor> processors, Object... urlSegments) {
		return newDeleteService(false, processors, urlSegments);
	}

	protected HttpService newDeleteService(Boolean mocked, Object... urlSegments) {
		return newDeleteService(mocked, getHttpServiceProcessors(), urlSegments);
	}

	protected HttpService newDeleteService(Boolean mocked, List<HttpServiceProcessor> processors, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockHttpServiceInstance(urlSegments);
		} else {
			return httpServiceFactory.newDeleteService(getServer(), Lists.newArrayList(urlSegments), processors);
		}
	}

	protected HttpService newCachedDeleteService(Cache cache, CachingStrategy cachingStrategy, Long timeToLive,
												Object... urlSegments) {
		HttpService httpService = newDeleteService(urlSegments);
		return newCachedhttpService(httpService, cache, cachingStrategy, timeToLive);
	}
	
	// PATCH
	
	protected BodyEnclosingHttpService newPatchService(Object... urlSegments) {
		return newPatchService(false, urlSegments);
	}

	protected BodyEnclosingHttpService newPatchService(Boolean mocked, Object... urlSegments) {
		return newPatchService(mocked, getHttpServiceProcessors(), urlSegments);
	}

	protected BodyEnclosingHttpService newPatchService(Boolean mocked, List<HttpServiceProcessor> processors, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockHttpServiceInstance(urlSegments);
		} else {
			return httpServiceFactory.newPatchService(getServer(), Lists.newArrayList(urlSegments), processors);
		}
	}

	protected BodyEnclosingHttpService newCachedPatchService(Cache cache, CachingStrategy cachingStrategy,
			Long timeToLive, Object... urlSegments) {
		HttpService httpService = newPatchService(urlSegments);
		return newCachedhttpService(httpService, cache, cachingStrategy, timeToLive);
	}

	/////////////

	protected CachedHttpService newCachedhttpService(HttpService httpService, Cache cache, CachingStrategy cachingStrategy, Long timeToLive) {
		return new CachedHttpService(httpService, cache, cachingStrategy, timeToLive) {

			@Override
			protected File getHttpCacheDirectory(Cache cache) {
				return AbstractApiService.this.getHttpCacheDirectory(cache);
			}
		};
	}

	protected HttpServiceFactory createHttpServiceFactory() {
		HttpServiceFactory httpServiceFactory = createFactory("com.jdroid.java.http.okhttp.OkHttpServiceFactory");
		if (httpServiceFactory == null) {
			httpServiceFactory  = createFactory("com.jdroid.java.http.apache.ApacheHttpServiceFactory");
			if (httpServiceFactory == null) {
				httpServiceFactory  = createFactory("com.jdroid.java.http.urlconnection.UrlConnectionHttpServiceFactory");
			}
		}
		return httpServiceFactory;
	}

	private HttpServiceFactory createFactory(String className) {
		try {
			return ReflectionUtils.newInstance(className);
		} catch (UnexpectedException e) {
			return null;
		}
	}

	protected abstract Server getServer();
	
	protected List<HttpServiceProcessor> getHttpServiceProcessors() {
		return getServer().getHttpServiceProcessors();
	}
	
	protected abstract AbstractMockHttpService getAbstractMockHttpServiceInstance(Object... urlSegments);
	
	protected abstract Boolean isHttpMockEnabled();
	
	protected File getHttpCacheDirectory(Cache cache) {
		return null;
	}
	
	protected void marshallSimple(BodyEnclosingHttpService httpService, Object object) {
		marshall(httpService, object, MarshallerMode.SIMPLE);
	}
	
	protected void marshall(BodyEnclosingHttpService httpService, Object object) {
		marshall(httpService, object, MarshallerMode.COMPLETE);
	}
	
	protected void marshall(BodyEnclosingHttpService httpService, Object object, MarshallerMode mode) {
		marshall(httpService, object, mode, null);
	}
	
	protected void marshall(BodyEnclosingHttpService httpService, Object object, Map<String, String> extras) {
		marshall(httpService, object, MarshallerMode.COMPLETE, extras);
	}
	
	protected void marshall(BodyEnclosingHttpService httpService, Object object, MarshallerMode mode,
			Map<String, String> extras) {
		httpService.setBody(MarshallerProvider.get().marshall(object, mode, extras).toString());
	}
}
