package com.jdroid.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.jdroid.android.application.AppModule;
import com.jdroid.android.loading.ActivityLoading;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.uri.UriHandler;

public interface ActivityIf extends ComponentIf {

	// //////////////////////// Layout //////////////////////// //

	@LayoutRes
	public int getContentView();

	// //////////////////////// Life cycle //////////////////////// //
	
	public Boolean onBeforeSetContentView();
	
	public void onAfterSetContentView(Bundle savedInstanceState);

	public void doOnCreateOptionsMenu(Menu menu);
	
	public MenuInflater getMenuInflater();
	
	public Boolean isLauncherActivity();

	public Intent getUpIntent();

	public Boolean isActivityDestroyed();

	public Boolean onBackPressedHandled();

	// //////////////////////// Delegates //////////////////////// //

	public ActivityDelegate createActivityDelegate(AppModule appModule);

	public ActivityDelegate getActivityDelegate(AppModule appModule);

	// //////////////////////// Loading //////////////////////// //

	@NonNull
	public ActivityLoading getDefaultLoading();
	
	public void setLoading(ActivityLoading loading);
	
	// //////////////////////// Navigation Drawer //////////////////////// //

	public void initNavDrawer(Toolbar appBar);

	public Boolean isNavDrawerEnabled();

	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar);

	// //////////////////////// Location //////////////////////// //

	@Nullable
	public Long getLocationFrequency();

	// //////////////////////// Others //////////////////////// //

	@Nullable
	public UriHandler getUriHandler();

	public Boolean isGooglePlayServicesVerificationEnabled();

	/**
	 * @return Whether this {@link Activity} requires authentication or not
	 */
	public Boolean requiresAuthentication();
	
}
