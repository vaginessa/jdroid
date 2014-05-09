package com.jdroid.java.exception;

/**
 * Expected Business exceptions that are related to the app's business logic.
 */
public class BusinessException extends ErrorCodeException {
	
	private static final long serialVersionUID = -4230222129357855037L;
	
	public BusinessException(ErrorCode errorCode, Object... errorCodeParameters) {
		super(errorCode, errorCodeParameters);
	}
	
	public BusinessException(ErrorCode errorCode) {
		super(errorCode);
	}
	
}
