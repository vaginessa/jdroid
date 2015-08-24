package com.jdroid.android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.jdroid.android.R;
import com.jdroid.android.fragment.FragmentIf;

public abstract class FragmentContainerActivity extends AbstractFragmentActivity {
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return isNavDrawerEnabled() ? R.layout.nav_fragment_container_activity : R.layout.fragment_container_activity;
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
	
	protected Fragment createNewFragment() {
		return instanceFragment(getFragmentClass(), getIntent().getExtras());
	}
	
	protected Class<? extends Fragment> getFragmentClass() {
		return null;
	}
	
	public Fragment getFragment() {
		return getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
	}

	@Override
	public Integer getMenuResourceId() {
		Integer menuResourceId = super.getMenuResourceId();
		if (menuResourceId == null) {
			Fragment fragment = getFragment();
			if (fragment != null && fragment instanceof FragmentIf) {
				return ((FragmentIf)fragment).getMenuResourceId();
			}
		}
		return menuResourceId;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item) || getFragment().onOptionsItemSelected(item);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		Fragment fragment = getFragment();
		if (fragment != null) {
			fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}


}
