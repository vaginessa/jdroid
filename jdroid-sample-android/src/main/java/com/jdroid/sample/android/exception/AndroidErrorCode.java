package com.jdroid.sample.android.exception;

import java.util.Collection;
import com.jdroid.java.exception.ApplicationException;
import com.jdroid.java.exception.BusinessException;
import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.java.utils.ValidationUtils;

public enum AndroidErrorCode implements ErrorCode {
	
	SAMPLE_ERROR(null, 203);
	
	private Integer resourceId;
	private Integer statusCode;
	
	private AndroidErrorCode(Integer resourceId, Integer statusCode) {
		this.resourceId = resourceId;
		this.statusCode = statusCode;
	}
	
	private AndroidErrorCode(Integer resourceId) {
		this.resourceId = resourceId;
	}
	
	public static ErrorCode findByStatusCode(String statusCode) {
		ErrorCode errorCode = null;
		for (AndroidErrorCode each : values()) {
			if ((each.statusCode != null) && each.statusCode.toString().equals(statusCode)) {
				errorCode = each;
				break;
			}
		}
		return errorCode;
	}
	
	/**
	 * @see com.jdroid.java.exception.ErrorCode#getStatusCode()
	 */
	@Override
	public String getStatusCode() {
		return statusCode.toString();
	}
	
	/**
	 * @see com.jdroid.java.exception.ErrorCode#newBusinessException(java.lang.Object[])
	 */
	@Override
	public BusinessException newBusinessException(Object... errorCodeParameters) {
		return new BusinessException(this, errorCodeParameters);
	}
	
	/**
	 * @see com.jdroid.java.exception.ErrorCode#newApplicationException(java.lang.Throwable)
	 */
	@Override
	public ApplicationException newApplicationException(Throwable throwable) {
		return new ApplicationException(this, throwable);
	}
	
	/**
	 * @see com.jdroid.java.exception.ErrorCode#newApplicationException(java.lang.String)
	 */
	@Override
	public ApplicationException newApplicationException(String message) {
		return new ApplicationException(this, message);
	}
	
	/**
	 * @see com.jdroid.java.exception.ErrorCode#getResourceId()
	 */
	@Override
	public Integer getResourceId() {
		return resourceId;
	}
	
	public void validateRequired(String value) {
		if (StringUtils.isEmpty(value)) {
			throw newBusinessException();
		}
	}
	
	public void validateRequired(Object value) {
		if (value == null) {
			throw newBusinessException();
		}
	}
	
	public void validateRequired(Collection<?> value) {
		if ((value == null) || value.isEmpty()) {
			throw newBusinessException();
		}
	}
	
	public void validatePositive(Integer value) {
		if (value <= 0) {
			throw newBusinessException();
		}
	}
	
	public void validatePositive(Float value) {
		if (value <= 0) {
			throw newBusinessException();
		}
	}
	
	public void validateMaximum(Integer value, Integer maximum) {
		if (value > maximum) {
			throw newBusinessException(maximum);
		}
	}
	
	public void validateMinimum(int value, int minimum) {
		if (value < minimum) {
			throw newBusinessException(minimum);
		}
	}
	
	public void validateMinimumLength(String value, int minimum) {
		if (value.length() < minimum) {
			throw newBusinessException(minimum);
		}
	}
	
	public void validateMaximumLength(String value, int maximum) {
		if (value.length() > maximum) {
			throw newBusinessException(maximum);
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
			throw newBusinessException();
		}
	}
	
	/**
	 * Validate that the value is an email address
	 * 
	 * @param value
	 */
	public void validateEmail(String value) {
		if (!ValidationUtils.isValidEmail(value)) {
			throw newBusinessException();
		}
	}
}
