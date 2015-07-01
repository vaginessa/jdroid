package com.jdroid.java.http.apache;

import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.utils.FileUtils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class ApacheHttpResponseWrapper extends HttpResponseWrapper {
	
	private HttpResponse httpResponse;
	
	public ApacheHttpResponseWrapper(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
	}

	@Override
	public InputStream getInputStream() {
		InputStream inputStream = null;
		try {
			inputStream = httpResponse.getEntity() != null ? httpResponse.getEntity().getContent() : null;
			Header contentEncoding = httpResponse.getFirstHeader(HttpService.CONTENT_ENCODING_HEADER);
			if (inputStream != null && (contentEncoding != null) && contentEncoding.getValue().equalsIgnoreCase(HttpService.GZIP_ENCODING)) {
				inputStream = new GZIPInputStream(inputStream);
			}
		} catch (IOException e) {
			throw new ConnectionException(e, false);
		}
		return inputStream;
	}

	/**
	 * @see com.jdroid.java.http.HttpResponseWrapper#getContent()
	 */
	@Override
	public String getContent() {
		String content = null;
		InputStream inputStream = null;
		try {
			inputStream = getInputStream();
			if (inputStream != null) {
				content = FileUtils.toString(inputStream);
			}
		} finally {
			FileUtils.safeClose(inputStream);
		}
		return content;
	}
	
	/**
	 * @see com.jdroid.java.http.HttpResponseWrapper#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		return httpResponse.getStatusLine().getStatusCode();
	}
	
	/**
	 * @see com.jdroid.java.http.HttpResponseWrapper#getStatusReason()
	 */
	@Override
	public String getStatusReason() {
		return httpResponse.getStatusLine().getReasonPhrase();
	}
	
	/**
	 * @see com.jdroid.java.http.HttpResponseWrapper#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		Header[] headerStatusCode = httpResponse.getHeaders(name);
		return headerStatusCode.length > 0 ? headerStatusCode[0].getValue() : null;
	}
	
}
