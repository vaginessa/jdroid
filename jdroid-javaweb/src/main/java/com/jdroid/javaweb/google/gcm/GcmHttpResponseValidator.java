package com.jdroid.javaweb.google.gcm;

import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.http.AbstractHttpResponseValidator;
import com.jdroid.java.http.HttpResponseWrapper;

public class GcmHttpResponseValidator extends AbstractHttpResponseValidator {

	private static final GcmHttpResponseValidator INSTANCE = new GcmHttpResponseValidator();

	private GcmHttpResponseValidator() {
		// nothing...
	}

	public static GcmHttpResponseValidator get() {
		return INSTANCE;
	}

	@Override
	protected void onServerError(HttpResponseWrapper httpResponse, String message) {
		// Do Nothing
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
