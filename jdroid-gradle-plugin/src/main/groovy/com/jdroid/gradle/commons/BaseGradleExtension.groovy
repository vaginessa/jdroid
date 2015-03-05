package com.jdroid.gradle.commons

public class BaseGradleExtension {

	protected final BaseGradlePlugin baseGradlePlugin

	public BaseGradleExtension(BaseGradlePlugin baseGradlePlugin) {
		this.baseGradlePlugin = baseGradlePlugin
	}

	public String gitSha() {
		return 'git rev-parse --short HEAD'.execute().text.trim()
	}

	public String buildTime() {
		def df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
		df.setTimeZone(TimeZone.getDefault())
		return df.format(new Date())
	}

	public String branch() {
		return 'git symbolic-ref HEAD'.execute().text.trim().replaceAll(".*/", "")
	}

	public def getProp(String propertyName) {
		return getProp(propertyName, null)
	}

	public def getProp(String propertyName, def defaultValue) {
		def value = baseGradlePlugin.project.hasProperty(propertyName) ? baseGradlePlugin.project.ext.get(propertyName) : System.getenv(propertyName)
		return value != null ? value : defaultValue
	}
}