package com.jdroid.gradle.android

import com.jdroid.gradle.commons.JavaBaseGradleExtension

public class AndroidGradlePluginExtension extends JavaBaseGradleExtension {

	String[] resourcesDirsPaths = ['src/main/res/']
	String[] notDefaultLanguages = []
	String missingTranslationExpression = "#TODO#"

	public AndroidGradlePluginExtension(AndroidGradlePlugin androidGradlePlugin) {
		super(androidGradlePlugin)
	}

	public void setBuildConfigString(def flavor, String propertyName) {
		setBuildConfigString(flavor, propertyName, null)
	}

	public void setBuildConfigString(def flavor, String propertyName, String defaultValue) {
		def value = getProp(propertyName, defaultValue)
		def stringValue = value == null ? "null" : '"' + value + '"'
		flavor.buildConfigField "String", propertyName, stringValue
	}

	public void setBuildConfigBoolean(def flavor, String propertyName, Boolean defaultValue) {
		def value = getProp(propertyName, defaultValue).toString()
		flavor.buildConfigField "Boolean", propertyName, value
	}

	public void setBuildConfigInteger(def flavor, String propertyName, Integer defaultValue) {
		def value = getProp(propertyName, defaultValue).toString()
		flavor.buildConfigField "Integer", propertyName, value
	}

	public void setResValueString(def flavor, String propertyName, String defaultValue) {
		def value = getProp(propertyName, defaultValue)
		def stringValue = value == null ? "null" : '"' + value + '"'
		flavor.buildConfigField "string", propertyName, stringValue
	}
}