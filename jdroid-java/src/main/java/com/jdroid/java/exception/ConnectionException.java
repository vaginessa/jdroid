package com.jdroid.java.exception;

/**
 * Exception related with connectivity errors.
 */
public class ConnectionException extends ApplicationException {
	
	private static final long serialVersionUID = 1136199464840653811L;
	
	private Boolean timeout = false;
	
	public ConnectionException(Throwable throwable) {
		super(throwable);
	}
	
	public ConnectionException(String message) {
		super(message);
	}
	
	public ConnectionException(Throwable throwable, Boolean isTimeout) {
		super(throwable);
		timeout = isTimeout;
	}
	
	public boolean isTimeout() {
		return timeout;
	}
}
