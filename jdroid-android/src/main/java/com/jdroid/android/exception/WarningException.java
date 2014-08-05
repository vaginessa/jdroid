package com.jdroid.android.exception;

import com.jdroid.java.exception.AbstractException;

public class WarningException extends AbstractException {
	
	private static final long serialVersionUID = -3119943371589459664L;
	
	public WarningException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public WarningException(String message) {
		super(message);
	}
}
