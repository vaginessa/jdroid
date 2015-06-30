package com.jdroid.java.http.urlconnection.delete;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.urlconnection.UrlConnectionHttpService;

import java.util.List;

public class UrlConnectionDeleteHttpService extends UrlConnectionHttpService {

	public UrlConnectionDeleteHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.DELETE;
	}
}
