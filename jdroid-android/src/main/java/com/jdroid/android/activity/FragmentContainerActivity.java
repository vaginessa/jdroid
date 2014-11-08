package com.jdroid.android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.jdroid.android.R;

public abstract class FragmentContainerActivity extends AbstractFragmentActivity {
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		if (isNavDrawerEnabled()) {
			return isNavDrawerOverlayEnabled() ? R.layout.nav_overlay_fragment_container_activity
					: R.layout.nav_fragment_container_activity;
		}
		return R.layout.fragment_container_activity;
	}
	
	public Boolean isNavDrawerOverlayEnabled() {
		return false;
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			Fragment fragment = createNewFragment();
			fragmentTransaction.add(R.id.fragmentContainer, fragment, fragment.getClass().getSimpleName());
			if (addToBackStack()) {
				fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
			}
			fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.commit();
		}
	}
	
	protected Boolean addToBackStack() {
		return false;
	}
	
	@Override
	public void addFragment(Fragment newFragment, int containerId, boolean addToBackStack) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(containerId, newFragment, newFragment.getClass().getSimpleName());
		if (addToBackStack) {
			fragmentTransaction.addToBackStack(newFragment.getClass().getSimpleName());
		}
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
	public void replaceFragment(Fragment newFragment) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.fragmentContainer, newFragment);
		if (addToBackStack()) {
			fragmentTransaction.addToBackStack(newFragment.getClass().getSimpleName());
		}
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
	protected Fragment createNewFragment() {
		return instanceFragment(getFragmentClass(), getIntent().getExtras());
	}
	
	protected Class<? extends Fragment> getFragmentClass() {
		return null;
	}
	
	protected int getTransition() {
		return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
	}
	
	public Fragment getFragment() {
		return getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
	}
}
