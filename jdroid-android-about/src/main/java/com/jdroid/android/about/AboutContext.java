package com.jdroid.android.about;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.LocalizationUtils;
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

	public Boolean isBetaTestingEnabled() {
		return false;
	}

	public String getBetaTestingUrl() {
		return "https://play.google.com/apps/testing/" + AppUtils.getApplicationId();
	}

	public String getAppInviteTitle() {
		return LocalizationUtils.getString(R.string.jdroid_appInviteTitle, LocalizationUtils.getString(R.string.jdroid_appName));
	}

	public String getAppInviteMessage() {
		return LocalizationUtils.getString(R.string.jdroid_appInviteMessage, LocalizationUtils.getString(R.string.jdroid_appName));
	}

	public String getAppInviteDeeplink() {
		return AbstractApplication.get().getAppContext().getWebsite();
	}

}
