package com.jdroid.java.context;

import com.jdroid.java.utils.PropertiesUtils;

public class GitContext {
	
	private static final String GIT_PROPERTIES_RESOURCE_NAME = "git.properties";
	
	private String commitTime;
	private String buildTime;
	private String commitId;
	private String branch;
	
	private static GitContext INSTANCE;
	
	private GitContext() {
		PropertiesUtils.loadProperties(GIT_PROPERTIES_RESOURCE_NAME);
		
		commitTime = PropertiesUtils.getStringProperty("git.commit.time");
		buildTime = PropertiesUtils.getStringProperty("git.build.time");
		commitId = PropertiesUtils.getStringProperty("git.commit.id");
		branch = PropertiesUtils.getStringProperty("git.branch");
	}
	
	/**
	 * @return The {@link GitContext} instance
	 */
	public static GitContext get() {
		if (INSTANCE == null) {
			INSTANCE = new GitContext();
		}
		return INSTANCE;
	}
	
	public String getCommitTime() {
		return commitTime;
	}
	
	public String getBuildTime() {
		return buildTime;
	}
	
	public String getCommitId() {
		return commitId;
	}
	
	public String getBranch() {
		return branch;
	}
}
