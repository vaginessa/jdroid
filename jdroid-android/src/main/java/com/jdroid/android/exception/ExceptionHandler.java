package com.jdroid.android.exception;

import java.lang.Thread.UncaughtExceptionHandler;

public interface ExceptionHandler extends UncaughtExceptionHandler {
	
	public void setDefaultExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler);
	
	public void logHandledException(String errorMessage, Throwable throwable);
	
	public void logHandledException(Throwable throwable);
	
	public void logWarningException(String errorMessage, Throwable throwable);
	
	public void logWarningException(String errorMessage);
	
	public void handleThrowable(Throwable throwable);
	
}
