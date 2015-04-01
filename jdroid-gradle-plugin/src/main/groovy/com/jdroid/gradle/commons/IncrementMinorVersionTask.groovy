package com.jdroid.gradle.commons

import org.gradle.api.tasks.TaskAction

public class IncrementMinorVersionTask extends AbstractIncrementVersionTask {

	public IncrementMinorVersionTask() {
		description = 'Increments the minor version (X.X+1.X)'
	}

	@TaskAction
	public void doExecute() {
		project.jdroid.versionMinor = changeVersion("versionMinor", null)
		project.jdroid.versionPatch = changeVersion("versionPatch", 0)
		commitVersionChange()
	}
}
