package com.jdroid.javaweb.exception;

import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.exception.ErrorCodeException;

public class InvalidAuthenticationException extends ErrorCodeException {
	
	public InvalidAuthenticationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
