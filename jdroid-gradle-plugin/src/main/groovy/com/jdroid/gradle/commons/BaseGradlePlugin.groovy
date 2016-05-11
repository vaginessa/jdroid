package com.jdroid.gradle.commons

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import com.jdroid.gradle.commons.tasks.IncrementMajorVersionTask
import com.jdroid.gradle.commons.tasks.IncrementMinorVersionTask
import com.jdroid.gradle.commons.tasks.IncrementPatchVersionTask

public class BaseGradlePlugin implements Plugin<Project> {

	protected Project project;

	protected jdroid

	public void apply(Project project) {
		this.project = project

		project.extensions.create("jdroid", getExtensionClass(), this)
		jdroid = project.jdroid
		project.version = jdroid.generateVersionName()

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

				if (!jdroid.getBooleanProp('INTEGRATION_TESTS_ENABLED', true)) {
					exclude jdroid.integrationTestsPattern
				}
			}
		}
	}

	protected Class<? extends BaseGradleExtension> getExtensionClass() {
		return BaseGradleExtension.class;
	}
}