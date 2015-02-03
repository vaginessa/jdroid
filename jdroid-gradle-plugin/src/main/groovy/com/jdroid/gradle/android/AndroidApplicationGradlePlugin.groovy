package com.jdroid.gradle.android

import org.gradle.api.GradleException
import org.gradle.api.Project

public class AndroidApplicationGradlePlugin extends AndroidGradlePlugin {

	public void apply(Project project) {
		super.apply(project);

		project.task('countMethods') << {

			description = 'Output per-package method counts in an Android DEX executable grouped by package.'
			group = 'verification'

			if (!project.hasProperty('apkPathName')) {
				throw new GradleException('Missing apkPathName parameter')
			} else {
				println "Counting the methods on " + project.apkPathName
			}

			File outputDir = project.file('./build/reports/');
			outputDir.mkdirs();
			File outputFile = project.file(outputDir.getAbsolutePath() + '/methodCount.txt');

			info.persistent.dex.Main main = new info.persistent.dex.Main();
			main.count(project.apkPathName, outputFile);

			println "Methods count report generated on " + outputFile.getAbsolutePath();

		}
	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidApplicationGradlePluginExtension.class;
	}
}

public class AndroidApplicationGradlePluginExtension extends AndroidGradlePluginExtension {

}
