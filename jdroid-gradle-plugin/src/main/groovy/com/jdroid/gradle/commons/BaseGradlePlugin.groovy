package com.jdroid.gradle.commons

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

public class BaseGradlePlugin implements Plugin<Project> {

	protected Project project;

	public void apply(Project project) {
		this.project = project

		project.extensions.create("jdroid", getExtensionClass(), this)

		project.task('incrementMajorVersion', type: IncrementMajorVersionTask)
		project.task('incrementMinorVersion', type: IncrementMinorVersionTask)
		project.task('incrementPatchVersion', type: IncrementPatchVersionTask)

		project.task('buildScriptDependencies') << {
			project.buildscript.configurations.classpath.asPath.split(':').each {
				println it
			}
		}

		project.afterEvaluate {
			project.tasks.withType(Test) {
				scanForTestClasses = true

				if (project.jdroid.getProp('INTEGRATION_TESTS_ENABLED', true) == 'false') {
					exclude project.jdroid.integrationTestsPattern
				}
			}
		}
	}

	protected Class<? extends BaseGradleExtension> getExtensionClass() {
		return BaseGradleExtension.class;
	}
}