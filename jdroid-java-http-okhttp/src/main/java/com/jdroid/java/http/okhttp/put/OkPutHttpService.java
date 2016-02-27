package com.jdroid.java.http.okhttp.put;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.MultipartHttpService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.okhttp.OkBodyEnclosingHttpService;

import java.io.ByteArrayInputStream;
import java.util.List;

import okhttp3.Request;
import okhttp3.RequestBody;

public class OkPutHttpService extends OkBodyEnclosingHttpService implements MultipartHttpService {

	public OkPutHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	protected void onConfigureRequestBuilder(Request.Builder builder, RequestBody requestBody) {
		builder.put(requestBody);
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
