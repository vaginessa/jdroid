package com.jdroid.gradle.android

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

public abstract class AndroidGradlePlugin implements Plugin<Project> {

	public void apply(Project project) {
		project.extensions.create("jdroid", getExtensionClass())

		project.task('verifyMissingTranslations') << {

			description = 'Verify if there are missing translations ("TODO") on any string resource.'
			group = 'verification'

			def resourcesDirsPaths = project.jdroid.resourcesDirsPaths

			for (resourceDirPath in resourcesDirsPaths) {
				File resDirFile = new File(resourceDirPath)
				for (file in resDirFile.listFiles()) {
					if (file.isDirectory() && file.getName().startsWith("values")) {
						String[] filesNames = ['strings.xml', 'plurals.xml', 'array.xml']
						for (fileName in filesNames) {
							String resourceFilePath = file.getAbsolutePath() + File.separator + fileName
							File resourceFile = new File(resourceFilePath)
							if (resourceFile.exists()) {
								if (resourceFile.text.contains("TODO")) {
									throw new GradleException('Missing translation on ' + resourceFilePath)
								} else {
									println "Not Missing translations on " + resourceFilePath
								}
							}
						}
					}
				}
			}
		}
	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidGradlePluginExtension.class;
	}
}

public class AndroidGradlePluginExtension {
	String[] resourcesDirsPaths = []
}

