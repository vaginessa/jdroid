package com.jdroid.javaweb.context;

import com.jdroid.java.context.GitContext;

public class ServerGitContext implements GitContext {

	private String sha;
	private String branch;

	public String getSha() {
		return sha;
	}

	public String getBranch() {
		return branch;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}
}
