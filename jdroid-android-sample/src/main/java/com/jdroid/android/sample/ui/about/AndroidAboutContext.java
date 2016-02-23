package com.jdroid.android.sample.ui.about;

import com.jdroid.android.about.AboutContext;
import com.jdroid.android.fragment.AbstractFragment;

public class AndroidAboutContext extends AboutContext {

	@Override
	public Class<? extends AbstractFragment> getSpreadTheLoveFragmentClass() {
		return AndroidSpreadTheLoveFragment.class;
	}

	@Override
	public Boolean isBetaTestingEnabled() {
		return true;
	}
}
