package com.jdroid.android.navdrawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.java.concurrent.ExecutorUtils;

public abstract class NavDrawer {

	public static final String NAV_DRAWER_MANUALLY_USED = "navDrawerManuallyUsed";

	private static Boolean navDrawerManuallyUsed = false;

	private AbstractFragmentActivity activity;
	private Toolbar appBar;
	private Boolean isNavDrawerTopLevelView = false;
	private Boolean darkTheme;
	private Boolean isNavDrawerOpenedOnFirstSession = true;

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private View content;

	public NavDrawer(final AbstractFragmentActivity activity, Boolean darkTheme, Toolbar appBar) {
		this.activity = activity;
		this.darkTheme = darkTheme;
		this.appBar = appBar;
		drawerLayout = activity.findView(R.id.drawer_layout);
	}

	public void init() {

		// Set the adapter for the list view
		// set a custom shadow that overlays the main content when the drawer opens
		drawerLayout.setDrawerShadow(darkTheme ? R.drawable.drawer_shadow_dark : R.drawable.drawer_shadow,
				GravityCompat.START);

		final DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {

			@Override
			public void onDrawerStateChanged(int newState) {
				if (newState == DrawerLayout.STATE_DRAGGING) {
					saveNavDrawerManuallyUsed();
				}
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// Do nothing
			}

			@Override
			public void onDrawerOpened(View view) {
				// Do nothing
			}

			@Override
			public void onDrawerClosed(View view) {
				// Do nothing
			}
		};

		if (isNavDrawerTopLevelView && appBar != null) {
			drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, appBar, R.string.drawerOpen,
					R.string.drawerClose) {

				@Override
				public void onDrawerStateChanged(int newState) {
					super.onDrawerStateChanged(newState);
					drawerListener.onDrawerStateChanged(newState);
				}

				@Override
				public void onDrawerSlide(View drawerView, float slideOffset) {
					// 0 offset to disable the hamburger animation
					super.onDrawerSlide(drawerView, 0);
					drawerListener.onDrawerSlide(drawerView, 0);
				}

				@Override
				public void onDrawerClosed(View view) {
					super.onDrawerClosed(view);
					drawerListener.onDrawerClosed(view);
				}

				@Override
				public void onDrawerOpened(View drawerView) {
					super.onDrawerOpened(drawerView);
					drawerListener.onDrawerOpened(drawerView);
				}
			};
			// Set the drawer toggle as the DrawerListener
			drawerLayout.setDrawerListener(drawerToggle);
		} else {
			if (appBar != null) {
				appBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			}
			drawerLayout.setDrawerListener(drawerListener);
		}

		content = createContent();

		if (isNavDrawerOpenedOnFirstSession) {
			ExecutorUtils.schedule(new Runnable() {

				@Override
				public void run() {
					navDrawerManuallyUsed = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(
							NAV_DRAWER_MANUALLY_USED, false);
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							String action = activity.getIntent().getAction();
							if (!navDrawerManuallyUsed && Intent.ACTION_MAIN.equals(action)) {
								drawerLayout.openDrawer(content);
							}
						}
					});
				}
			}, 1L);
		}
	}

	private void saveNavDrawerManuallyUsed() {
		if ((navDrawerManuallyUsed == null) || !navDrawerManuallyUsed) {
			ExecutorUtils.execute(new Runnable() {

				@Override
				public void run() {
					SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get());
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putBoolean(NAV_DRAWER_MANUALLY_USED, true);
					editor.apply();
					navDrawerManuallyUsed = true;
				}
			});
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns true, then it has handled the app icon touch event
		if ((drawerToggle != null) && drawerToggle.onOptionsItemSelected(item)) {
			saveNavDrawerManuallyUsed();
			return true;
		} else {
			return false;
		}
	}


	public void onPostCreate(Bundle savedInstanceState) {
		if (drawerToggle != null) {
			// Sync the toggle state after onRestoreInstanceState has occurred.
			drawerToggle.syncState();
		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		if (drawerToggle != null) {
			drawerToggle.onConfigurationChanged(newConfig);
		}
	}

	public DrawerLayout getDrawerLayout() {
		return drawerLayout;
	}

	public void onResume() {
		// Do Nothing
	}

	public abstract View createContent();

	protected AbstractFragmentActivity getActivity() {
		return activity;
	}

	public void setIsNavDrawerTopLevelView(Boolean isNavDrawerTopLevelView) {
		this.isNavDrawerTopLevelView = isNavDrawerTopLevelView;
	}

	public void setIsNavDrawerOpenedOnFirstSession(Boolean isNavDrawerOpenedOnFirstSession) {
		this.isNavDrawerOpenedOnFirstSession = isNavDrawerOpenedOnFirstSession;
	}

}
