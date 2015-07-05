package com.jdroid.android.about;

import android.support.v4.app.Fragment;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.activity.FragmentContainerActivity;

public class SpreadTheLoveActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return AbstractApplication.get().getAboutContext().getSpreadTheLoveFragmentClass();
	}
}
