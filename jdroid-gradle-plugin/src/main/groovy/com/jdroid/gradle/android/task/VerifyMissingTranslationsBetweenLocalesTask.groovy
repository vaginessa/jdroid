package com.jdroid.gradle.android.task
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.TaskAction

import java.util.regex.Matcher
import java.util.regex.Pattern

public class VerifyMissingTranslationsBetweenLocalesTask extends DefaultTask {

	public VerifyMissingTranslationsBetweenLocalesTask() {
		description = 'Verify if there are missing translations between locales'
		group = JavaBasePlugin.VERIFICATION_GROUP
	}

	@TaskAction
	public void doExecute() {

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

	private String getKey(String it) {
		String key = null;
		if (it.trim().matches('[^!]* name="[^"]*">.*')) {
			Matcher matcher = Pattern.compile('name="([^"]*)">').matcher(it.trim());
			matcher.find();
			key = matcher.group(1)
		}
		return key;
	}
}
