package com.jdroid.gradle.javaweb.task
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

public class HerokuBuildTask extends DefaultTask {

	public HerokuBuildTask() {
		description = 'Build the app for Heroku'
	}

	@TaskAction
	public void doExecute() {
		println 'Building war for Heroku'
	}
}
