package com.jdroid.android.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import com.jdroid.java.exception.ApplicationException;
import com.jdroid.java.exception.BusinessException;
import com.jdroid.java.exception.ConnectionException;

public interface ExceptionHandler extends UncaughtExceptionHandler {
	
	public void handleMainThreadException(Thread thread, Throwable throwable);
	
	public void handleException(BusinessException businessException);
	
	public void handleException(Thread thread, BusinessException businessException);
	
	public void handleException(Thread thread, ConnectionException connectionException);
	
	public void handleException(Thread thread, ApplicationException applicationException);
	
	public void handleException(Thread thread, InvalidApiVersionException exception);
	
	public void handleException(Thread thread, Throwable throwable);
	
	public void logHandledException(String message, Throwable throwable);
	
	public void logHandledException(Throwable throwable);
	
	public void logWarningException(String errorMessage, Throwable throwable);
	
	public void logWarningException(String errorMessage);
	
	public void setDefaultExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler);
	
}
