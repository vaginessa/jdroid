package com.jdroid.java.http.urlconnection.patch;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.urlconnection.UrlConnectionEntityEnclosingHttpService;

import java.util.List;

public class UrlConnectionPatchHttpService extends UrlConnectionEntityEnclosingHttpService {

	public UrlConnectionPatchHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.PATCH;
	}
}
