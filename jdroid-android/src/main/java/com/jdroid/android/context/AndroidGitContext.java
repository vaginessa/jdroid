package com.jdroid.android.context;

import com.jdroid.android.AbstractApplication;
import com.jdroid.java.context.GitContext;

public class AndroidGitContext implements GitContext {

	@Override
	public String getBranch() {
		return AbstractApplication.get().getBuildConfigValue("GIT_BRANCH");
	}

	@Override
	public String getSha() {
		return AbstractApplication.get().getBuildConfigValue("GIT_SHA");
	}
}
