package com.jdroid.java.exception;

/**
 * Exception related with connectivity errors.
 */
public class ConnectionException extends ApplicationException {
	
	private static final long serialVersionUID = 1136199464840653811L;
	
	public ConnectionException(Throwable throwable) {
		super(throwable);
	}
	
	public ConnectionException(String message) {
		super(message);
	}
	
}
