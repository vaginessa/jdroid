package com.jdroid.java.http.apache.patch;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.apache.ApacheHttpEntityEnclosingWebService;
import com.jdroid.java.http.apache.HttpClientFactory;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.util.List;

public class ApacheHttpPatchWebService extends ApacheHttpEntityEnclosingWebService {
	
	public ApacheHttpPatchWebService(HttpClientFactory httpClientFactory, Server baseURL, List<Object> urlSegments,
			List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		super(httpClientFactory, baseURL, urlSegments, httpWebServiceProcessors);
	}
	
	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.PATCH;
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpEntityEnclosingWebService#createHttpEntityEnclosingRequestBase(java.lang.String)
	 */
	@Override
	protected HttpEntityEnclosingRequestBase createHttpEntityEnclosingRequestBase(String uri) {
		return new HttpPatch(uri);
	}
}
