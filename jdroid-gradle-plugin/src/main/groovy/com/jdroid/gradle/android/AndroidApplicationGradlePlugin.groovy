package com.jdroid.gradle.android
import com.android.build.gradle.AppPlugin
import com.jdroid.gradle.android.task.CopyApksTask
import com.jdroid.gradle.android.task.CountMethodsReportTask
import com.jdroid.gradle.android.task.CountMethodsSummaryTask
import com.jdroid.gradle.android.task.GooglePlayPublisherTask
import org.gradle.api.Project

public class AndroidApplicationGradlePlugin extends AndroidGradlePlugin {

	public void apply(Project project) {
		super.apply(project);

		project.task('countMethodsReport', type: CountMethodsReportTask)
		project.task('countMethodsSummary', type: CountMethodsSummaryTask)

		project.task('googlePlayPublish', type: GooglePlayPublisherTask)

		project.task('copyApks', type: CopyApksTask)
	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidApplicationGradlePluginExtension.class;
	}

	protected void applyAndroidPlugin() {
		project.apply plugin: AppPlugin
	}
}
