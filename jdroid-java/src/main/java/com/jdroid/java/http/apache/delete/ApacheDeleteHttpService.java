package com.jdroid.java.http.apache.delete;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.apache.ApacheHttpService;
import com.jdroid.java.http.apache.HttpClientFactory;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.List;

public class ApacheDeleteHttpService extends ApacheHttpService {
	
	public ApacheDeleteHttpService(HttpClientFactory httpClientFactory, Server server, List<Object> urlSegments,
								   List<HttpServiceProcessor> httpServiceProcessors) {
		super(httpClientFactory, server, urlSegments, httpServiceProcessors);
	}
	
	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.DELETE;
	}
	
	/**
	 * @see ApacheHttpService#createHttpUriRequest(java.lang.String)
	 */
	@Override
	protected HttpUriRequest createHttpUriRequest(String url) {
		return new HttpDelete(url);
	}
}
