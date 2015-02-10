package com.jdroid.gradle.android
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin

import java.util.regex.Matcher
import java.util.regex.Pattern

public abstract class AndroidGradlePlugin implements Plugin<Project> {

	public void apply(Project project) {
		project.extensions.create("jdroid", getExtensionClass())

		project.task('verifyMissingTranslationsBetweenLocales') << {

			description = 'Verify if there are missing translations between locales'
			group = JavaBasePlugin.VERIFICATION_GROUP

			Boolean error = false;

			for (String resourceDirPath in project.jdroid.resourcesDirsPaths) {

				File defaultLanguageValuesDir = project.file(resourceDirPath + "values")
				for (String language in project.jdroid.notDefaultLanguages) {

					File notDefaultLanguageValuesDir = project.file(resourceDirPath + "values-" + language)
					String[] resTypesNames = ['strings.xml', 'plurals.xml', 'array.xml']
					for (resTypeName in resTypesNames) {

						File defaultLanguageValuesFile = new File(defaultLanguageValuesDir.getAbsolutePath() + File.separator + resTypeName)
						File notDefaultLanguageValuesFile = new File(notDefaultLanguageValuesDir.getAbsolutePath() + File.separator + resTypeName)

						if (defaultLanguageValuesFile.exists()) {
							if (!notDefaultLanguageValuesFile.exists()) {
								println 'Missing resources file ' + notDefaultLanguageValuesFile.getAbsolutePath()
								error = true
							} else {
								def defaultLanguageKeys = []
								defaultLanguageValuesFile.eachLine {
									String key = getKey(it)
									if (key != null) {
										defaultLanguageKeys.add(key)
									}
								}

								def notDefaultLanguageKeys = []
								notDefaultLanguageValuesFile.eachLine {
									String key = getKey(it)
									if (key != null) {
										notDefaultLanguageKeys.add(key)
									}
								}

								def commons = defaultLanguageKeys.intersect(notDefaultLanguageKeys)

								defaultLanguageKeys.removeAll(commons)
								if (!defaultLanguageKeys.isEmpty()) {
									println "The following keys are missing on " + notDefaultLanguageValuesFile.getAbsolutePath()
									println "* " + defaultLanguageKeys
									println ""
									error = true
								}

								notDefaultLanguageKeys.removeAll(commons)
								if (!notDefaultLanguageKeys.isEmpty()) {
									println "The following keys are missing on " + defaultLanguageValuesFile.getAbsolutePath()
									println "* " + notDefaultLanguageKeys
									println ""
									error = true
								}

								if (!error) {
									println "The following i19n files match:"
									println "* " + defaultLanguageValuesFile.getAbsolutePath()
									println "* " + notDefaultLanguageValuesFile.getAbsolutePath()
									println ""
								}
							}
						} else {
							println "Ignoring the following file because it doesn't exist: " + defaultLanguageValuesFile.getAbsolutePath()
						}
					}
				}
			}

			if (error) {
				println "Remember that the i19n files should have the same keys on the same lines. If you don't have the translation for any language, please add the key on all the files, and 'TODO' as value"
				throw new GradleException("The translations between locales doesn't match")
			}

		}

		project.task('verifyMissingTranslations') << {

			description = 'Verify if there are missing translations ("TODO") on any string resource.'
			group = JavaBasePlugin.VERIFICATION_GROUP

			Boolean error = false;

			for (String resourceDirPath in project.jdroid.resourcesDirsPaths) {
				File resDirFile = project.file(resourceDirPath)
				for (file in resDirFile.listFiles()) {
					if (file.isDirectory() && file.getName().startsWith("values")) {
						String[] resTypesNames = ['strings.xml', 'plurals.xml', 'array.xml']
						for (resTypesName in resTypesNames) {
							String resourceFilePath = file.getAbsolutePath() + File.separator + resTypesName
							File resourceFile = new File(resourceFilePath)
							if (resourceFile.exists()) {
								if (resourceFile.text.contains("TODO")) {
									println 'Missing translations (TODO) on ' + resourceFilePath
									error = true
								} else {
									println "Not Missing translations (TODO) on " + resourceFilePath
								}
							}
						}
					}
				}
			}

			if (error) {
				throw new GradleException()
			}
		}
	}

	public String getKey(String it) {
		String key = null;
		if (it.trim().matches('[^!]* name="[^"]*">.*')) {
			Matcher matcher = Pattern.compile('name="([^"]*)">').matcher(it.trim());
			matcher.find();
			key = matcher.group(1)
		}
		return key;
	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidGradlePluginExtension.class;
	}
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
		def branch = hasProperty('gitBranch') ? gitBranch : 'git symbolic-ref HEAD'.execute().text.trim()
		return branch.replaceAll(".*/", "")
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

