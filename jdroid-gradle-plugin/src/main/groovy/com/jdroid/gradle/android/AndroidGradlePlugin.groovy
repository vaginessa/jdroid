package com.jdroid.gradle.android
import com.jdroid.gradle.android.task.VerifyMissingTranslationsBetweenLocalesTask
import com.jdroid.gradle.android.task.VerifyMissingTranslationsTask
import com.jdroid.gradle.commons.BaseGradlePlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Project

public abstract class AndroidGradlePlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		applyAndroidPlugin()

		def android = project.extensions.findByName("android")

		android.compileSdkVersion 21
		android.buildToolsVersion "21.1.2"

		android.defaultConfig {
			minSdkVersion 14
			targetSdkVersion 21
		}

		android.compileOptions {
			sourceCompatibility JavaVersion.VERSION_1_7
			targetCompatibility JavaVersion.VERSION_1_7
		}

		android.lintOptions {
			checkReleaseBuilds false
			// Or, if you prefer, you can continue to check for errors in release builds,
			// but continue the build even when errors are found:
			abortOnError false
		}

		android.packagingOptions {
			exclude 'META-INF/LICENSE'
			exclude 'META-INF/NOTICE'
		}

		project.task('verifyMissingTranslationsBetweenLocales', type: VerifyMissingTranslationsBetweenLocalesTask)
		project.tasks.'check'.dependsOn 'verifyMissingTranslationsBetweenLocales'

		project.task('verifyMissingTranslations', type: VerifyMissingTranslationsTask)

	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidGradlePluginExtension.class;
	}

	protected abstract void applyAndroidPlugin();
}


