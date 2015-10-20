package com.jdroid.java.http.okhttp;

import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.AbstractHttpService;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.utils.StringUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class OkHttpService extends AbstractHttpService {

	public OkHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	protected HttpResponseWrapper doExecute(String urlString) {

		OkHttpClient client = new OkHttpClient();
		client.setConnectTimeout(getConnectionTimeout(), TimeUnit.MILLISECONDS);
		client.setWriteTimeout(getReadTimeout(), TimeUnit.MILLISECONDS);
		client.setReadTimeout(getReadTimeout(), TimeUnit.MILLISECONDS);

		Request.Builder builder = new Request.Builder();
		builder.url(urlString);

		addUserAgent(builder);
		addHeaders(builder);

		onConfigureRequestBuilder(builder);
		Request request = builder.build();

		try {
			Response response = client.newCall(request).execute();
			return new OkHttpResponseWrapper(response);
		} catch (SocketTimeoutException e) {
			throw new ConnectionException(e, true);
		} catch (ConnectException e) {
			throw new ConnectionException(e, false);
		} catch (UnknownHostException e) {
			throw new ConnectionException(e, false);
		} catch (InterruptedIOException e) {
			throw new ConnectionException(e, true);
		} catch (SocketException e) {
			Throwable cause = e.getCause();
			if (cause != null) {
				String message = cause.getMessage();
				if (message != null) {
					if (message.contains("isConnected failed: EHOSTUNREACH (No route to host)")) {
						throw new ConnectionException(e, false);
					} else if (message.contains("recvfrom failed: ETIMEDOUT (Connection timed out)")) {
						throw new ConnectionException(e, true);
					} else if (message.contains("recvfrom failed: ECONNRESET (Connection reset by peer)")) {
						throw new ConnectionException(e, false);
					}
				}
			}
			throw new UnexpectedException(e);
		} catch (IOException e) {
			String message = e.getMessage();
			if (message != null && message.contains("unexpected end of stream on")) {
				throw new ConnectionException(e, true);
			}
			throw new UnexpectedException(e);
		}
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
