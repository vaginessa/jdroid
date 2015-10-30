package com.jdroid.gradle.javaweb
import com.jdroid.gradle.java.JavaGradlePlugin
import org.gradle.api.Project

public class JavaWebGradlePlugin extends JavaGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.apply plugin: 'jetty'
	}

	protected Class<? extends JavaWebGradleExtension> getExtensionClass() {
		return JavaWebGradleExtension.class;
	}
}