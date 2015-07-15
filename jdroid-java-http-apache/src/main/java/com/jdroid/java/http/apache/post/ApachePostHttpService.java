package com.jdroid.java.http.apache.post;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.apache.ApacheBodyEnclosingHttpService;
import com.jdroid.java.http.apache.HttpClientFactory;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;

import java.util.List;

public class ApachePostHttpService extends ApacheBodyEnclosingHttpService {
	
	public ApachePostHttpService(HttpClientFactory httpClientFactory, Server server, List<Object> urlSegments,
								 List<HttpServiceProcessor> httpServiceProcessors) {
		super(httpClientFactory, server, urlSegments, httpServiceProcessors);
	}
	
	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.POST;
	}
	
	/**
	 * @see ApacheBodyEnclosingHttpService#createHttpEntityEnclosingRequestBase(java.lang.String)
	 */
	@Override
	protected HttpEntityEnclosingRequestBase createHttpEntityEnclosingRequestBase(String uri) {
		return new HttpPost(uri);
	}
}
