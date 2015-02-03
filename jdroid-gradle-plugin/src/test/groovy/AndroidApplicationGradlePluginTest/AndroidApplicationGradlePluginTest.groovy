package AndroidApplicationGradlePluginTest;

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

public class AndroidApplicationGradlePluginTest {

	@Test
	public void countMethodsTest() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'com.jdroid.android.application'

		project.tasks.countMethods.execute()
	}
}
