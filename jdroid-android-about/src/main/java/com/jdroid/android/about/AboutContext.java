package com.jdroid.android.about;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.utils.ReflectionUtils;

public class AboutContext {

	@NonNull
	@SuppressWarnings("unchecked")
	public Class<? extends AbstractFragment> getAboutFragmentClass() {
		return (Class<? extends AbstractFragment>)ReflectionUtils.getClass("com.jdroid.android.about.AboutFragment");
	}

	@NonNull
	@SuppressWarnings("unchecked")
	public Class<? extends AbstractFragment> getLibrariesFragmentClass() {
		return (Class<? extends AbstractFragment>)ReflectionUtils.getClass("com.jdroid.android.about.LibrariesFragment");
	}

	@Nullable
	public Class<? extends AbstractFragment> getSpreadTheLoveFragmentClass() {
		return null;
	}

}
