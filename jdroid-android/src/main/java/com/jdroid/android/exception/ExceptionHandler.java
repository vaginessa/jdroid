package com.jdroid.android.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import com.jdroid.java.utils.LoggerUtils.ExceptionLogger;

public interface ExceptionHandler extends UncaughtExceptionHandler, ExceptionLogger {
	
	public void setDefaultExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler);
	
	public void logHandledException(String errorMessage, Throwable throwable);
	
	public void logWarningException(String errorMessage, Throwable throwable);
	
	public void logWarningException(String errorMessage);
	
	public void handleThrowable(Throwable throwable);
	
}
