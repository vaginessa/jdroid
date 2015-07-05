package com.jdroid.android.about;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.utils.ReflectionUtils;

public class AboutContext {

	public Class<? extends AbstractFragment> getAboutFragmentClass() {
		return (Class<? extends AbstractFragment>)ReflectionUtils.getClass("com.jdroid.android.about.AboutFragment");
	}

	public Class<? extends AbstractFragment> getLibrariesFragmentClass() {
		return (Class<? extends AbstractFragment>)ReflectionUtils.getClass("com.jdroid.android.about.LibrariesFragment");
	}

	public Class<? extends AbstractFragment> getSpreadTheLoveFragmentClass() {
		return null;
	}

}
