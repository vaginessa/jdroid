package com.jdroid.java.exception;

/**
 * 
 * @author Maxi Rosson
 */
public class ErrorCodeException extends AbstractException {
	
	private static final long serialVersionUID = -7477869088363031784L;
	
	private ErrorCode errorCode;
	private Object[] errorCodeParameters;
	
	protected ErrorCodeException() {
		// Nothing by default.
	}
	
	protected ErrorCodeException(Throwable throwable) {
		super(throwable);
	}
	
	public ErrorCodeException(String errorMessage) {
		super(errorMessage);
	}
	
	public ErrorCodeException(ErrorCode errorCode, Throwable throwable) {
		super(throwable);
		this.errorCode = errorCode;
	}
	
	public ErrorCodeException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public ErrorCodeException(ErrorCode errorCode, Object... errorCodeParameters) {
		this.errorCode = errorCode;
		this.errorCodeParameters = errorCodeParameters;
	}
	
	public ErrorCodeException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * @return the errorCode
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	
	/**
	 * @return the errorCodeParameters
	 */
	public Object[] getErrorCodeParameters() {
		return errorCodeParameters;
	}
}
