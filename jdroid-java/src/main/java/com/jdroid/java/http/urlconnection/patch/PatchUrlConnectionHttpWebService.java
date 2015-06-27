package com.jdroid.java.http.urlconnection.patch;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.urlconnection.UrlConnectionEntityEnclosingWebService;

import java.util.List;

public class PatchUrlConnectionHttpWebService extends UrlConnectionEntityEnclosingWebService {

	public PatchUrlConnectionHttpWebService(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		super(server, urlSegments, httpWebServiceProcessors);
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.PATCH;
	}
}
