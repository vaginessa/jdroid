package com.jdroid.java.http.urlconnection.put;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.MultipartWebService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.urlconnection.UrlConnectionEntityEnclosingWebService;

import java.io.ByteArrayInputStream;
import java.util.List;

public class PutUrlConnectionHttpWebService extends UrlConnectionEntityEnclosingWebService implements MultipartWebService {

	public PutUrlConnectionHttpWebService(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		super(server, urlSegments, httpWebServiceProcessors);
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.PUT;
	}

	@Override
	public void addPart(String name, ByteArrayInputStream in, String mimeType, String filename) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addPart(String name, Object value, String mimeType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addJsonPart(String name, Object value) {
		throw new UnsupportedOperationException();
	}
}
