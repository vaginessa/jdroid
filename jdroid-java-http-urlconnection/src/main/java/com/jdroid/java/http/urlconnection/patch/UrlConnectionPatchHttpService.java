package com.jdroid.java.http.urlconnection.patch;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.urlconnection.UrlConnectionBodyEnclosingHttpService;

import java.util.List;

public class UrlConnectionPatchHttpService extends UrlConnectionBodyEnclosingHttpService {

	public UrlConnectionPatchHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.PATCH;
	}
}
