package com.jdroid.gradle.commons.tasks

import org.gradle.api.tasks.TaskAction

public class IncrementPatchVersionTask extends AbstractIncrementVersionTask {

	public IncrementPatchVersionTask() {
		description = 'Increments the patch version (X.X.X+1)'
	}

	@TaskAction
	public void doExecute() {
		project.jdroid.versionPatch = changeVersion("versionPatch", null)
		commitVersionChange()
	}
}
