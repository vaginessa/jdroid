package com.jdroid.gradle.android.task
import org.gradle.api.DefaultTask
import org.gradle.api.Project

public class AbstractIncrementVersionTask extends DefaultTask {

	protected def changeVersion(Project project, def versionType, def newVersion) {
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

	protected void commitVersionChange() {
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
