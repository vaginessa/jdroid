package com.jdroid.java.exception;

/**
 * Expected Business exceptions that are related to the app's business logic.
 */
public class BusinessException extends ErrorCodeException {
	
	public BusinessException(ErrorCode errorCode, Object... errorCodeParameters) {
		super(errorCode, errorCodeParameters);
	}
	
	public BusinessException(ErrorCode errorCode) {
		super(errorCode);
	}
	
}
