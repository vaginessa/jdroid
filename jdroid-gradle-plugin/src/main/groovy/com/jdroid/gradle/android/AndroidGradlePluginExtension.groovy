package com.jdroid.gradle.android

public class AndroidGradlePluginExtension {

	String[] resourcesDirsPaths = ['src/main/res/']
	String[] notDefaultLanguages = []

	protected final AndroidGradlePlugin androidGradlePlugin

	public AndroidGradlePluginExtension(AndroidGradlePlugin androidGradlePlugin) {
		this.androidGradlePlugin = androidGradlePlugin
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

	public void setString(def flavor, String propertyName, String defaultValue) {
		def value = getProp(propertyName, defaultValue)
		def stringValue = value == null ? "null" : '"' + value + '"'
		flavor.buildConfigField "String", propertyName, stringValue
	}

	public void setBoolean(def flavor, String propertyName, Boolean defaultValue) {
		def value = getProp(propertyName, defaultValue).toString()
		flavor.buildConfigField "Boolean", propertyName, value
	}

	public void setInteger(def flavor, String propertyName, Integer defaultValue) {
		def value = getProp(propertyName, defaultValue).toString()
		flavor.buildConfigField "Integer", propertyName, value
	}

	public def getProp(String propertyName) {
		return getProp(propertyName, null)
	}

	public def getProp(String propertyName, def defaultValue) {
		def value = androidGradlePlugin.project.hasProperty(propertyName) ? androidGradlePlugin.project.ext.get(propertyName) : System.getenv(propertyName)
		return value != null ? value : defaultValue
	}
}