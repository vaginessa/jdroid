package com.jdroid.javaweb.exception;

import com.jdroid.java.exception.AbstractException;

public class InvalidArgumentException extends AbstractException {
	
	private static final long serialVersionUID = 8008107012607820854L;
	
	public InvalidArgumentException(String message) {
		super(message);
	}
	
}
