package com.jdroid.gradle.android;

public class AndroidApplicationGradlePluginExtension extends AndroidGradlePluginExtension {

	def versionMajor
	def versionMinor
	def versionPatch

	public AndroidApplicationGradlePluginExtension(AndroidGradlePlugin androidGradlePlugin) {
		super(androidGradlePlugin)
	}

}