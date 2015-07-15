package com.jdroid.java.http.apache.put;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.apache.ApacheBodyEnclosingHttpService;
import com.jdroid.java.http.apache.HttpClientFactory;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;

import java.util.List;

public class ApachePutHttpService extends ApacheBodyEnclosingHttpService {
	
	public ApachePutHttpService(HttpClientFactory httpClientFactory, Server server, List<Object> urlSegments,
								List<HttpServiceProcessor> httpServiceProcessors) {
		super(httpClientFactory, server, urlSegments, httpServiceProcessors);
	}
	
	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.PUT;
	}
	
	/**
	 * @see ApacheBodyEnclosingHttpService#createHttpEntityEnclosingRequestBase(java.lang.String)
	 */
	@Override
	protected HttpEntityEnclosingRequestBase createHttpEntityEnclosingRequestBase(String uri) {
		return new HttpPut(uri);
	}
}
