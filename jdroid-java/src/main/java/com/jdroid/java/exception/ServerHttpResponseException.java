package com.jdroid.java.exception;


/**
 * Exception thrown when there are http errors communicating with the server.
 * 
 * @author Estefania Caravatti
 */
public class ServerHttpResponseException extends HttpResponseException {
	
	private static final long serialVersionUID = 7532460690433661042L;
	
	public ServerHttpResponseException(ErrorCode errorCode, Throwable throwable) {
		super(errorCode, throwable);
	}
	
	public ServerHttpResponseException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}
