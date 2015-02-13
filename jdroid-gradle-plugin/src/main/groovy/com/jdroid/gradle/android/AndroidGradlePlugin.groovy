package com.jdroid.gradle.android
import com.jdroid.gradle.android.task.VerifyMissingTranslationsBetweenLocalesTask
import com.jdroid.gradle.android.task.VerifyMissingTranslationsTask
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

public abstract class AndroidGradlePlugin implements Plugin<Project> {

	protected Project project;

	public void apply(Project project) {
		this.project = project

		project.extensions.create("jdroid", getExtensionClass())

		applyAndroidPlugin()

		def android = project.extensions.findByName("android")

		android.compileSdkVersion 21
		android.buildToolsVersion "21.1.1"

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
		project.task('verifyMissingTranslations', type: VerifyMissingTranslationsTask)
	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidGradlePluginExtension.class;
	}

	protected abstract void applyAndroidPlugin();
}



public class AndroidGradlePluginExtension {

	String[] resourcesDirsPaths = ['src/main/res/']
	String[] notDefaultLanguages = []

	public String gitSha() {
		return 'git rev-parse --short HEAD'.execute().text.trim()
	}

	public String buildTime() {
		def df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
		df.setTimeZone(TimeZone.getDefault())
		return df.format(new Date())
	}

	public String branch() {
		return 'git symbolic-ref HEAD'.execute().text.trim().replaceAll(".*/", "")
	}

	public void setString(def flavor, String key, def value, String defaultValue) {
		value = value == "null" ? defaultValue : value
		flavor.buildConfigField "String", key, value
	}

	public void setBoolean(def flavor, String key, def value, def defaultValue) {
		value = value == "null" ? defaultValue : value
		flavor.buildConfigField "boolean", key, value
	}

	public void setInteger(def flavor, String key, def value, def defaultValue) {
		value = value == "null" ? defaultValue : value
		flavor.buildConfigField "Integer", key, value
	}
}

