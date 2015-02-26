package com.jdroid.gradle.android

import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Project

public class AndroidLibraryGradlePlugin extends AndroidGradlePlugin {

	public void apply(Project project) {
		super.apply(project);

	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidLibraryGradlePluginExtension.class;
	}

	protected void applyAndroidPlugin() {
		project.apply plugin: LibraryPlugin
	}
}

