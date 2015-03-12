package com.jdroid.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.fragment.UseCaseFragment;
import com.jdroid.android.loading.ActivityLoading;
import com.jdroid.java.exception.UnexpectedException;

import java.util.List;

/**
 * Base {@link Activity}
 * 
 */
public abstract class AbstractFragmentActivity extends ActionBarActivity implements ActivityIf {
	
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
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		activityHelper.onStart();
	}
	
	/**
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		activityHelper.onResume();
	}
	
	/**
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		activityHelper.onBeforePause();
		super.onPause();
		activityHelper.onPause();
	}
	
	/**
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		activityHelper.onStop();
	}
	
	/**
	 * @see android.app.Activity#onDestroy()
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
			} catch (InstantiationException | IllegalAccessException e) {
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
		E fragment;
		try {
			fragment = fragmentClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
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
		return activityHelper.getLocationFrequency();
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
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getUpIntent()
	 */
	@Override
	public Intent getUpIntent() {
		return activityHelper.getUpIntent();
	}
	
	/**
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		activityHelper.onNewIntent(intent);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isInterstitialEnabled()
	 */
	@Override
	public Boolean isInterstitialEnabled() {
		return activityHelper.isInterstitialEnabled();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#displayInterstitial(java.lang.Boolean)
	 */
	@Override
	public void displayInterstitial(Boolean retryIfNotLoaded) {
		activityHelper.displayInterstitial(retryIfNotLoaded);
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		activityHelper.executeOnUIThread(runnable);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isActivityDestroyed()
	 */
	@Override
	public Boolean isActivityDestroyed() {
		return activityHelper.isActivityDestroyed();
	}
	
	// //////////////////////// Loading //////////////////////// //
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		activityHelper.showLoading();
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		activityHelper.dismissLoading();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getDefaultLoading()
	 */
	@Override
	public ActivityLoading getDefaultLoading() {
		return activityHelper.getDefaultLoading();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#setLoading(com.jdroid.android.loading.ActivityLoading)
	 */
	@Override
	public void setLoading(ActivityLoading loading) {
		activityHelper.setLoading(loading);
	}
	
}
