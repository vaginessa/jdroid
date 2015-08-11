package com.jdroid.javaweb.exception;

import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.java.utils.ValidationUtils;

import java.util.Collection;

public enum CommonErrorCode implements ErrorCode {
	
	BAD_REQUEST(400),
	INVALID_CREDENTIALS(401),
	INVALID_USER_TOKEN(402),
	INVALID_API_VERSION(403),
	INVALID_SECURITY_TOKEN(404);
	
	private Integer statusCode;
	
	CommonErrorCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * @see com.jdroid.java.exception.ErrorCode#getStatusCode()
	 */
	@Override
	public String getStatusCode() {
		return statusCode.toString();
	}
	
	/**
	 * @see com.jdroid.java.exception.ErrorCode#newErrorCodeException(java.lang.Object[])
	 */
	@Override
	public ErrorCodeException newErrorCodeException(Object... errorCodeParameters) {
		return new ErrorCodeException(this, errorCodeParameters);
	}
	
	/**
	 * @see com.jdroid.java.exception.ErrorCode#newErrorCodeException()
	 */
	@Override
	public ErrorCodeException newErrorCodeException() {
		return new ErrorCodeException(this);
	}
	
	/**
	 * @see com.jdroid.java.exception.ErrorCode#newErrorCodeException(java.lang.Throwable)
	 */
	@Override
	public ErrorCodeException newErrorCodeException(Throwable throwable) {
		return new ErrorCodeException(this, throwable);
	}
	
	/**
	 * @see com.jdroid.java.exception.ErrorCode#newErrorCodeException(java.lang.String)
	 */
	@Override
	public ErrorCodeException newErrorCodeException(String message) {
		return new ErrorCodeException(this, message);
	}
	
	/**
	 * @see com.jdroid.java.exception.ErrorCode#getTitleResId()
	 */
	@Override
	public Integer getTitleResId() {
		return null;
	}
	
	/**
	 * @see com.jdroid.java.exception.ErrorCode#getDescriptionResId()
	 */
	@Override
	public Integer getDescriptionResId() {
		return null;
	}
	
	public void validateRequired(String value) {
		if (StringUtils.isEmpty(value)) {
			throw newErrorCodeException();
		}
	}
	
	public void validateRequired(Object value) {
		if (value == null) {
			throw newErrorCodeException();
		}
	}
	
	public void validateRequired(Collection<?> value) {
		if ((value == null) || value.isEmpty()) {
			throw newErrorCodeException();
		}
	}
	
	public void validatePositive(Integer value) {
		if (value <= 0) {
			throw newErrorCodeException();
		}
	}
	
	public void validatePositive(Float value) {
		if (value <= 0) {
			throw newErrorCodeException();
		}
	}
	
	public void validateMaximum(Integer value, Integer maximum) {
		if (value > maximum) {
			throw newErrorCodeException(maximum);
		}
	}
	
	public void validateMinimum(int value, int minimum) {
		if (value < minimum) {
			throw newErrorCodeException(minimum);
		}
	}
	
	public void validateMinimumLength(String value, int minimum) {
		if (value.length() < minimum) {
			throw newErrorCodeException(minimum);
		}
	}
	
	public void validateMaximumLength(String value, int maximum) {
		if (value.length() > maximum) {
			throw newErrorCodeException(maximum);
		}
	}
	
	/**
	 * Validates that the two values are equals. Assumes that no value is null.
	 * 
	 * @param value
	 * @param otherValue
	 */
	public void validateEquals(Object value, Object otherValue) {
		if ((value != null) && !value.equals(otherValue)) {
			throw newErrorCodeException();
		}
	}
	
	/**
	 * Validate that the value is an email address
	 * 
	 * @param value
	 */
	public void validateEmail(String value) {
		if (!ValidationUtils.isValidEmail(value)) {
			throw newErrorCodeException();
		}
	}
}
