package com.jdroid.java.http.apache.put;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.apache.ApacheHttpEntityEnclosingWebService;
import com.jdroid.java.http.apache.HttpClientFactory;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;

import java.util.List;

public class ApacheHttpPutWebService extends ApacheHttpEntityEnclosingWebService {
	
	public ApacheHttpPutWebService(HttpClientFactory httpClientFactory, Server server, List<Object> urlSegments,
			List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		super(httpClientFactory, server, urlSegments, httpWebServiceProcessors);
	}
	
	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.PUT;
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpEntityEnclosingWebService#createHttpEntityEnclosingRequestBase(java.lang.String)
	 */
	@Override
	protected HttpEntityEnclosingRequestBase createHttpEntityEnclosingRequestBase(String uri) {
		return new HttpPut(uri);
	}
}
