package com.jdroid.java.http;

import com.jdroid.java.exception.ErrorCode;

public class DefaultHttpResponseValidator extends AbstractHttpResponseValidator {

	private static final DefaultHttpResponseValidator INSTANCE = new DefaultHttpResponseValidator();

	private DefaultHttpResponseValidator() {
		// nothing...
	}

	public static DefaultHttpResponseValidator get() {
		return INSTANCE;
	}

	@Override
	protected ErrorCode findByCommonStatusCode(String statusCode) {
		return null;
	}

	@Override
	protected ErrorCode findByStatusCode(String statusCode) {
		return null;
	}
}
