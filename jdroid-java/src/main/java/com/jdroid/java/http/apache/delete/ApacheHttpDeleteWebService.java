package com.jdroid.java.http.apache.delete;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.apache.ApacheHttpWebService;
import com.jdroid.java.http.apache.HttpClientFactory;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.List;

public class ApacheHttpDeleteWebService extends ApacheHttpWebService {
	
	public ApacheHttpDeleteWebService(HttpClientFactory httpClientFactory, Server server, List<Object> urlSegments,
			List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		super(httpClientFactory, server, urlSegments, httpWebServiceProcessors);
	}
	
	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.DELETE;
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpWebService#createHttpUriRequest(java.lang.String)
	 */
	@Override
	protected HttpUriRequest createHttpUriRequest(String url) {
		return new HttpDelete(url);
	}
}
