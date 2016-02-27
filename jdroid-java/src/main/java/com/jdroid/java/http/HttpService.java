package com.jdroid.java.http;

import com.jdroid.java.http.parser.Parser;

import java.util.Collection;

public interface HttpService {
	
	public static final String HTTPS_PROTOCOL = "https";
	public static final String HTTP_PROTOCOL = "http";
	public static final String HTTP_SCHEME = "http://";
	
	public static final String ACCEPT_ENCODING_HEADER = "Accept-Encoding";
	public static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
	public static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
	public static final String GZIP_ENCODING = "gzip";
	public static final String ACCEPT_HEADER = "accept";
	public static final String CONTENT_TYPE_HEADER = "content-type";
	public static final String USER_AGENT_HEADER = "User-Agent";
	
	public static final String QUESTION_MARK = "?";
	public static final String EQUALS = "=";
	public static final String AMPERSAND = "&";
	
	public <T> T execute(Parser parser);
	
	public void execute();
	
	/**
	 * @param name The header name.
	 * @param value The header value.
	 */
	public void addHeader(String name, String value);

	public String getHeaderValue(String key);
	
	/**
	 * @param name The parameter name.
	 * @param value The parameter value.
	 */
	public void addQueryParameter(String name, Object value);
	
	/**
	 * @param name The parameter name.
	 * @param values The parameter values.
	 */
	public void addQueryParameter(String name, Collection<?> values);
	
	/**
	 * @param segment The segment name
	 */
	public void addUrlSegment(Object segment);
	
	public void addHttpServiceProcessor(HttpServiceProcessor httpServiceProcessor);
	
	/**
	 * @param connectionTimeout The connection timeout in milliseconds.
	 */
	public void setConnectionTimeout(Integer connectionTimeout);

	public void setReadTimeout(Integer readTimeout);

	public void setWriteTimeout(Integer writeTimeout);

	/**
	 * @param userAgent The user agent
	 */
	public void setUserAgent(String userAgent);
	
	/**
	 * @param ssl
	 */
	public void setSsl(Boolean ssl);
	
	public String getUrl();
	
	public String getUrlSuffix();

	public HttpResponseWrapper getHttpResponseWrapper();
	
}
