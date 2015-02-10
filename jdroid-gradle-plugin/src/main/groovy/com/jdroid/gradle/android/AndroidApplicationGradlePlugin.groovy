package com.jdroid.gradle.android
import com.android.build.gradle.AppPlugin
import org.gradle.api.GradleException
import org.gradle.api.Project
public class AndroidApplicationGradlePlugin extends AndroidGradlePlugin {

	public void apply(Project project) {
		super.apply(project);

		project.task('countMethods') << {

			description = 'Output per-package method counts in an Android DEX executable grouped by package.'
			group = JavaBasePlugin.VERIFICATION_GROUP

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

		// Increment version tasks

		project.task('incrementMajorVersion') << {

			description = 'Increments the major version (X+1.X.X) '

			project.jdroid.versionMajor = changeVersion(project, "versionMajor", null)
			project.jdroid.versionMinor = changeVersion(project, "versionMinor", 0)
			project.jdroid.versionPatch = changeVersion(project, "versionPatch", 0)
			commitVersionChange(project)
		}

		project.task('incrementMinorVersion') << {

			description = 'Increments the minor version (X.X+1.X)'

			project.jdroid.versionMinor = changeVersion(project, "versionMinor", null)
			project.jdroid.versionPatch = changeVersion(project, "versionPatch", 0)
			commitVersionChange(project)
		}

		project.task('incrementPatchVersion') << {

			description = 'Increments the patch version (X.X.X+1)'

			project.jdroid.versionPatch = changeVersion(project, "versionPatch", null)
			commitVersionChange(project)
		}

	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidApplicationGradlePluginExtension.class;
	}

	protected void applyAndroidPlugin() {
		project.apply plugin: AppPlugin
	}

	def changeVersion(Project project, def versionType, def newVersion) {
		def file = project.file("./build.gradle")
		def patternVersionNumber = java.util.regex.Pattern.compile(versionType + " = (\\d+)")
		def manifestText = file.getText()
		def matcherVersionNumber = patternVersionNumber.matcher(manifestText)
		matcherVersionNumber.find()
		def currentVersion = Integer.parseInt(matcherVersionNumber.group(1))
		if (newVersion == null) {
			newVersion = currentVersion + 1
		}
		def fileContent = matcherVersionNumber.replaceAll(versionType + " = " + newVersion)
		file.write(fileContent)
		return newVersion
	}

	void commitVersionChange(Project project) {
		project.exec {
			commandLine 'git', 'diff', 'HEAD'
		}

		project.exec {
			commandLine 'git', 'add', '-A'
		}

		project.exec {
			commandLine 'git', 'commit', '-m', "Changed app version to v${project.jdroid.versionMajor}.${project.jdroid.versionMinor}.${project.jdroid.versionPatch}"
		}
	}
}

import org.gradle.api.plugins.JavaBasePlugin

public class AndroidApplicationGradlePluginExtension extends AndroidGradlePluginExtension {

	def versionMajor
	def versionMinor
	def versionPatch

}
