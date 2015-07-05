package com.jdroid.sample.android.ui.about;

import com.jdroid.android.about.AboutContext;
import com.jdroid.android.fragment.AbstractFragment;

public class AndroidAboutContext extends AboutContext {

	@Override
	public Class<? extends AbstractFragment> getSpreadTheLoveFragmentClass() {
		return AndroidSpreadTheLoveFragment.class;
	}
}
