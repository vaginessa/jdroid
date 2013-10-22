package com.jdroid.java.exception;

/**
 * Expected Application exception not related with business logic. <br>
 * For example a time out or I/O error.
 * 
 */
public class ApplicationException extends ErrorCodeException {
	
	protected ApplicationException() {
		// Nothing by default.
	}
	
	public ApplicationException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
	
	public ApplicationException(ErrorCode errorCode, Throwable throwable) {
		super(errorCode, throwable);
	}
	
	public ApplicationException(String errorMessage) {
		super(errorMessage);
	}
	
	public ApplicationException(Throwable throwable) {
		super(throwable);
	}
	
}
