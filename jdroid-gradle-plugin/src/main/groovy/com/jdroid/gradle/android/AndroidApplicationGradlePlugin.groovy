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

		android.defaultConfig {
			versionCode generateVersionCode()
			versionName project.version

			jackOptions {
				enabled jdroid.getBooleanProp('JACK_ENABLED', false)
			}
		}

		android.signingConfigs {

			debug {
				storeFile project.file('./debug.keystore')
			}

			release {
				storeFile project.file(jdroid.getProp('STORE_FILE', './debug.keystore'))
				storePassword jdroid.getProp('STORE_PASSWORD')
				keyAlias jdroid.getProp('KEY_ALIAS')
				keyPassword jdroid.getProp('KEY_PASSWORD')
			}
		}
	}

	protected Integer generateVersionCode() {
		Integer versionCodePrefix = jdroid.versionCodePrefix
		if (versionCodePrefix == null) {
			versionCodePrefix = minimumSdkVersion
		}
		return versionCodePrefix * 10000000 + jdroid.versionCodeExtraBit * 1000000 + jdroid.versionMajor * 10000 + jdroid.versionMinor * 100 + jdroid.versionPatch
	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidApplicationGradlePluginExtension.class;
	}

	protected void applyAndroidPlugin() {
		project.apply plugin: AppPlugin
	}
}
