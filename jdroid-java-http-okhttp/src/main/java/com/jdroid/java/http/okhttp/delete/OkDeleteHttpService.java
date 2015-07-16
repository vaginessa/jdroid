package com.jdroid.java.http.okhttp.delete;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.okhttp.OkHttpService;
import com.squareup.okhttp.Request;

import java.util.List;

public class OkDeleteHttpService extends OkHttpService {

	public OkDeleteHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	protected void onConfigureRequestBuilder(Request.Builder builder) {
		builder.delete();
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.DELETE;
	}
}
