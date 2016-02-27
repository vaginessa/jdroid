package com.jdroid.java.http.okhttp.patch;

import com.jdroid.java.http.HttpMethod;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.okhttp.OkBodyEnclosingHttpService;

import java.util.List;

import okhttp3.Request;
import okhttp3.RequestBody;

public class OkPatchHttpService extends OkBodyEnclosingHttpService {

	public OkPatchHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	protected void onConfigureRequestBuilder(Request.Builder builder, RequestBody requestBody) {
		builder.patch(requestBody);
	}

	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.PATCH;
	}
}
