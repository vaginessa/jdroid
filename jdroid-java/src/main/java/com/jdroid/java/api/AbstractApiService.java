package com.jdroid.java.api;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.MultipartWebService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.cache.Cache;
import com.jdroid.java.http.cache.CachedWebService;
import com.jdroid.java.http.cache.CachingStrategy;
import com.jdroid.java.http.mock.AbstractMockWebService;
import com.jdroid.java.http.post.EntityEnclosingWebService;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.java.marshaller.MarshallerProvider;

import java.io.File;
import java.util.List;
import java.util.Map;

public abstract class AbstractApiService {
	
	// GET
	
	protected WebService newGetService(Object... urlSegments) {
		return newGetService(false, urlSegments);
	}

	protected WebService newGetService(List<HttpWebServiceProcessor> processors, Object... urlSegments) {
		return newGetService(false, processors, urlSegments);
	}
	
	protected WebService newGetService(Boolean mocked, Object... urlSegments) {
		return newGetService(mocked, getHttpWebServiceProcessors(), urlSegments);
	}

	protected WebService newGetService(Boolean mocked, List<HttpWebServiceProcessor> processors, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newGetServiceImpl(getServer(), Lists.newArrayList(urlSegments), processors);
		}
	}

	protected abstract WebService newGetServiceImpl(Server server, List<Object> urlSegments,
			List<HttpWebServiceProcessor> httpWebServiceProcessors);

	protected WebService newCachedGetService(Cache cache, CachingStrategy cachingStrategy, Long timeToLive,
											 List<HttpWebServiceProcessor> processors, Object... urlSegments) {
		return newCachedGetService(newGetServiceImpl(getServer(), Lists.newArrayList(urlSegments), processors), cache,
				cachingStrategy, timeToLive);
	}

	protected WebService newCachedGetService(Cache cache, CachingStrategy cachingStrategy, Long timeToLive,
											 Object... urlSegments) {
		return newCachedGetService(newGetService(urlSegments), cache, cachingStrategy, timeToLive);
	}

	private WebService newCachedGetService(WebService webService, Cache cache, CachingStrategy cachingStrategy,
										   Long timeToLive) {
		return newCachedWebService(webService, cache, cachingStrategy, timeToLive);
	}
	
	// POST
	
	protected EntityEnclosingWebService newPostService(Object... urlSegments) {
		return newPostService(false, urlSegments);
	}
	
	protected EntityEnclosingWebService newPostService(Boolean mocked, Object... urlSegments) {
		return newPostService(mocked, getHttpWebServiceProcessors(), urlSegments);
	}

	protected EntityEnclosingWebService newPostService(Boolean mocked, List<HttpWebServiceProcessor> processors, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newPostServiceImpl(getServer(), Lists.newArrayList(urlSegments), processors);
		}
	}

	protected abstract EntityEnclosingWebService newPostServiceImpl(Server server, List<Object> urlSegments,
			List<HttpWebServiceProcessor> httpWebServiceProcessors);

	// POST MULTIPART
	
	protected MultipartWebService newMultipartPostService(Object... urlSegments) {
		return newMultipartPostService(false, urlSegments);
	}

	protected MultipartWebService newMultipartPostService(Boolean mocked, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newMultipartPostServiceImpl(getServer(), Lists.newArrayList(urlSegments),
				getHttpWebServiceProcessors());
		}
	}
	
	protected abstract MultipartWebService newMultipartPostServiceImpl(Server server, List<Object> urlSegments,
			List<HttpWebServiceProcessor> httpWebServiceProcessors);

	// POST FORM
	
	protected EntityEnclosingWebService newFormPostService(Object... urlSegments) {
		return newFormPostService(false, urlSegments);
	}
	
	protected EntityEnclosingWebService newFormPostService(Boolean mocked, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newFormPostServiceImpl(getServer(), Lists.newArrayList(urlSegments), getHttpWebServiceProcessors());
		}
	}
	
	protected abstract EntityEnclosingWebService newFormPostServiceImpl(Server server, List<Object> urlSegments,
			List<HttpWebServiceProcessor> httpWebServiceProcessors);
	
	// PUT
	
	protected EntityEnclosingWebService newPutService(Object... urlSegments) {
		return newPutService(false, urlSegments);
	}

	protected EntityEnclosingWebService newPutService(Boolean mocked, Object... urlSegments) {
		return newPutService(mocked, getHttpWebServiceProcessors(), urlSegments);
	}

	protected EntityEnclosingWebService newPutService(Boolean mocked, List<HttpWebServiceProcessor> processors, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newPutServiceImpl(getServer(), Lists.newArrayList(urlSegments), processors);
		}
	}

	protected abstract EntityEnclosingWebService newPutServiceImpl(Server server, List<Object> urlSegments,
																	List<HttpWebServiceProcessor> httpWebServiceProcessors);

	protected EntityEnclosingWebService newCachedPutService(Cache cache, CachingStrategy cachingStrategy,
															Long timeToLive, Object... urlSegments) {
		WebService webService = newPutService(urlSegments);
		return newCachedWebService(webService, cache, cachingStrategy, timeToLive);
	}

	// PUT MULTIPART
	
	protected MultipartWebService newMultipartPutService(Object... urlSegments) {
		return newMultipartPutService(false, urlSegments);
	}
	
	protected MultipartWebService newMultipartPutService(Boolean mocked, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newMultipartPutServiceImpl(getServer(), Lists.newArrayList(urlSegments),
				getHttpWebServiceProcessors());
		}
	}
	
	protected abstract MultipartWebService newMultipartPutServiceImpl(Server server, List<Object> urlSegments,
			List<HttpWebServiceProcessor> httpWebServiceProcessors);
	
	// DELETE
	
	protected WebService newDeleteService(Object... urlSegments) {
		return newDeleteService(false, urlSegments);
	}

	protected WebService newDeleteService(List<HttpWebServiceProcessor> processors, Object... urlSegments) {
		return newDeleteService(false, processors, urlSegments);
	}

	protected WebService newDeleteService(Boolean mocked, Object... urlSegments) {
		return newDeleteService(mocked, getHttpWebServiceProcessors(), urlSegments);
	}

	protected WebService newDeleteService(Boolean mocked, List<HttpWebServiceProcessor> processors, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newDeleteServiceImpl(getServer(), Lists.newArrayList(urlSegments), processors);
		}
	}

	protected abstract WebService newDeleteServiceImpl(Server server, List<Object> urlSegments,
													List<HttpWebServiceProcessor> httpWebServiceProcessors);

	protected WebService newCachedDeleteService(Cache cache, CachingStrategy cachingStrategy, Long timeToLive,
												Object... urlSegments) {
		WebService webService = newDeleteService(urlSegments);
		return newCachedWebService(webService, cache, cachingStrategy, timeToLive);
	}
	
	// PATCH
	
	protected EntityEnclosingWebService newPatchService(Object... urlSegments) {
		return newPatchService(false, urlSegments);
	}

	protected EntityEnclosingWebService newPatchService(Boolean mocked, Object... urlSegments) {
		return newPatchService(mocked, getHttpWebServiceProcessors(), urlSegments);
	}

	protected EntityEnclosingWebService newPatchService(Boolean mocked, List<HttpWebServiceProcessor> processors, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newPatchServiceImpl(getServer(), Lists.newArrayList(urlSegments), processors);
		}
	}

	protected abstract EntityEnclosingWebService newPatchServiceImpl(Server server, List<Object> urlSegments,
																   List<HttpWebServiceProcessor> httpWebServiceProcessors);

	protected EntityEnclosingWebService newCachedPatchService(Cache cache, CachingStrategy cachingStrategy,
			Long timeToLive, Object... urlSegments) {
		WebService webService = newPutService(urlSegments);
		return newCachedWebService(webService, cache, cachingStrategy, timeToLive);
	}

	/////////////

	protected CachedWebService newCachedWebService(WebService webService, Cache cache, CachingStrategy cachingStrategy, Long timeToLive) {
		return new CachedWebService(webService, cache, cachingStrategy, timeToLive) {

			@Override
			protected File getHttpCacheDirectory(Cache cache) {
				return AbstractApiService.this.getHttpCacheDirectory(cache);
			}
		};
	}

	protected abstract Server getServer();
	
	protected List<HttpWebServiceProcessor> getHttpWebServiceProcessors() {
		return getServer().getHttpWebServiceProcessors();
	}
	
	protected abstract AbstractMockWebService getAbstractMockWebServiceInstance(Object... urlSegments);
	
	protected abstract Boolean isHttpMockEnabled();
	
	protected File getHttpCacheDirectory(Cache cache) {
		return null;
	}
	
	public void marshallSimple(EntityEnclosingWebService webservice, Object object) {
		marshall(webservice, object, MarshallerMode.SIMPLE);
	}
	
	public void marshall(EntityEnclosingWebService webservice, Object object) {
		marshall(webservice, object, MarshallerMode.COMPLETE);
	}
	
	public void marshall(EntityEnclosingWebService webservice, Object object, MarshallerMode mode) {
		marshall(webservice, object, mode, null);
	}
	
	public void marshall(EntityEnclosingWebService webservice, Object object, Map<String, String> extras) {
		marshall(webservice, object, MarshallerMode.COMPLETE, extras);
	}
	
	public void marshall(EntityEnclosingWebService webservice, Object object, MarshallerMode mode,
			Map<String, String> extras) {
		webservice.setEntity(MarshallerProvider.get().marshall(object, mode, extras).toString());
	}
}
