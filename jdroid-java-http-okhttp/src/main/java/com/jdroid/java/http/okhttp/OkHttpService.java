package com.jdroid.java.http.okhttp;

import com.jdroid.java.http.AbstractHttpService;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.parser.Parser;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public abstract class OkHttpService extends AbstractHttpService {

	static {
		// This is to avoid exceptions like "java.net.ProtocolException: Unexpected status line:..." that occurs in some
		// cases when HTTP connections are reused. This issue, according to the tests carried out, seems to happen in the app
		// only in some cases when a connection is reused after a response code 204.
		System.setProperty("http.keepAlive", "false");
	}

	private static OkHttpClient OK_HTTP_CLIENT_PROTOTYPE;

	protected OkHttpClient client;
	protected Request request;
	private List<Interceptor> networkInterceptors;

	public OkHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	protected HttpResponseWrapper doExecute(String urlString) {

		if (OK_HTTP_CLIENT_PROTOTYPE == null) {
			synchronized (OkHttpService.class) {
				if (OK_HTTP_CLIENT_PROTOTYPE == null) {
					OK_HTTP_CLIENT_PROTOTYPE = new OkHttpClient();
				}
			}
		}

		OkHttpClient.Builder clientBuilder = OK_HTTP_CLIENT_PROTOTYPE.newBuilder();
		clientBuilder.connectTimeout(getConnectionTimeout(), TimeUnit.MILLISECONDS);
		clientBuilder.writeTimeout(getWriteTimeout(), TimeUnit.MILLISECONDS);
		clientBuilder.readTimeout(getReadTimeout(), TimeUnit.MILLISECONDS);
		if (networkInterceptors != null) {
			for (Interceptor each : networkInterceptors) {
				clientBuilder.addNetworkInterceptor(each);
			}
		}
		client = clientBuilder.build();

		Request.Builder builder = new Request.Builder();
		builder.url(urlString);

		addUserAgent(builder);
		addHeaders(builder);

		onConfigureRequestBuilder(builder);
		request = builder.build();

		return new ExecuteRequestCommand().execute(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T parse(Parser parser, InputStream inputStream) {
		return (T)new ParseCommand(parser).execute(inputStream);
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
		try {
			OkHttpResponseWrapper okHttpResponseWrapper = (OkHttpResponseWrapper)getHttpResponseWrapper();
			if (okHttpResponseWrapper != null) {
				ResponseBody body = okHttpResponseWrapper.getResponse().body();
				if (body != null) {
					body.close();
				}
			}
		} catch (Exception e) {
			LoggerUtils.logHandledException(LOGGER, e);
		}
	}

	public void setNetworkInterceptors(List<Interceptor> networkInterceptors) {
		this.networkInterceptors = networkInterceptors;
	}

	public void addNetworkInterceptor(Interceptor networkInterceptor) {
		this.networkInterceptors.add(networkInterceptor);
	}
}
