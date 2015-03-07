package com.jdroid.gradle.javaweb

import com.jdroid.gradle.java.JavaGradlePlugin
import com.jdroid.gradle.javaweb.task.CopyJettyRunnerTask
import com.jdroid.gradle.javaweb.task.HerokuBuildTask
import org.gradle.api.Project

public class JavaWebGradlePlugin extends JavaGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.apply plugin: 'jetty'

		project.configurations {
			runtimeOnly
		}

		project.dependencies {
			// TODO Remove hardcoded version
			compile 'com.jdroidframework:jdroid-javaweb:0.7.0-SNAPSHOT'

			// to run our App on Heroku
			runtimeOnly "org.mortbay.jetty:jetty-runner:8.1.1.v20120215"
		}


		project.task('copyJettyRunner', type: CopyJettyRunnerTask)
		project.task('stage', type: HerokuBuildTask)
		project.tasks.'stage'.dependsOn 'assemble', 'copyJettyRunner'
	}

	protected Class<? extends JavaWebGradleExtension> getExtensionClass() {
		return JavaWebGradleExtension.class;
	}
}