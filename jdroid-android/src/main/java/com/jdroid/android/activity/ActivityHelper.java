package com.jdroid.android.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.R;
import com.jdroid.android.ad.AdHelper;
import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.google.inappbilling.InAppBillingHelper;
import com.jdroid.android.loading.ActivityLoading;
import com.jdroid.android.loading.DefaultBlockingLoading;
import com.jdroid.android.location.LocationHelper;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.uri.UriHandler;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;

import org.slf4j.Logger;

public class ActivityHelper implements ActivityIf {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(ActivityHelper.class);
	
	private static final int LOCATION_UPDATE_TIMER_CODE = IdGenerator.getIntId();
	private static final String TITLE_KEY = "title";
	
	private AbstractFragmentActivity activity;
	private Handler locationHandler;
	private AdHelper adHelper;
	private boolean isDestroyed = false;
	
	private ActivityLoading loading;
	
	private String title;
	
	private NavDrawer navDrawer;

	public ActivityHelper(AbstractFragmentActivity activity) {
		this.activity = activity;
	}
	
	public ActivityIf getActivityIf() {
		return activity;
	}
	
	protected Activity getActivity() {
		return activity;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAppContext()
	 */
	@Override
	public AppContext getAppContext() {
		return AbstractApplication.get().getAppContext();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return getActivityIf().getContentView();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onBeforeSetContentView()
	 */
	@Override
	public Boolean onBeforeSetContentView() {
		return true;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onAfterSetContentView(android.os.Bundle)
	 */
	@Override
	public void onAfterSetContentView(Bundle savedInstanceState) {
		// Do Nothing
	}
	
	public void beforeOnCreate() {
		// Do nothing
	}
	

	public void onCreate(Bundle savedInstanceState) {
		LOGGER.debug("Executing onCreate on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);

		overrideStatusBarColor();

		AbstractApplication.get().initExceptionHandlers();

		UriHandler uriHandler = getActivityIf().getUriHandler();
		if (uriHandler != null) {
			AbstractApplication.get().getUriMapper().handleUri(activity, savedInstanceState, uriHandler);
		}

		InAppBillingHelper.onCreate(activity, savedInstanceState);

		if (getActivityIf().onBeforeSetContentView()) {
			if (getContentView() != 0) {
				activity.setContentView(getContentView());
				getActivityIf().onAfterSetContentView(savedInstanceState);
			}
		}
		
		// Ads
		adHelper = getActivityIf().createAdHelper();
		if (adHelper != null) {
			adHelper.setAdViewContainer((ViewGroup)(activity.findViewById(R.id.adViewContainer)));
			adHelper.loadBanner(activity);
			adHelper.loadInterstitial(activity);
		}

		if (savedInstanceState == null) {
			trackNotificationOpened(activity.getIntent());
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	protected void overrideStatusBarColor() {
		if (!AndroidUtils.isPreLollipop()) {
			activity.getWindow().setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
		}
	}

	@Override
	public UriHandler getUriHandler() {
		return null;
	}

	public void onPostCreate(Bundle savedInstanceState) {
		if (navDrawer != null) {
			navDrawer.onPostCreate(savedInstanceState);
		}
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		if (navDrawer != null) {
			navDrawer.onConfigurationChanged(newConfig);
		}
	}

	@Override
	@Nullable
	public AdHelper createAdHelper() {
		return null;
	}

	@Nullable
	@Override
	public AdHelper getAdHelper() {
		return adHelper;
	}

	/**
	 * @see com.jdroid.android.activity.ActivityIf#isLauncherActivity()
	 */
	@Override
	public Boolean isLauncherActivity() {
		return false;
	}
	
	public void onSaveInstanceState(Bundle outState) {
		LOGGER.debug("Executing onSaveInstanceState on " + activity);
		dismissLoading();
		outState.putString(TITLE_KEY, title);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		LOGGER.debug("Executing onRestoreInstanceState on " + activity);
		title = savedInstanceState.getString(TITLE_KEY);
	}
	
	@SuppressLint("HandlerLeak")
	public void onStart() {
		LOGGER.debug("Executing onStart on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);
		
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				AbstractApplication.get().saveInstallationSource();
				AppLoadingSource appLoadingSource = AppLoadingSource.getAppLoadingSource(activity.getIntent());
				AbstractApplication.get().getAnalyticsSender().onActivityStart(activity.getClass(), appLoadingSource,
						getOnActivityStartData());
			}
		});
		
		final Long locationFrequency = getActivityIf().getLocationFrequency();
		if (locationFrequency != null) {
			locationHandler = new Handler() {
				
				@Override
				@RequiresPermission(anyOf = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
				public void handleMessage(Message m) {
					LocationHelper.get().startLocalization();
					locationHandler.sendMessageDelayed(Message.obtain(locationHandler, LOCATION_UPDATE_TIMER_CODE),
						locationFrequency);
				}
			};
			locationHandler.sendMessage(Message.obtain(locationHandler, LOCATION_UPDATE_TIMER_CODE));
		}
	}

	@Nullable
	protected Object getOnActivityStartData() {
		return null;
	}
	
	@Override
	public Long getLocationFrequency() {
		return null;
	}
	
	public void onResume() {
		
		LOGGER.debug("Executing onResume on " + activity);
		AbstractApplication.get().setInBackground(false);
		AbstractApplication.get().setCurrentActivity(activity);

		if (adHelper != null) {
			adHelper.onResume();
		}

		if (navDrawer != null) {
			navDrawer.onResume();
		}
	}
	
	public void onBeforePause() {
		if (adHelper != null) {
			adHelper.onPause();
		}
	}
	
	public void onPause() {
		LOGGER.debug("Executing onPause on " + activity);
		AbstractApplication.get().setInBackground(true);
	}
	
	public void onStop() {
		LOGGER.debug("Executing onStop on " + activity);
		ToastUtils.cancelCurrentToast();
		AbstractApplication.get().getAnalyticsSender().onActivityStop(activity);
		
		if (locationHandler != null) {
			locationHandler.removeCallbacksAndMessages(null);
		}
	}
	
	public void onBeforeDestroy() {
		if (adHelper != null) {
			adHelper.onDestroy();
		}
	}
	
	public void onDestroy() {
		isDestroyed = true;
		LOGGER.debug("Executing onDestroy on " + activity);
		dismissLoading();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		AbstractApplication.get().setCurrentActivity(activity);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		if (getActivityIf().getMenuResourceId() != null) {
			MenuInflater inflater = getActivityIf().getMenuInflater();
			inflater.inflate(getActivityIf().getMenuResourceId(), menu);
			getActivityIf().doOnCreateOptionsMenu(menu);
		}
		return true;
	}
	
	@Override
	public Integer getMenuResourceId() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#doOnCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public void doOnCreateOptionsMenu(Menu menu) {
		// Do nothing
	}
	
	public void onPrepareOptionsMenu(Menu menu) {
		// Do nothing
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (navDrawer != null && navDrawer.onOptionsItemSelected(item)) {
			return true;
		} else {
			return onOptionsItemSelected(item.getItemId());
		}
	}
	
	private boolean onOptionsItemSelected(int itemId) {
		if (itemId == android.R.id.home) {
			
			Intent upIntent = getActivityIf().getUpIntent();
			if (upIntent == null) {
				upIntent = NavUtils.getParentActivityIntent(activity);
			}
			if (upIntent != null && NavUtils.shouldUpRecreateTask(activity, upIntent)) {
				// This activity is NOT part of this app's task, so create a new task
				// when navigating up, with a synthesized back stack.
				TaskStackBuilder builder = TaskStackBuilder.create(activity);
				// Add all of this activity's parents to the back stack
				builder.addNextIntentWithParentStack(upIntent);
				// Navigate up to the closest parent
				builder.startActivities();
			} else {
				// This activity is part of this app's task, so simply
				// navigate up to the logical parent activity.
				// NavUtils.navigateUpTo(activity, upIntent);
				if (upIntent != null) {
					upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					activity.startActivity(upIntent);
					activity.finish();
				} else {
					ActivityLauncher.launchHomeActivity();
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public Intent getUpIntent() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return ReflectionUtils.newInstance(clazz);
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#getExtra(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <E> E getExtra(String key) {
		Bundle extras = activity.getIntent().getExtras();
		return extras != null ? (E)extras.get(key) : null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <V extends View> V findView(int id) {
		return (V)activity.findViewById(id);
	}
	
	@Override
	public View inflate(int resource) {
		return LayoutInflater.from(activity).inflate(resource, null);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#requiresAuthentication()
	 */
	@Override
	public Boolean requiresAuthentication() {
		return true;
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#getUser()
	 */
	@Override
	public User getUser() {
		return SecurityContext.get().getUser();
	}
	
	public Boolean isAuthenticated() {
		return SecurityContext.get().isAuthenticated();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getMenuInflater()
	 */
	@Override
	public MenuInflater getMenuInflater() {
		return null;
	}
	
	public void onNewIntent(Intent intent) {
		LOGGER.debug("Executing onNewIntent on " + activity);
		
		trackNotificationOpened(intent);
	}
	
	private void trackNotificationOpened(Intent intent) {
		try {
			AppLoadingSource appLoadingSource = AppLoadingSource.getAppLoadingSource(intent);
			if (appLoadingSource != null) {
				if (AppLoadingSource.NOTIFICATION.equals(appLoadingSource)) {
					String notificationName = intent.getStringExtra(NotificationBuilder.NOTIFICATION_NAME);
					if (notificationName != null) {
						AbstractApplication.get().getAnalyticsSender().trackNotificationOpened(notificationName);
					}
				}
			}
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		if (activity.equals(AbstractApplication.get().getCurrentActivity())) {
			activity.runOnUiThread(runnable);
		}
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isActivityDestroyed()
	 */
	@Override
	public Boolean isActivityDestroyed() {
		return isDestroyed;
	}
	
	// //////////////////////// Loading //////////////////////// //
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				if (loading == null) {
					loading = getActivityIf().getDefaultLoading();
				}
				loading.show(getActivityIf());
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				if (loading != null) {
					loading.dismiss(getActivityIf());
				}
			}
		});
	}
	
	@NonNull
	@Override
	public ActivityLoading getDefaultLoading() {
		return new DefaultBlockingLoading();
	}
	
	@Override
	public void setLoading(ActivityLoading loading) {
		this.loading = loading;
	}

	@Override
	public Boolean onBackPressedHandled() {
		if (navDrawer != null) {
			return navDrawer.onBackPressed();
		}
		return false;
	}
	
	// //////////////////////// Navigation Drawer //////////////////////// //

	public void initNavDrawer(Toolbar appBar) {
		if (getActivityIf().isNavDrawerEnabled()) {
			navDrawer = getActivityIf().createNavDrawer(activity, appBar);
			navDrawer.init();
		}
	}

	@Override
	public Boolean isNavDrawerEnabled() {
		return false;
	}

	@Override
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		return null;
	}
}
