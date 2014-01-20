package com.jdroid.java.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jdroid.java.logger.MuteLogger;

/**
 * 
 * @author Maxi Rosson
 */
public class LoggerUtils {
	
	private static boolean enabled = false;
	
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
}
