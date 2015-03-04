package com.jdroid.gradle.commons

import org.gradle.api.Plugin
import org.gradle.api.Project

public class BaseGradlePlugin implements Plugin<Project> {

	protected Project project;

	public void apply(Project project) {
		this.project = project

		project.extensions.create("jdroid", getExtensionClass(), this)
	}

	protected Class<? extends BaseGradleExtension> getExtensionClass() {
		return BaseGradleExtension.class;
	}
}