package com.jdroid.gradle.android

import org.junit.Test
import static org.junit.Assert.*

public class AndroidGradlePluginTest {

//	@Test
//	public void verifyMissingTranslationsBetweenLocalesTest() {
//		Project project = ProjectBuilder.builder().build()
//		project.apply plugin: 'com.jdroid.android.application'
//
//		project.tasks.verifyMissingTranslationsBetweenLocales.execute()
//	}

	@Test
	public void getKey() {

		AndroidApplicationGradlePlugin plugin = new AndroidApplicationGradlePlugin()

		assertResKey(plugin, ' <string name="key">value</string>')
		assertResKey(plugin, '<string name="key">value</string>')
		assertResKey(plugin, '<string name="key">value</string> ')
		assertResKey(plugin, '<string-array name="key">')
		assertNullResKey(plugin, '<item>0</item>')
		assertResKey(plugin, '<plurals name="key">')
		assertNullResKey(plugin, '<?xml version="1.0" encoding="utf-8"?>')
		assertNullResKey(plugin, '<resources>')
		assertNullResKey(plugin, '<item quantity="one">1 pago</item>')
		assertNullResKey(plugin, '<!-- Menu -->')
		assertNullResKey(plugin, '<!--<string name="key">value</string>-->')

	}

	private void assertResKey(AndroidApplicationGradlePlugin plugin, String fileLine) {
		assertEquals 'key', plugin.getKey(fileLine)
	}

	private void assertNullResKey(AndroidApplicationGradlePlugin plugin, String fileLine) {
		assertNull plugin.getKey(fileLine)
	}

}
