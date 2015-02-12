package com.jdroid.gradle.android

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.TaskAction

public abstract class AbstractCountMethodsTask extends DefaultTask {

	@TaskAction
	public void doExecute() {

		group = JavaBasePlugin.VERIFICATION_GROUP

		List<String> apkFilePaths = []
		if (project.hasProperty('apkFilePath')) {
			apkFilePaths.add(project.apkFilePath)
		} else {
			FileCollection collection = project.files {
				project.file(project.buildDir.getAbsolutePath() + '/outputs/apk').listFiles(new FileFilter() {

					@Override
					boolean accept(File file) {
						return file.getAbsolutePath().endsWith(".apk") && !file.getAbsolutePath().endsWith("unaligned.apk")
					}
				})
			}
			apkFilePaths = collection.collect { File file ->
				file.getAbsolutePath()
			}
		}

		apkFilePaths.each { apkFilePath ->
			doGenerateReport(apkFilePath)
		}
	}

	protected abstract void doGenerateReport(String apkFilePath)
}
