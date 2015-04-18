package com.jdroid.java.http.apache.post;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.apache.ApacheHttpEntityEnclosingWebService;
import com.jdroid.java.http.apache.HttpClientFactory;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;

import java.util.List;

public class ApacheHttpPostWebService extends ApacheHttpEntityEnclosingWebService {
	
	public ApacheHttpPostWebService(HttpClientFactory httpClientFactory, Server server, List<Object> urlSegments,
			List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		super(httpClientFactory, server, urlSegments, httpWebServiceProcessors);
	}
	
	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.POST;
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpEntityEnclosingWebService#createHttpEntityEnclosingRequestBase(java.lang.String)
	 */
	@Override
	protected HttpEntityEnclosingRequestBase createHttpEntityEnclosingRequestBase(String uri) {
		return new HttpPost(uri);
	}
}
