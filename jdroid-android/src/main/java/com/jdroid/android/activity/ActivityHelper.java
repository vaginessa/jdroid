package com.jdroid.android.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.R;
import com.jdroid.android.ad.AdHelper;
import com.jdroid.android.ad.HouseAdBuilder;
import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.inappbilling.InAppBillingHelperFragment;
import com.jdroid.android.loading.ActivityLoading;
import com.jdroid.android.loading.DefaultBlockingLoading;
import com.jdroid.android.location.LocationHelper;
import com.jdroid.android.navdrawer.NavDrawerAdapter;
import com.jdroid.android.navdrawer.NavDrawerHeaderBuilder;
import com.jdroid.android.navdrawer.NavDrawerItem;
import com.jdroid.android.utils.NotificationBuilder;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;

import org.slf4j.Logger;

import java.util.List;

public class ActivityHelper implements ActivityIf {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(ActivityHelper.class);
	
	private static final int LOCATION_UPDATE_TIMER_CODE = IdGenerator.getIntId();
	public static final String NAV_DRAWER_MANUALLY_USED = "navDrawerManuallyUsed";
	private static final String TITLE_KEY = "title";
	
	private AbstractFragmentActivity activity;
	private Handler locationHandler;
	private AdHelper adHelper;
	private boolean isDestroyed = false;
	
	private ActivityLoading loading;
	
	private String title;
	
	// Navigation drawer
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private ListView drawerList;
	private static Boolean navDrawerManuallyUsed = false;
	private static Boolean inAppBillingLoaded = false;
	
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
		
		AbstractApplication.get().changeLocale();
		
		AbstractApplication.get().initExceptionHandlers();
		
		if ((savedInstanceState == null) && !inAppBillingLoaded) {
			InAppBillingHelperFragment.add(activity, InAppBillingHelperFragment.class, true, null);
			inAppBillingLoaded = true;
		}
		
