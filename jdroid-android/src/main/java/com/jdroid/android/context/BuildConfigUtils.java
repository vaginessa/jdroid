package com.jdroid.android.context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.ReflectionUtils;

public class BuildConfigUtils {

	@SuppressWarnings("unchecked")
	public static <T> T getBuildConfigValue(String property) {
		return (T)ReflectionUtils.getStaticFieldValue(AbstractApplication.get().getBuildConfigClass(), property);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBuildConfigValue(String property, Object defaultValue) {
		return (T)ReflectionUtils.getStaticFieldValue(AbstractApplication.get().getBuildConfigClass(), property, defaultValue);
	}

	public static Boolean getBuildConfigBoolean(String property, Boolean defaultValue) {
		return (Boolean)getBuildConfigValue(property, defaultValue);
	}
}
