package com.jdroid.java.http.okhttp.get;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.okhttp.OkHttpService;
import com.squareup.okhttp.Request;

import java.util.List;

public class OkGetHttpService extends OkHttpService {

	public OkGetHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	protected void onConfigureRequestBuilder(Request.Builder builder) {
		builder.get();
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.GET;
	}
}
