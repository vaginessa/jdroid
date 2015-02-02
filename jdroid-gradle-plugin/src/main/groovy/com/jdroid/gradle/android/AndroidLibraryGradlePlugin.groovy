package com.jdroid.gradle.android

import org.gradle.api.Project

public class AndroidLibraryGradlePlugin extends AndroidGradlePlugin {

	public void apply(Project project) {
		super.apply(project);
	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidLibraryGradlePluginExtension.class;
	}
}

public class AndroidLibraryGradlePluginExtension extends AndroidGradlePluginExtension {

}