		// Action bar
		final ActionBar actionBar = activity.getSupportActionBar();
		if (actionBar != null) {
			
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		if (getActivityIf().onBeforeSetContentView()) {
			if (getContentView() != 0) {
				activity.setContentView(getContentView());
				getActivityIf().onAfterSetContentView(savedInstanceState);
			}
		}
		
		// Ads
		adHelper = createAdHelper();
		if (adHelper != null) {
			adHelper.loadBanner(activity, (ViewGroup)(activity.findViewById(R.id.adViewContainer)),
					getActivityIf().getAdSize(), getActivityIf().getBannerAdUnitId(), getHouseAdBuilder());
			if (getActivityIf().isInterstitialEnabled()) {
				adHelper.loadInterstitial(activity, getActivityIf().getInterstitialAdUnitId());
			}
		}
		
		// Nav Drawer
		
		if (isNavDrawerEnabled()) {
			
			drawerLayout = findView(R.id.drawer_layout);
			drawerList = findView(R.id.left_drawer);
			
			// Set the adapter for the list view
			// set a custom shadow that overlays the main content when the drawer opens
			drawerLayout.setDrawerShadow(isDarkTheme() ? R.drawable.drawer_shadow_dark : R.drawable.drawer_shadow,
				GravityCompat.START);

			if (isNavDrawerUserHeaderVisible()) {
				drawerList.addHeaderView(createNavDrawerHeaderBuilder().build());
			}
			drawerList.setAdapter(new NavDrawerAdapter(activity, getVisibleNavDrawerItems()));
			// Set the list's click listener
			drawerList.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (position >= drawerList.getHeaderViewsCount()) {
						selectNavDrawerItem(position);
					}
				}
				
			});
			
			final DrawerListener drawerListener = new DrawerListener() {
				
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
			
			if (getActivityIf().isNavDrawerTopLevelView()) {
				drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, R.string.drawerOpen,
						R.string.drawerClose) {
					
					@Override
					public void onDrawerStateChanged(int newState) {
						super.onDrawerStateChanged(newState);
						drawerListener.onDrawerStateChanged(newState);
					}
					
					@Override
					public void onDrawerSlide(View drawerView, float slideOffset) {
						super.onDrawerSlide(drawerView, slideOffset);
						drawerListener.onDrawerSlide(drawerView, slideOffset);
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
				drawerLayout.setDrawerListener(drawerListener);
			}
			
			if (isNavDrawerOpenedOnFirstSession()) {
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
									drawerLayout.openDrawer(drawerList);
								}
							}
						});
					}
				}, 1L);
			}
			
		}
		
		if (savedInstanceState == null) {
			trackNotificationOpened(activity.getIntent());
		}
	}

	public Boolean isDarkTheme() {
		return false;
	}
	
	private String getMockedEmail() {
		return "tonystark@ironmail.com";
	}
	
	private String getMockedFullname() {
		return "Tony Stark";
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
	
	protected AdHelper createAdHelper() {
		return new AdHelper();
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
				public void handleMessage(Message m) {
					LocationHelper.get().startLocalization();
					locationHandler.sendMessageDelayed(Message.obtain(locationHandler, LOCATION_UPDATE_TIMER_CODE),
						locationFrequency);
				}
			};
			locationHandler.sendMessage(Message.obtain(locationHandler, LOCATION_UPDATE_TIMER_CODE));
		}
	}
	
	protected Object getOnActivityStartData() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getLocationFrequency()
	 */
	@Override
	public Long getLocationFrequency() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isInterstitialEnabled()
	 */
	@Override
	public Boolean isInterstitialEnabled() {
		return false;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#displayInterstitial(java.lang.Boolean)
	 */
	@Override
	public void displayInterstitial(Boolean retryIfNotLoaded) {
		adHelper.displayInterstitial(retryIfNotLoaded);
	}
	
	public void onResume() {
		
		LOGGER.debug("Executing onResume on " + activity);
		AbstractApplication.get().setInBackground(false);
		AbstractApplication.get().setCurrentActivity(activity);
		
		ActionBar actionBar = activity.getSupportActionBar();
		if (actionBar != null) {
			AppContext context = AbstractApplication.get().getAppContext();
			if (!context.isProductionEnvironment() && context.displayDebugSettings()) {
				if (context.isHttpMockEnabled()) {
					actionBar.setBackgroundDrawable(getActivity().getResources().getDrawable(
						R.color.actionbarMockBackground));
				}
			}
		}
		
		if (adHelper != null) {
			adHelper.onResume();
		}
		
		checkNavDrawerItem();
	}
	
	private void checkNavDrawerItem() {
		if (isNavDrawerEnabled()) {
			for (int i = 0; i < getVisibleNavDrawerItems().size(); i++) {
				NavDrawerItem item = getVisibleNavDrawerItems().get(i);
				if (item.isMainAction() && item.matchesActivity(activity)) {
					drawerList.setItemChecked(i + drawerList.getHeaderViewsCount(), true);
				}
			}
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
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getMenuResourceId()
	 */
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
		if (isNavDrawerEnabled() && (drawerLayout != null)) {
			// If the nav drawer is open, hide action items related to the content view
			boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
			for (Integer itemId : getActivityIf().getContextualMenuItemsIds()) {
				menu.findItem(itemId).setVisible(!drawerOpen);
			}
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns true, then it has handled the app icon touch event
		if (isNavDrawerEnabled() && (drawerToggle != null) && drawerToggle.onOptionsItemSelected(item)) {
			saveNavDrawerManuallyUsed();
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
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#findView(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <V extends View> V findView(int id) {
		return (V)activity.findViewById(id);
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#inflate(int)
	 */
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
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return AdSize.SMART_BANNER;
	}

	@Override
	public String getBannerAdUnitId() {
		return AbstractApplication.get().getAppContext().getDefaultAdUnitId();
	}

	@Override
	public String getInterstitialAdUnitId() {
		return AbstractApplication.get().getAppContext().getDefaultAdUnitId();
	}

	public HouseAdBuilder getHouseAdBuilder() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContextualMenuItemsIds()
	 */
	@Override
	public List<Integer> getContextualMenuItemsIds() {
		return Lists.newArrayList();
	}
	
	public void onNewIntent(Intent intent) {
		LOGGER.debug("Executing onNewIntent on " + activity);
		
		trackNotificationOpened(intent);
	}
	
	private void trackNotificationOpened(Intent intent) {
		AppLoadingSource appLoadingSource = AppLoadingSource.getAppLoadingSource(intent);
		if (appLoadingSource != null) {
			if (AppLoadingSource.NOTIFICATION.equals(appLoadingSource)) {
				String notificationName = intent.getStringExtra(NotificationBuilder.NOTIFICATION_NAME);
				if (notificationName != null) {
					AbstractApplication.get().getAnalyticsSender().trackNotificationOpened(notificationName);
				}
			}
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
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getDefaultLoading()
	 */
	@Override
	public ActivityLoading getDefaultLoading() {
		return new DefaultBlockingLoading();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#setLoading(com.jdroid.android.loading.ActivityLoading)
	 */
	@Override
	public void setLoading(ActivityLoading loading) {
		this.loading = loading;
	}

	@Override
	public void onBackPressed() {
		// Do nothing
	}
	
	// //////////////////////// Navigation Drawer //////////////////////// //
	
	private void saveNavDrawerManuallyUsed() {
		if ((navDrawerManuallyUsed == null) || !navDrawerManuallyUsed) {
			ExecutorUtils.execute(new Runnable() {
				
				@Override
				public void run() {
					SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get());
					Editor editor = sharedPreferences.edit();
					editor.putBoolean(NAV_DRAWER_MANUALLY_USED, true);
					editor.apply();
					navDrawerManuallyUsed = true;
				}
			});
		}
	}
	
	private void selectNavDrawerItem(int position) {
		NavDrawerItem navDrawerItem = (NavDrawerItem)drawerList.getAdapter().getItem(position);
		if (!navDrawerItem.matchesActivity(activity)) {
			navDrawerItem.startActivity(activity);
		}
		
		// update selected item and title, then close the drawer
		if (navDrawerItem.isMainAction()) {
			drawerList.setItemChecked(position, true);
		} else {
			checkNavDrawerItem();
		}
		drawerLayout.closeDrawer(drawerList);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isNavDrawerEnabled()
	 */
	@Override
	public Boolean isNavDrawerEnabled() {
		return false;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isNavDrawerTopLevelView()
	 */
	@Override
	public Boolean isNavDrawerTopLevelView() {
		return false;
	}
	
	public List<NavDrawerItem> getNavDrawerItems() {
		return null;
	}
	
	private List<NavDrawerItem> getVisibleNavDrawerItems() {
		List<NavDrawerItem> visibleNavDrawerItems = Lists.newArrayList();
		for (NavDrawerItem each : getNavDrawerItems()) {
			if (each.isVisible()) {
				visibleNavDrawerItems.add(each);
			}
		}
		return visibleNavDrawerItems;
	}
	
	public Boolean isNavDrawerOpenedOnFirstSession() {
		return true;
	}
	
	protected Boolean isNavDrawerUserHeaderVisible() {
		return true;
	}

	@Override
	public NavDrawerHeaderBuilder createNavDrawerHeaderBuilder() {
		NavDrawerHeaderBuilder builder = new NavDrawerHeaderBuilder(getActivityIf());
		User user = SecurityContext.get().getUser();
		if (user != null) {
			builder.setBackground(user.getCoverPictureUrl(), User.PROFILE_PICTURE_TTL);
			builder.setMainImage(user.getProfilePictureUrl(), User.PROFILE_PICTURE_TTL);

			String fullname = user.getFullname();
			String email = user.getEmail();
			if (AbstractApplication.get().getAppContext().isUserDataMocked()) {
				fullname = getMockedFullname();
				email = getMockedEmail();
			}

			builder.setTitle(fullname);
			builder.setSubTitle(email);
		} else {
			builder.setMainImage(R.drawable.ic_launcher);
			builder.setTitle(getActivity().getString(R.string.appName));
			String website = AbstractApplication.get().getAppContext().getWebsite();
			if (website != null) {
				builder.setSubTitle(website.replaceAll("http://", ""));
			}
		}
		return builder;
	}
}
