package com.jdroid.gradle.commons.tasks

import org.gradle.api.tasks.TaskAction

public class IncrementMajorVersionTask extends AbstractIncrementVersionTask {

	public IncrementMajorVersionTask() {
		description = 'Increments the major version (X+1.X.X)'
	}

	@TaskAction
	public void doExecute() {
		project.jdroid.versionMajor = changeVersion("versionMajor", null)
		project.jdroid.versionMinor = changeVersion("versionMinor", 0)
		project.jdroid.versionPatch = changeVersion("versionPatch", 0)
		commitVersionChange()
	}
}
