package com.jdroid.android.debug;

import android.app.FragmentTransaction;
import android.os.Bundle;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.java.exception.UnexpectedException;

/**
 * Debug Settings Activity just for debug purposes. These settings are disabled on the production environment
 * 
 * @author Maxi Rosson
 */
public class DebugSettingsActivity extends AbstractFragmentActivity {
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return isNavDrawerEnabled() ? R.layout.nav_fragment_container_activity : R.layout.fragment_container_activity;
	}
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.add(R.id.fragmentContainer, createNewFragment());
			fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.commit();
		}
	}
	
	protected AbstractPreferenceFragment createNewFragment() {
		return instanceAbstractPreferenceFragment(AbstractApplication.get().getDebugSettingsFragmentClass(),
			getIntent().getExtras());
	}
	
	private <E extends AbstractPreferenceFragment> E instanceAbstractPreferenceFragment(Class<E> fragmentClass,
			Bundle bundle) {
		E fragment = null;
		try {
			fragment = fragmentClass.newInstance();
		} catch (InstantiationException e) {
			throw new UnexpectedException(e);
		} catch (IllegalAccessException e) {
			throw new UnexpectedException(e);
		}
		fragment.setArguments(bundle);
		return fragment;
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#getMenuResourceId()
	 */
	@Override
	public Integer getMenuResourceId() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractPreferenceActivity#requiresAuthentication()
	 */
	@Override
	public Boolean requiresAuthentication() {
		return false;
	}
}