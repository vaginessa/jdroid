package com.jdroid.java.http;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.parser.Parser;
import com.jdroid.java.utils.EncodingUtils;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

import org.slf4j.Logger;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractWebService implements WebService {

	protected static final Logger LOGGER = LoggerUtils.getLogger(AbstractWebService.class);

	private Boolean ssl = false;

	/** Connection timeout in milliseconds. 10 seconds as default */
	private Integer connectionTimeout = 10000;

	/** Read timeout in milliseconds. 60 seconds as default */
	private Integer readTimeout = 60000;

	private String userAgent;

	private Server server;
	private List<Object> urlSegments;

	/** Query Parameter values of the request. */
	private Map<String, String> queryParameters = Maps.newLinkedHashMap();

	/** Header values of the request. */
	private Map<String, String> headers = Maps.newHashMap();

	private List<HttpWebServiceProcessor> httpWebServiceProcessors = Lists.newArrayList();

	/**
	 * @param httpWebServiceProcessors
	 * @param urlSegments
	 * @param server The {@link Server} where execute the request
	 */
	public AbstractWebService(Server server, List<Object> urlSegments,
								List<HttpWebServiceProcessor> httpWebServiceProcessors) {

		this.urlSegments = Lists.newArrayList();
		if (urlSegments != null) {
			for (Object segment : urlSegments) {
				addUrlSegment(segment);
			}
		}
		this.server = server;

		if (httpWebServiceProcessors != null) {
			for (HttpWebServiceProcessor each : httpWebServiceProcessors) {
				addHttpWebServiceProcessor(each);
			}
		}
	}

	/**
	 * @see com.jdroid.java.http.WebService#execute()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final <T> T execute() {
		return (T)execute(null);
	}

	/**
	 * @see com.jdroid.java.http.WebService#execute(com.jdroid.java.parser.Parser)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T execute(Parser parser) {
		InputStream inputStream = null;
		try {

			for (HttpWebServiceProcessor each : httpWebServiceProcessors) {
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

			HttpResponseWrapper httpResponseWrapper = doExecute(url);

			for (HttpWebServiceProcessor each : httpWebServiceProcessors) {
				each.afterExecute(this, httpResponseWrapper);
			}

			inputStream = httpResponseWrapper.getInputStream();

			// parse and return response.
			if (parser != null) {
				if (inputStream != null) {
					return (T)parser.parse(inputStream);
				} else {
					throw new UnexpectedException("The web service was expecting a response, but it was null");
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

	/**
	 * @see com.jdroid.java.http.WebService#setSsl(java.lang.Boolean)
	 */
	@Override
	public void setSsl(Boolean ssl) {
		this.ssl = ssl;
	}

	public Boolean isSsl() {
		return ssl;
	}

	/**
	 * @see com.jdroid.java.http.WebService#setUserAgent(java.lang.String)
	 */
	@Override
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * @see com.jdroid.java.http.WebService#setConnectionTimeout(java.lang.Integer)
	 */
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

	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @see com.jdroid.java.http.WebService#getUrl()
	 */
	@Override
	public String getUrl() {
		StringBuilder builder = new StringBuilder();
		builder.append(isSsl() && getServer().supportsSsl() ? HTTPS_PROTOCOL : HTTP_PROTOCOL);
		builder.append("://");
		builder.append(getServer().getBaseUrl());
		builder.append(getUrlSuffix());
		return builder.toString();
	}

	/**
	 * @see com.jdroid.java.http.WebService#getUrlSuffix()
	 */
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

	/**
	 * @see com.jdroid.java.http.WebService#addQueryParameter(java.lang.String, java.util.Collection)
	 */
	@Override
	public void addQueryParameter(String name, Collection<?> values) {
		addQueryParameter(name, StringUtils.join(values));
	}

	/**
	 * @see com.jdroid.java.http.WebService#addQueryParameter(java.lang.String, java.lang.Object)
	 */
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

	/**
	 * @see com.jdroid.java.http.WebService#addUrlSegment(java.lang.Object)
	 */
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

	/**
	 * @see com.jdroid.java.http.WebService#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value) {
		if (value != null) {
			headers.put(name, value);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + " - " + server.getBaseUrl();
	}


	/**
	 * @see com.jdroid.java.http.WebService#addHttpWebServiceProcessor(com.jdroid.java.http.HttpWebServiceProcessor)
	 */
	@Override
	public void addHttpWebServiceProcessor(HttpWebServiceProcessor httpWebServiceProcessor) {
		httpWebServiceProcessors.add(httpWebServiceProcessor);
		httpWebServiceProcessor.onInit(this);
	}
}
