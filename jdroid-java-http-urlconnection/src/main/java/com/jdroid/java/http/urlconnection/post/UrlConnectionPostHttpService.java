package com.jdroid.java.http.urlconnection.post;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.MultipartHttpService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.urlconnection.UrlConnectionBodyEnclosingHttpService;

import java.io.ByteArrayInputStream;
import java.util.List;

public class UrlConnectionPostHttpService extends UrlConnectionBodyEnclosingHttpService implements MultipartHttpService {

	public UrlConnectionPostHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.POST;
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
