package com.jdroid.gradle.android

import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

public class AndroidLibraryGradlePlugin extends AndroidGradlePlugin {

	public void apply(Project project) {
		super.apply(project);

		project.ext.PACKAGING = 'aar'

		android.defaultConfig {
			jdroid.setString(android.defaultConfig, "VERSION", project.version)
		}

		Boolean isOpenSourceEnabled = jdroid.getBooleanProp("OPEN_SOURCE_ENABLED", true)
		if (isOpenSourceEnabled) {
			project.task('androidSourcesJar', type: Jar) {
				classifier = 'sources'
				from android.sourceSets.main.java.sourceFiles, android.sourceSets.debug.java.sourceFiles
			}

			project.artifacts {
				archives project.tasks.androidSourcesJar
			}
		}
	}

	protected Class<? extends AndroidLibraryGradlePluginExtension> getExtensionClass() {
		return AndroidLibraryGradlePluginExtension.class;
	}

	protected void applyAndroidPlugin() {
		project.apply plugin: LibraryPlugin
	}
}

