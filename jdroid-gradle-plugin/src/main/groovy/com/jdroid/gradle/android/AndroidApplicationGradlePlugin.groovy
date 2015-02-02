package com.jdroid.gradle.android

import org.gradle.api.Project

public class AndroidApplicationGradlePlugin extends AndroidGradlePlugin {

	public void apply(Project project) {
		super.apply(project);
	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidApplicationGradlePluginExtension.class;
	}
}

public class AndroidApplicationGradlePluginExtension extends AndroidGradlePluginExtension {

}
