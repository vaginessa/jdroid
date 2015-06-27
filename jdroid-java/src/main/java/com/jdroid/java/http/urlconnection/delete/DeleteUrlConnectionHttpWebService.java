package com.jdroid.java.http.urlconnection.delete;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.urlconnection.UrlConnectionHttpWebService;

import java.util.List;

public class DeleteUrlConnectionHttpWebService extends UrlConnectionHttpWebService {

	public DeleteUrlConnectionHttpWebService(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		super(server, urlSegments, httpWebServiceProcessors);
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.DELETE;
	}
}
