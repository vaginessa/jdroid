package com.jdroid.android.context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.ReflectionUtils;

public abstract class AbstractAppContext {

	public Class<?> getBuildConfigClass() {
		return ReflectionUtils.getClass(AbstractApplication.get().getManifestPackageName() + ".BuildConfig");
	}

	@SuppressWarnings("unchecked")
	public <T> T getBuildConfigValue(String property) {
		return (T)ReflectionUtils.getStaticFieldValue(getBuildConfigClass(), property);
	}

	@SuppressWarnings("unchecked")
	public <T> T getBuildConfigValue(String property, Object defaultValue) {
		return (T)ReflectionUtils.getStaticFieldValue(getBuildConfigClass(), property, defaultValue);
	}

	public Boolean getBuildConfigBoolean(String property, Boolean defaultValue) {
		return (Boolean)getBuildConfigValue(property, defaultValue);
	}
}
