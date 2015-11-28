package com.jdroid.java.http.okhttp;

import com.jdroid.java.http.AbstractHttpService;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.utils.StringUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class OkHttpService extends AbstractHttpService {

	static {
		// This is to avoid exceptions like "java.net.ProtocolException: Unexpected status line:..." that occurs in some
		// cases when HTTP connections are reused. This issue, according to the tests carried out, seems to happen in the app
		// only in some cases when a connection is reused after a response code 204.
		System.setProperty("http.keepAlive", "false");
	}

	protected OkHttpClient client;
	protected Request request;

	public OkHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	protected HttpResponseWrapper doExecute(String urlString) {

		client = new OkHttpClient();
		client.setConnectTimeout(getConnectionTimeout(), TimeUnit.MILLISECONDS);
		client.setWriteTimeout(getReadTimeout(), TimeUnit.MILLISECONDS);
		client.setReadTimeout(getReadTimeout(), TimeUnit.MILLISECONDS);

		Request.Builder builder = new Request.Builder();
		builder.url(urlString);

		addUserAgent(builder);
		addHeaders(builder);

		onConfigureRequestBuilder(builder);
		request = builder.build();

		return new ExecuteRequestCommand().execute(this);
	}

	protected void addUserAgent(Request.Builder builder) {
		String userAgent = getUserAgent();
		if (StringUtils.isNotBlank(userAgent)) {
			builder.addHeader(HttpService.USER_AGENT_HEADER, userAgent);
			LOGGER.debug("User Agent: " + userAgent);
		}
	}


	private void addHeaders(Request.Builder builder) {
		for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
			builder.addHeader(entry.getKey(), entry.getValue());
		}
	}

	protected void onConfigureRequestBuilder(Request.Builder builder) {
		// Do nothing
	}

	@Override
	protected void doFinally() {
		// Do nothing
	}
}
