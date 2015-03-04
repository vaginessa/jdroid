package com.jdroid.gradle.commons

public class BaseGradleExtension {

	protected final BaseGradlePlugin baseGradlePlugin

	public BaseGradleExtension(BaseGradlePlugin baseGradlePlugin) {
		this.baseGradlePlugin = baseGradlePlugin
	}

	public def getProp(String propertyName) {
		return getProp(propertyName, null)
	}

	public def getProp(String propertyName, def defaultValue) {
		def value = baseGradlePlugin.project.hasProperty(propertyName) ? baseGradlePlugin.project.ext.get(propertyName) : System.getenv(propertyName)
		return value != null ? value : defaultValue
	}
}