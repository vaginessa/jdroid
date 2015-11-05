package com.jdroid.android.sample.api;

import com.jdroid.android.http.AbstractAndroidHttpResponseValidator;
import com.jdroid.java.exception.ErrorCode;
import com.jdroid.android.sample.exception.AndroidErrorCode;

public class HttpResponseValidator extends AbstractAndroidHttpResponseValidator {
	
	private static final HttpResponseValidator INSTANCE = new HttpResponseValidator();
	
	private HttpResponseValidator() {
		// nothing...
	}
	
	public static HttpResponseValidator get() {
		return INSTANCE;
	}
	
	/**
	 * @see AbstractAndroidHttpResponseValidator#findByStatusCode(java.lang.String)
	 */
	@Override
	protected ErrorCode findByStatusCode(String statusCode) {
		return AndroidErrorCode.findByStatusCode(statusCode);
	}
}
