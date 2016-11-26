package com.jdroid.java.http;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.parser.Parser;
import com.jdroid.java.utils.EncodingUtils;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

import org.slf4j.Logger;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractHttpService implements HttpService {

	protected static final Logger LOGGER = LoggerUtils.getLogger(AbstractHttpService.class);

	private Boolean ssl = false;

	/** Connection timeout in milliseconds. 10 seconds as default */
	private Integer connectionTimeout = 10000;

	/** Read timeout in milliseconds. 60 seconds as default */
	private Integer readTimeout = 60000;

	/** Write timeout in milliseconds. 60 seconds as default */
	private Integer writeTimeout = 60000;

	private String userAgent;

	private Server server;
	private List<Object> urlSegments;

	/** Query Parameter values of the request. */
	private Map<String, String> queryParameters = Maps.newLinkedHashMap();

	/** Header values of the request. */
	private Map<String, String> headers = Maps.newHashMap();

	private List<HttpServiceProcessor> httpServiceProcessors = Lists.newArrayList();

	private HttpResponseWrapper httpResponseWrapper;

	/**
	 * @param httpServiceProcessors
	 * @param urlSegments
	 * @param server The {@link Server} where execute the request
	 */
	public AbstractHttpService(Server server, List<Object> urlSegments,
							   List<HttpServiceProcessor> httpServiceProcessors) {

		this.urlSegments = Lists.newArrayList();
		if (urlSegments != null) {
			for (Object segment : urlSegments) {
				addUrlSegment(segment);
			}
		}
		this.server = server;

		if (httpServiceProcessors != null) {
			for (HttpServiceProcessor each : httpServiceProcessors) {
				addHttpServiceProcessor(each);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void execute() {
		execute(null);
	}

	@Override
	public <T> T execute(Parser parser) {
		InputStream inputStream = null;
		try {

			for (HttpServiceProcessor each : httpServiceProcessors) {
				each.beforeExecute(this);
			}

			String url = getUrl();

			// Log request
			LOGGER.debug(getHttpMethod() + ": " + url);
			if (!queryParameters.isEmpty()) {
				LOGGER.debug("Query Parameters: " + queryParameters.toString());
			}
			if (!headers.isEmpty()) {
				LOGGER.debug("Headers: " + headers.toString());
			}

			httpResponseWrapper = doExecute(url);

			for (HttpServiceProcessor each : httpServiceProcessors) {
				each.afterExecute(this, httpResponseWrapper);
			}

			// parse and return response.
			if (parser != null) {

				inputStream = httpResponseWrapper.getInputStream();

				if (inputStream != null) {
					return parse(parser, inputStream);
				} else {
					throw new UnexpectedException("The http service was expecting a response, but it was null");
				}
			}
			return null;
		} finally {
			FileUtils.safeClose(inputStream);
			doFinally();
		}
	}

	protected void doFinally() {
		// Do Nothing
	}

	public abstract HttpMethod getHttpMethod();

	protected abstract HttpResponseWrapper doExecute(String url);

	@SuppressWarnings("unchecked")
	protected <T> T parse(Parser parser, InputStream inputStream) {
		return (T)parser.parse(inputStream);
	}

	@Override
	public void setSsl(Boolean ssl) {
		this.ssl = ssl;
	}

	public Boolean isSsl() {
		return ssl;
	}

	@Override
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Override
	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	@Override
	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	public Integer getWriteTimeout() {
		return writeTimeout;
	}

	@Override
	public void setWriteTimeout(Integer writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public String getUserAgent() {
		return userAgent;
	}

	@Override
	public String getUrl() {
		StringBuilder builder = new StringBuilder();
		builder.append(isSsl() && getServer().supportsSsl() ? HTTPS_PROTOCOL : HTTP_PROTOCOL);
		builder.append("://");
		builder.append(getServer().getBaseUrl());
		builder.append(getUrlSuffix());
		return builder.toString();
	}

	@Override
	public String getUrlSuffix() {
		StringBuilder builder = new StringBuilder();
		builder.append(getUrlSegments());
		builder.append(makeStringParameters());
		return builder.toString();
	}

	protected String makeStringParameters() {
		StringBuilder params = new StringBuilder();
		boolean isFirst = true;

		for (Map.Entry<String, String> entry : getQueryParameters().entrySet()) {
			if (isFirst) {
				params.append(QUESTION_MARK);
				isFirst = false;
			} else {
				params.append(AMPERSAND);
			}
			params.append(entry.getKey());
			params.append(EQUALS);
			params.append(entry.getValue());
		}

		return params.toString();
	}

	@Override
	public void addQueryParameter(String name, Collection<?> values) {
		addQueryParameter(name, StringUtils.join(values));
	}

	@Override
	public void addQueryParameter(String name, Object value) {
		if (value != null) {
			queryParameters.put(name, EncodingUtils.encodeURL(value.toString()));
		}
	}

	/**
	 * @return the Query parameters
	 */
	public Map<String, String> getQueryParameters() {
		return queryParameters;
	}

	public Server getServer() {
		return server;
	}

	@Override
	public void addUrlSegment(Object segment) {
		String segmentString = segment.toString();
		if (StringUtils.isNotEmpty(segmentString)) {
			urlSegments.add(EncodingUtils.encodeURL(segmentString));
		}
	}

	public String getUrlSegments() {
		return urlSegments.isEmpty() ? StringUtils.EMPTY : StringUtils.SLASH
				+ StringUtils.join(urlSegments, StringUtils.SLASH);
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public String getHeaderValue(String key) {
		return headers.get(key);
	}

	@Override
	public void addHeader(String name, String value) {
		if (value != null) {
			headers.put(name, value);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " - " + server.getBaseUrl();
	}


	@Override
	public void addHttpServiceProcessor(HttpServiceProcessor httpServiceProcessor) {
		httpServiceProcessors.add(httpServiceProcessor);
		httpServiceProcessor.onInit(this);
	}

	@Override
	public HttpResponseWrapper getHttpResponseWrapper() {
		return httpResponseWrapper;
	}
}
