package com.jdroid.java.http.okhttp;

import com.jdroid.java.http.AbstractHttpService;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.post.BodyEnclosingHttpService;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public abstract class OkBodyEnclosingHttpService extends OkHttpService implements BodyEnclosingHttpService {

	private String body;

	public OkBodyEnclosingHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	protected void onConfigureRequestBuilder(Request.Builder builder) {

		RequestBody requestBody = null;
		if (body != null) {
			AbstractHttpService.LOGGER.debug("Body: " + body);
			requestBody = RequestBody.create(MediaType.parse(MimeType.JSON), body);
		}
		onConfigureRequestBuilder(builder, requestBody);
	}

	protected abstract void onConfigureRequestBuilder(Request.Builder builder, RequestBody requestBody);

	@Override
	public void setBody(String body) {
		this.body = body;
	}
}
