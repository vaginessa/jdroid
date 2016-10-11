package com.jdroid.android.context;

import com.jdroid.java.context.GitContext;

public class AndroidGitContext extends AbstractAppContext implements GitContext {

	@Override
	public String getBranch() {
		return getBuildConfigValue("GIT_BRANCH");
	}

	@Override
	public String getSha() {
		return getBuildConfigValue("GIT_SHA");
	}
}
