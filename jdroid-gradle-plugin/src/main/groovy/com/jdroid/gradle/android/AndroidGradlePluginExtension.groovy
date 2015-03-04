package com.jdroid.gradle.android

import com.jdroid.gradle.commons.BaseGradleExtension

public class AndroidGradlePluginExtension extends BaseGradleExtension {

	String[] resourcesDirsPaths = ['src/main/res/']
	String[] notDefaultLanguages = []

	public AndroidGradlePluginExtension(AndroidGradlePlugin androidGradlePlugin) {
		super(androidGradlePlugin)
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
}