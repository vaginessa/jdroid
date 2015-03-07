package com.jdroid.gradle.javaweb.task
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

public class CopyJettyRunnerTask extends DefaultTask {

	public CopyJettyRunnerTask() {
		description = 'Copy the jetty runner jar to the build/lib dir'
	}

	@TaskAction
	public void doExecute() {
		project.copy {
			from project.configurations.runtimeOnly.copy().setTransitive(false)
			into project.buildDir.getAbsolutePath() + "/libs"
			rename { name ->
				def artifacts = project.configurations.runtimeOnly.resolvedConfiguration.resolvedArtifacts
				def artifact = artifacts.find { it.file.name == name }
				"${artifact.name}.${artifact.extension}"
			}
		}
	}
}
