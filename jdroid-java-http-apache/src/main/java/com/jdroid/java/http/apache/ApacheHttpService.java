package com.jdroid.java.http.apache;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.AbstractHttpService;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.utils.LoggerUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map.Entry;

public abstract class ApacheHttpService extends AbstractHttpService {
	
	protected static final Logger LOGGER = LoggerUtils.getLogger(ApacheHttpService.class);
	
	private List<Cookie> cookies = Lists.newArrayList();
	private HttpClientFactory httpClientFactory;
	private HttpClient client = null;
	
	public ApacheHttpService(HttpClientFactory httpClientFactory, Server server, List<Object> urlSegments,
							 List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
		this.httpClientFactory = httpClientFactory;
	}
	
	@Override
	protected HttpResponseWrapper doExecute(String url) {

		try {

			// make client for http.
			client = httpClientFactory.createHttpClient(getConnectionTimeout(), getReadTimeout(), getUserAgent());

			// Add Cookies
			addCookies(client);

			// make request.
			HttpUriRequest request = createHttpUriRequest(url);

			// Add Headers
			addHeaders(request);

			// execute request
			HttpResponse httpResponse = client.execute(request);

			return new ApacheHttpResponseWrapper(httpResponse);

		} catch (ClientProtocolException e) {
			Throwable cause = e.getCause();
			if ((cause != null) && (cause instanceof CircularRedirectException)) {
				throw new ConnectionException(e, false);
			} else {
				throw new UnexpectedException(e);
			}
		} catch (SocketTimeoutException e) {
			throw new ConnectionException(e, true);
		} catch (ConnectTimeoutException e) {
			throw new ConnectionException(e, true);
		} catch (IOException e) {
			throw new ConnectionException(e, false);
		}
	}

	@Override
	protected void doFinally() {
		if (client != null) {
			client.getConnectionManager().shutdown();
		}
	}

	public void addCookie(Cookie cookie) {
		cookies.add(cookie);
	}
	
	private void addHeaders(HttpUriRequest httpUriRequest) {
		for (Entry<String, String> entry : getHeaders().entrySet()) {
			httpUriRequest.addHeader(entry.getKey(), entry.getValue());
		}
	}
	
	protected void addCookies(HttpClient client) {
		if (client instanceof DefaultHttpClient) {
			for (Cookie cookie : cookies) {
				DefaultHttpClient.class.cast(client).getCookieStore().addCookie(cookie);
			}
		}
	}

	/**
	 * Create the {@link HttpUriRequest} to send.
	 * 
	 * @param url The url to execute
	 */
	protected abstract HttpUriRequest createHttpUriRequest(String url);
}
