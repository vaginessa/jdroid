package com.jdroid.java.http.urlconnection.get;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.urlconnection.UrlConnectionHttpWebService;

import java.util.List;

public class GetUrlConnectionHttpWebService extends UrlConnectionHttpWebService {

	public GetUrlConnectionHttpWebService(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		super(server, urlSegments, httpWebServiceProcessors);
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.GET;
	}
}
