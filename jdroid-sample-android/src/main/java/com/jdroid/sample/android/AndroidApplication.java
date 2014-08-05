package com.jdroid.sample.android;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.gcm.GcmMessageResolver;
import com.jdroid.sample.android.gcm.AndroidGcmResolver;
import com.jdroid.sample.android.ui.HomeActivity;

public class AndroidApplication extends AbstractApplication {
	
	public static AndroidApplication get() {
		return (AndroidApplication)AbstractApplication.INSTANCE;
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#getHomeActivityClass()
	 */
	@Override
	public Class<? extends Activity> getHomeActivityClass() {
		return HomeActivity.class;
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#createActivityHelper(android.app.Activity)
	 */
	@Override
	public ActivityHelper createActivityHelper(Activity activity) {
		return new AndroidActivityHelper(activity);
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#createFragmentHelper(android.support.v4.app.Fragment)
	 */
	@Override
	public FragmentHelper createFragmentHelper(Fragment fragment) {
		return new AndroidFragmentHelper(fragment);
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#getGcmResolver()
	 */
	@Override
	public GcmMessageResolver getGcmResolver() {
		return AndroidGcmResolver.get();
	}
}
