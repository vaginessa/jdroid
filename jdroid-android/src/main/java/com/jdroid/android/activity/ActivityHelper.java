package com.jdroid.android.activity;

import java.util.List;
import org.slf4j.Logger;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.ads.AdSize;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.R;
import com.jdroid.android.ad.AdHelper;
import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.gps.LocalizationManager;
import com.jdroid.android.loading.DefaultLoadingDialogBuilder;
import com.jdroid.android.loading.LoadingDialogBuilder;
import com.jdroid.android.navdrawer.NavDrawerAdapter;
import com.jdroid.android.navdrawer.NavDrawerItem;
import com.jdroid.android.utils.ImageLoaderUtils;
import com.jdroid.android.utils.NotificationBuilder;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class ActivityHelper implements ActivityIf {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(ActivityHelper.class);
	
	private static final int LOCATION_UPDATE_TIMER_CODE = IdGenerator.getIntId();
	private static final String NAV_DRAWER_MANUALLY_USED = "navDrawerManuallyUsed";
	private static final String TITLE_KEY = "title";
	
	private Activity activity;
	protected Dialog loadingDialog;
	private Handler locationHandler;
	private AdHelper adHelper;
	private boolean isDestoyed = false;
	
	private String title;
	
	// Navigation drawer
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private ListView drawerList;
	private static Boolean navDrawerManuallyUsed = false;
	
	/**
	 * @param activity
	 */
	public ActivityHelper(Activity activity) {
		this.activity = activity;
	}
	
	public ActivityIf getActivityIf() {
		return (ActivityIf)activity;
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
	}
	
	public void onCreate(Bundle savedInstanceState) {
		LOGGER.trace("Executing onCreate on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);
		
		AbstractApplication.get().initExceptionHandlers();
		
		// Action bar
		final ActionBar actionBar = activity.getActionBar();
		if (actionBar != null) {
			
			actionBar.setHomeButtonEnabled(true);
			if (!getActivityIf().isLauncherActivity()) {
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
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
			adHelper.loadAd(activity, (ViewGroup)(activity.findViewById(R.id.adViewContainer)),
				getActivityIf().getAdSize(), getRemoveAdsClickListener());
		}
		
		// Nav Drawer
		
		if (isNavDrawerEnabled()) {
			
			if (getActivityIf().isNavDrawerTopLevelView()) {
				actionBar.setDisplayHomeAsUpEnabled(true);
				actionBar.setHomeButtonEnabled(true);
			}
			
			drawerLayout = findView(R.id.drawer_layout);
			drawerList = findView(R.id.left_drawer);
			
			// Set the adapter for the list view
			// set a custom shadow that overlays the main content when the drawer opens
			drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
			
			User user = SecurityContext.get().getUser();
			if (user != null) {
				View navDrawerHeader = inflate(R.layout.nav_drawer_header);
				ImageLoaderUtils.displayImage(user.getCoverPictureUrl(),
					((ImageView)navDrawerHeader.findViewById(R.id.cover)), null);
				ImageLoaderUtils.displayImage(user.getProfilePictureUrl(),
					((ImageView)navDrawerHeader.findViewById(R.id.photo)), R.drawable.profile_default, null,
					User.PROFILE_PICTURE_TTL);
				
				String fullname = user.getFullname();
				String email = user.getEmail();
				if (AbstractApplication.get().getAppContext().isUserDataMocked()) {
					fullname = getMockedFullname();
					email = getMockedEmail();
				}
				((TextView)navDrawerHeader.findViewById(R.id.fullName)).setText(fullname);
				((TextView)navDrawerHeader.findViewById(R.id.email)).setText(email);
				drawerList.addHeaderView(navDrawerHeader);
			}
			drawerList.setAdapter(new NavDrawerAdapter(activity, getVisibleNavDrawerItems()));
			// Set the list's click listener
			drawerList.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (position < drawerList.getHeaderViewsCount()) {
						onNavDrawerHeaderClick();
						drawerLayout.closeDrawer(drawerList);
					} else {
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
				public void onDrawerSlide(View arg0, float arg1) {
					// Do nothing
				}
				
				@Override
				public void onDrawerOpened(View view) {
					if (actionBar != null) {
						title = actionBar.getTitle() != null ? actionBar.getTitle().toString() : null;
						actionBar.setTitle(R.string.appName);
					}
					activity.invalidateOptionsMenu();
				}
				
				@Override
				public void onDrawerClosed(View view) {
					if (actionBar != null) {
						actionBar.setTitle(title);
					}
					activity.invalidateOptionsMenu();
				}
			};
			
			if (getActivityIf().isNavDrawerTopLevelView()) {
				drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, R.drawable.ic_drawer,
						R.string.drawerOpen, R.string.drawerClose) {
					
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
		
		if (savedInstanceState == null) {
			AppLoadingSource appLoadingSource = AppLoadingSource.getAppLoadingSource(activity.getIntent());
			if (AppLoadingSource.NOTIFICATION.equals(appLoadingSource)) {
				String notificationName = activity.getIntent().getStringExtra(NotificationBuilder.NOTIFICATION_NAME);
				if (notificationName != null) {
					AbstractApplication.get().getAnalyticsSender().trackNotificationOpened(notificationName);
				}
			}
		}
	}
	
	private String getMockedEmail() {
		return "tonystark@ironmail.com";
	}
	
	private String getMockedFullname() {
		return "Tony Stark";
	}
	
	private void saveNavDrawerManuallyUsed() {
		if ((navDrawerManuallyUsed == null) || !navDrawerManuallyUsed) {
			ExecutorUtils.execute(new Runnable() {
				
				@Override
				public void run() {
					SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get());
					Editor editor = sharedPreferences.edit();
					editor.putBoolean(NAV_DRAWER_MANUALLY_USED, true);
					editor.commit();
					navDrawerManuallyUsed = true;
				}
			});
		}
	}
	
	protected void onNavDrawerHeaderClick() {
		// Do nothing
	}
	
	private void selectNavDrawerItem(int position) {
		NavDrawerItem navDrawerItem = (NavDrawerItem)drawerList.getAdapter().getItem(position);
		if (!navDrawerItem.matchesActivity((FragmentActivity)activity)) {
			navDrawerItem.startActivity((FragmentActivity)activity);
		}
		
		// update selected item and title, then close the drawer
		drawerList.setItemChecked(position, true);
		drawerLayout.closeDrawer(drawerList);
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
	
	public void onContentChanged() {
	}
	
	public void onSaveInstanceState(Bundle outState) {
		LOGGER.trace("Executing onSaveInstanceState on " + activity);
		dismissBlockingLoading();
		outState.putString(TITLE_KEY, title);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		LOGGER.trace("Executing onRestoreInstanceState on " + activity);
		title = savedInstanceState.getString(TITLE_KEY);
	}
	
	@SuppressLint("HandlerLeak")
	public void onStart() {
		LOGGER.trace("Executing onStart on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);
		
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				AbstractApplication.get().saveInstallationSource();
				if (AbstractApplication.get().hasAnalyticsSender()) {
					AppLoadingSource appLoadingSource = AppLoadingSource.getAppLoadingSource(activity.getIntent());
					AbstractApplication.get().getAnalyticsSender().onActivityStart(activity, appLoadingSource,
						getOnActivityStartData());
				}
			}
		});
		
		final Long locationFrequency = getLocationFrequency();
		if (locationFrequency != null) {
			locationHandler = new Handler() {
				
				@Override
				public void handleMessage(Message m) {
					LocalizationManager.get().startLocalization();
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
		return getActivityIf().getLocationFrequency();
	}
	
	public void onResume() {
		LOGGER.trace("Executing onResume on " + activity);
		AbstractApplication.get().setInBackground(false);
		AbstractApplication.get().setCurrentActivity(activity);
		
		ActionBar actionBar = activity.getActionBar();
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
		
		if (isNavDrawerEnabled()) {
			for (int i = 0; i < getVisibleNavDrawerItems().size(); i++) {
				NavDrawerItem item = getVisibleNavDrawerItems().get(i);
				if (item.isMainAction() && item.matchesActivity((FragmentActivity)activity)) {
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
		LOGGER.trace("Executing onPause on " + activity);
		AbstractApplication.get().setInBackground(true);
	}
	
	public void onStop() {
		LOGGER.trace("Executing onStop on " + activity);
		ToastUtils.cancelCurrentToast();
		if (AbstractApplication.get().hasAnalyticsSender()) {
			AbstractApplication.get().getAnalyticsSender().onActivityStop(activity);
		}
		
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
		isDestoyed = true;
		LOGGER.trace("Executing onDestroy on " + activity);
		dismissBlockingLoading();
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
			Intent upIntent = NavUtils.getParentActivityIntent(activity);
			if (NavUtils.shouldUpRecreateTask(activity, upIntent)) {
				// This activity is NOT part of this app's task, so create a new task
				// when navigating up, with a synthesized back stack.
				TaskStackBuilder.create(activity)
				// Add all of this activity's parents to the back stack
				.addNextIntentWithParentStack(upIntent)
				// Navigate up to the closest parent
				.startActivities();
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
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#showBlockingLoading()
	 */
	@Override
	public void showBlockingLoading() {
		showBlockingLoading(new DefaultLoadingDialogBuilder());
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#showBlockingLoading(com.jdroid.android.loading.LoadingDialogBuilder)
	 */
	@Override
	public void showBlockingLoading(final LoadingDialogBuilder builder) {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (!isDestoyed && ((loadingDialog == null) || (!loadingDialog.isShowing()))) {
					loadingDialog = builder.build(activity);
					loadingDialog.show();
				}
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#dismissBlockingLoading()
	 */
	@Override
	public void dismissBlockingLoading() {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (loadingDialog != null) {
					loadingDialog.dismiss();
					loadingDialog = null;
				}
			}
		});
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
	
	public OnClickListener getRemoveAdsClickListener() {
		return null;
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
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContextualMenuItemsIds()
	 */
	@Override
	public List<Integer> getContextualMenuItemsIds() {
		return Lists.newArrayList();
	}
}
