package com.jdroid.gradle.android

import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Project

public class AndroidLibraryGradlePlugin extends AndroidGradlePlugin {

	public void apply(Project project) {
		super.apply(project);

		android.defaultConfig {
			jdroid.setString(android.defaultConfig, "VERSION", project.version)
		}

	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidLibraryGradlePluginExtension.class;
	}

	protected void applyAndroidPlugin() {
		project.apply plugin: LibraryPlugin
	}
}

