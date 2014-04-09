package com.jdroid.android.activity;

import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.gms.ads.AdSize;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.fragment.UseCaseFragment;
import com.jdroid.android.loading.LoadingDialogBuilder;
import com.jdroid.java.exception.UnexpectedException;

/**
 * Base {@link Activity}
 * 
 */
public abstract class AbstractFragmentActivity extends FragmentActivity implements ActivityIf {
	
	private ActivityHelper activityHelper;
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAppContext()
	 */
	@Override
	public AppContext getAppContext() {
		return activityHelper.getAppContext();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onBeforeSetContentView()
	 */
	@Override
	public Boolean onBeforeSetContentView() {
		return activityHelper.onBeforeSetContentView();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onAfterSetContentView(android.os.Bundle)
	 */
	@Override
	public void onAfterSetContentView(Bundle savedInstanceState) {
		activityHelper.onAfterSetContentView(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityHelper = AbstractApplication.get().createActivityHelper(this);
		activityHelper.beforeOnCreate();
		super.onCreate(savedInstanceState);
		activityHelper.onCreate(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onContentChanged()
	 */
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		activityHelper.onContentChanged();
	}
	
	/**
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		activityHelper.onSaveInstanceState(outState);
	}
	
	/**
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		activityHelper.onRestoreInstanceState(savedInstanceState);
	}
	
	/**
	 * @see roboguice.activity.GuiceActivity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		activityHelper.onStart();
	}
	
	/**
	 * @see roboguice.activity.GuiceActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		activityHelper.onResume();
	}
	
	/**
	 * @see roboguice.activity.RoboActivity#onPause()
	 */
	@Override
	protected void onPause() {
		activityHelper.onBeforePause();
		super.onPause();
		activityHelper.onPause();
	}
	
	/**
	 * @see roboguice.activity.RoboActivity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		activityHelper.onStop();
	}
	
	/**
	 * @see roboguice.activity.RoboActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		activityHelper.onBeforeDestroy();
		super.onDestroy();
		activityHelper.onDestroy();
	}
	
	/**
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		activityHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return activityHelper.onCreateOptionsMenu(menu);
	}
	
	/**
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		activityHelper.onPrepareOptionsMenu(menu);
		return super.onPrepareOptionsMenu(menu);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getMenuResourceId()
	 */
	@Override
	public Integer getMenuResourceId() {
		return activityHelper.getMenuResourceId();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#doOnCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public void doOnCreateOptionsMenu(Menu menu) {
		activityHelper.doOnCreateOptionsMenu(menu);
	}
	
	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return activityHelper.onOptionsItemSelected(item) ? true : super.onOptionsItemSelected(item);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findView(int)
	 */
	@Override
	public <V extends View> V findView(int id) {
		return activityHelper.findView(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showBlockingLoading()
	 */
	@Override
	public void showBlockingLoading() {
		activityHelper.showBlockingLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showBlockingLoading(com.jdroid.android.loading.LoadingDialogBuilder)
	 */
	@Override
	public void showBlockingLoading(LoadingDialogBuilder builder) {
		activityHelper.showBlockingLoading(builder);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissBlockingLoading()
	 */
	@Override
	public void dismissBlockingLoading() {
		activityHelper.dismissBlockingLoading();
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return activityHelper.inflate(resource);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return activityHelper.getInstance(clazz);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getExtra(java.lang.String)
	 */
	@Override
	public <E> E getExtra(String key) {
		return activityHelper.<E>getExtra(key);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#requiresAuthentication()
	 */
	@Override
	public Boolean requiresAuthentication() {
		return activityHelper.requiresAuthentication();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getUser()
	 */
	@Override
	public User getUser() {
		return activityHelper.getUser();
	}
	
	public Boolean isAuthenticated() {
		return activityHelper.isAuthenticated();
	}
	
	public void loadUseCaseFragment(Bundle savedInstanceState, Class<? extends UseCaseFragment<?>> useCaseFragmentClass) {
		
		if (savedInstanceState == null) {
			try {
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.add(useCaseFragmentClass.newInstance(), useCaseFragmentClass.getSimpleName());
				fragmentTransaction.commit();
			} catch (InstantiationException e) {
				throw new UnexpectedException(e);
			} catch (IllegalAccessException e) {
				throw new UnexpectedException(e);
			}
		}
	}
	
	public void removeUseCaseFragment(Class<? extends UseCaseFragment<?>> useCaseFragmentClass) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.remove(getUseCaseUseCaseFragment(useCaseFragmentClass));
		fragmentTransaction.commit();
	}
	
	public UseCaseFragment<?> getUseCaseUseCaseFragment(Class<? extends UseCaseFragment<?>> useCaseFragmentClass) {
		return (UseCaseFragment<?>)getSupportFragmentManager().findFragmentByTag(useCaseFragmentClass.getSimpleName());
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Fragment> E getFragment(Class<E> fragmentClass) {
		return (E)getSupportFragmentManager().findFragmentByTag(fragmentClass.getSimpleName());
	}
	
	public void addFragment(Fragment newFragment, int containerId, boolean addToBackStack) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(containerId, newFragment, newFragment.getClass().getSimpleName());
		if (addToBackStack) {
			fragmentTransaction.addToBackStack(newFragment.getClass().getSimpleName());
		}
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
	public <E extends Fragment> E instanceFragment(Class<E> fragmentClass, Bundle bundle) {
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
	
	public void commitFragment(Fragment fragment) {
		commitFragment(R.id.fragmentContainer, fragment);
	}
	
	public void commitFragment(int containerViewId, Fragment fragment) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(containerViewId, fragment);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return activityHelper.getAdSize();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isLauncherActivity()
	 */
	@Override
	public Boolean isLauncherActivity() {
		return activityHelper.isLauncherActivity();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getLocationFrequency()
	 */
	@Override
	public Long getLocationFrequency() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isNavDrawerEnabled()
	 */
	@Override
	public Boolean isNavDrawerEnabled() {
		return activityHelper.isNavDrawerEnabled();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isNavDrawerTopLevelView()
	 */
	@Override
	public Boolean isNavDrawerTopLevelView() {
		return activityHelper.isNavDrawerTopLevelView();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContextualMenuItemsIds()
	 */
	@Override
	public List<Integer> getContextualMenuItemsIds() {
		return activityHelper.getContextualMenuItemsIds();
	}
	
	/**
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		activityHelper.onPostCreate(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		activityHelper.onConfigurationChanged(newConfig);
	}
	
}
