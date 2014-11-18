package com.jdroid.java.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jdroid.java.logger.MuteLogger;

public class LoggerUtils {
	
	private static boolean enabled = false;
	private static ExceptionLogger exceptionLogger;
	
	private static final Logger MUTE_LOGGER = new MuteLogger();
	
	public static Logger getLogger(Object name) {
		return LoggerUtils.getLogger(name.getClass());
	}
	
	public static Logger getLogger(Class<?> clazz) {
		if (enabled) {
			return LoggerFactory.getLogger(getLmitedName(clazz.getSimpleName()));
		} else {
			return MUTE_LOGGER;
		}
	}
	
	private static String getLmitedName(String name) {
		// Logcat support 23 characters as maximum
		return name.substring(0, Math.min(name.length(), 23));
	}
	
	public static void setEnabled(boolean enabled) {
		LoggerUtils.enabled = enabled;
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
	
	public static void logHandledException(Logger logger, Exception e) {
		if (exceptionLogger != null) {
			exceptionLogger.logHandledException(e);
		} else {
			logger.error(e.getMessage(), e);
		}
	}
	
	public static interface ExceptionLogger {
		
		public void logHandledException(Throwable throwable);
	}
	
	public static void setExceptionLogger(ExceptionLogger exceptionLogger) {
		LoggerUtils.exceptionLogger = exceptionLogger;
	}
}
