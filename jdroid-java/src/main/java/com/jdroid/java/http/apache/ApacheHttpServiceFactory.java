package com.jdroid.java.http.apache;

import com.jdroid.java.http.HttpServiceFactory;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.MultipartHttpService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.apache.DefaultHttpClientFactory;
import com.jdroid.java.http.apache.HttpClientFactory;
import com.jdroid.java.http.apache.delete.ApacheDeleteHttpService;
import com.jdroid.java.http.apache.get.ApacheGetHttpService;
import com.jdroid.java.http.apache.patch.ApachePatchHttpService;
import com.jdroid.java.http.apache.post.ApacheFormPostHttpService;
import com.jdroid.java.http.apache.post.ApachePostHttpService;
import com.jdroid.java.http.apache.post.ApacheMultipartPostHttpService;
import com.jdroid.java.http.apache.put.ApachePutHttpService;
import com.jdroid.java.http.apache.put.ApacheMultipartPutHttpService;
import com.jdroid.java.http.post.EntityEnclosingHttpService;

import java.util.List;

public class ApacheHttpServiceFactory implements HttpServiceFactory {
	
	@Override
	public HttpService newGetService(Server server, List<Object> urlSegments,
										   List<HttpServiceProcessor> httpServiceProcessors) {
		return new ApacheGetHttpService(getHttpClientFactoryInstance(), server, urlSegments,
				httpServiceProcessors);
	}
	
	@Override
	public EntityEnclosingHttpService newPostService(Server server, List<Object> urlSegments,
														   List<HttpServiceProcessor> httpServiceProcessors) {
		return new ApachePostHttpService(getHttpClientFactoryInstance(), server, urlSegments,
				httpServiceProcessors);
	}
	
	@Override
	public EntityEnclosingHttpService newPutService(Server server, List<Object> urlSegments,
														  List<HttpServiceProcessor> httpServiceProcessors) {
		return new ApachePutHttpService(getHttpClientFactoryInstance(), server, urlSegments,
				httpServiceProcessors);
	}
	
	@Override
	public MultipartHttpService newMultipartPutService(Server server, List<Object> urlSegments,
															 List<HttpServiceProcessor> httpServiceProcessors) {
		return new ApacheMultipartPutHttpService(getHttpClientFactoryInstance(), server, urlSegments,
				httpServiceProcessors);
	}
	
	@Override
	public MultipartHttpService newMultipartPostService(Server server, List<Object> urlSegments,
															  List<HttpServiceProcessor> httpServiceProcessors) {
		return new ApacheMultipartPostHttpService(getHttpClientFactoryInstance(), server, urlSegments,
				httpServiceProcessors);
	}
	
	@Override
	public HttpService newDeleteService(Server server, List<Object> urlSegments,
											  List<HttpServiceProcessor> httpServiceProcessors) {
		return new ApacheDeleteHttpService(getHttpClientFactoryInstance(), server, urlSegments,
				httpServiceProcessors);
	}
	
	@Override
	public EntityEnclosingHttpService newFormPostService(Server server, List<Object> urlSegments,
															   List<HttpServiceProcessor> httpServiceProcessors) {
		return new ApacheFormPostHttpService(getHttpClientFactoryInstance(), server, urlSegments,
				httpServiceProcessors);
	}
	
	@Override
	public EntityEnclosingHttpService newPatchService(Server baseURL, List<Object> urlSegments,
															List<HttpServiceProcessor> httpServiceProcessors) {
		return new ApachePatchHttpService(getHttpClientFactoryInstance(), baseURL, urlSegments,
				httpServiceProcessors);
	}
	
	protected HttpClientFactory getHttpClientFactoryInstance() {
		return DefaultHttpClientFactory.get();
	}
}
