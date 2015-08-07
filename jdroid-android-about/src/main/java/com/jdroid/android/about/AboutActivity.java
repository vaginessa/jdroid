package com.jdroid.android.about;

import android.support.v4.app.Fragment;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.activity.FragmentContainerActivity;

public class AboutActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return AbstractApplication.get().getAboutContext().getAboutFragmentClass();
	}
	
}
