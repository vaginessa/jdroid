package com.jdroid.gradle.java
import com.jdroid.gradle.commons.BaseGradlePlugin
import org.gradle.api.Project

public class JavaGradlePlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)
	}

	protected Class<? extends JavaGradleExtension> getExtensionClass() {
		return JavaGradleExtension.class;
	}
}