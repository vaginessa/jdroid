package com.jdroid.gradle.commons

import com.jdroid.java.exception.UnexpectedException

public class BaseGradleExtension {

	protected final BaseGradlePlugin baseGradlePlugin

	def versionMajor
	def versionMinor
	def versionPatch
	def versionClassifier = ""
	def integrationTestsPattern = "**/integration/**/*Test.class"

	public BaseGradleExtension(BaseGradlePlugin baseGradlePlugin) {
		this.baseGradlePlugin = baseGradlePlugin
	}

	public String getVersion() {
		return "${versionMajor}.${versionMinor}.${versionPatch}${versionClassifier}"
	}

	public String getGitSha() {
		return 'git rev-parse --short HEAD'.execute().text.trim()
	}

	public String getGitBranch() {
		return 'git symbolic-ref HEAD'.execute().text.trim().replaceAll(".*/", "")
	}

	public String getBuildTime() {
		def df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
		df.setTimeZone(TimeZone.getDefault())
		return df.format(new Date())
	}

	public def getProp(String propertyName) {
		return getProp(propertyName, null)
	}

	public def getProp(String propertyName, def defaultValue) {
		def value = baseGradlePlugin.project.hasProperty(propertyName) ? baseGradlePlugin.project.ext.get(propertyName) : System.getenv(propertyName)
		return value != null ? value : defaultValue
	}

	public Boolean getBooleanProp(String propertyName, Boolean defaultValue) {
		def value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else if (value == 'true') {
			return true
		} else if (value == 'false') {
			return false
		} else {
			throw new UnexpectedException("Invalid Boolean value: " + value)
		}
	}
}