package com.jdroid.java.exception;

/**
 * 
 * @author Estefania Caravatti
 */
public class HttpResponseException extends ApplicationException {
	
	private static final long serialVersionUID = 3969454310912271279L;
	
	protected HttpResponseException() {
		// Nothing by default.
	}
	
	public HttpResponseException(ErrorCode errorCode, Throwable throwable) {
		super(errorCode, throwable);
	}
	
	public HttpResponseException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
	
}
