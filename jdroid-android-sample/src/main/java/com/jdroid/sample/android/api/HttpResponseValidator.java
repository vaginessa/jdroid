package com.jdroid.sample.android.api;

import com.jdroid.android.http.AbstractHttpResponseValidator;
import com.jdroid.java.exception.ErrorCode;
import com.jdroid.sample.android.exception.AndroidErrorCode;

public class HttpResponseValidator extends AbstractHttpResponseValidator {
	
	private static final HttpResponseValidator INSTANCE = new HttpResponseValidator();
	
	private HttpResponseValidator() {
		// nothing...
	}
	
	public static HttpResponseValidator get() {
		return INSTANCE;
	}
	
	/**
	 * @see AbstractHttpResponseValidator#findByStatusCode(java.lang.String)
	 */
	@Override
	protected ErrorCode findByStatusCode(String statusCode) {
		return AndroidErrorCode.findByStatusCode(statusCode);
	}
}
