package com.jdroid.android.exception;

import com.jdroid.java.exception.AbstractException;

/**
 * 
 * @author Maxi Rosson
 */
public class WarningException extends AbstractException {
	
	public WarningException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public WarningException(String message) {
		super(message);
	}
}
