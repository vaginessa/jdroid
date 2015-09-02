package com.jdroid.android.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.facebook.device.yearclass.YearClass;
import com.jdroid.android.R;
import com.jdroid.android.about.AboutContext;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.context.AndroidGitContext;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.exception.ExceptionHandler;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.google.inappbilling.InAppBillingContext;
import com.jdroid.android.http.cache.CacheManager;
import com.jdroid.android.images.loader.ImageLoaderHelper;
import com.jdroid.android.images.loader.uil.UilImageLoaderHelper;
import com.jdroid.android.repository.UserRepository;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.android.sqlite.SQLiteUpgradeStep;
import com.jdroid.android.uri.UriMapper;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.context.GitContext;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;
import com.jdroid.java.utils.StringUtils;

import org.slf4j.Logger;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractApplication extends Application {
	
	/**
	 * The LOGGER variable is initialized in the "OnCreate" method, after that "LoggerUtils" has been properly
	 * configured by the superclass.
	 */
	protected static Logger LOGGER;
	
	private static final String INSTALLATION_ID = "installationId";
	public static final String INSTALLATION_SOURCE = "installationSource";
	private static final String VERSION_CODE_KEY = "versionCodeKey";
	
	private static final String DEVICE_YEAR_CLASS = "DeviceYearClass";

	protected static AbstractApplication INSTANCE;
	
	private AppContext appContext;
	private GitContext gitContext;
	private DebugContext debugContext;
	private AboutContext aboutContext;
	private InAppBillingContext inAppBillingContext;

	private AnalyticsSender<? extends AnalyticsTracker> analyticsSender;
	private UriMapper uriMapper;
	
	/** Current activity in the top stack. */
	private Activity currentActivity;
	
	private String installationId;
	private Boolean inBackground = true;
	
	private AppLaunchStatus appLaunchStatus;
	
	private Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories;

	private Integer deviceYearClass = YearClass.CLASS_UNKNOWN;

	private List<AppModule> appModules = Lists.newArrayList();

	private ImageLoaderHelper imageLoaderHelper;

	private UpdateManager updateManager;
	private CacheManager cacheManager;

	public AbstractApplication() {
		INSTANCE = this;
	}
	
	public static AbstractApplication get() {
		return INSTANCE;
	}
	
	/**
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		appContext = createAppContext();

		LoggerUtils.setEnabled(appContext.isLoggingEnabled());
		LOGGER = LoggerUtils.getLogger(AbstractApplication.class);
		LOGGER.debug("Executing onCreate on " + this);

		analyticsSender = createAnalyticsSender();

		uriMapper = createUriMapper();

		updateManager = createUpdateManager();

		initExceptionHandlers();
		LoggerUtils.setExceptionLogger(getExceptionHandler());
		initStrictMode();
		
		// This is required to initialize the statics fields of the utils classes.
		ToastUtils.init();
		DateUtils.init();

		imageLoaderHelper = createImageLoaderHelper();
		ExecutorUtils.execute(new Runnable() {

			@Override
			public void run() {
				loadInstallationId();
				verifyAppLaunchStatus();
				initDeviceYearClass();

				if (getCacheManager() != null) {
					getCacheManager().initFileSystemCache();
				}

				if (imageLoaderHelper != null) {
					imageLoaderHelper.init();
				}

				appContext.saveFirstSessionTimestamp();
			}
		});

		initRepositories();

		initAppModule(appModules);
		for (AppModule each: appModules) {
			each.onCreate();
		}
	}

	@Nullable
	public ImageLoaderHelper getImageLoaderHelper() {
		return imageLoaderHelper;
	}

	@Nullable
	protected ImageLoaderHelper createImageLoaderHelper() {
		return new UilImageLoaderHelper();
	}

	protected void initAppModule(List<AppModule> appModules) {
		// Do nothing
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		for (AppModule each: appModules) {
			each.onConfigurationChanged(newConfig);
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

		for (AppModule each: appModules) {
			each.onLowMemory();
		}
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);

		for (AppModule each: appModules) {
			each.onTrimMemory(level);
		}
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);

		for (AppModule each: appModules) {
			each.attachBaseContext(base);
		}
	}
	
	protected void verifyAppLaunchStatus() {
		Integer fromVersionCode = SharedPreferencesHelper.get().loadPreferenceAsInteger(VERSION_CODE_KEY);
		if (fromVersionCode == null) {
			appLaunchStatus = AppLaunchStatus.NEW_INSTALLATION;
		} else {
			if (AndroidUtils.getVersionCode().equals(fromVersionCode)) {
				appLaunchStatus = AppLaunchStatus.NORMAL;
			} else {
				appLaunchStatus = AppLaunchStatus.VERSION_UPGRADE;
			}
		}
		LOGGER.debug("App launch status: " + appLaunchStatus);
		SharedPreferencesHelper.get().savePreference(VERSION_CODE_KEY, AndroidUtils.getVersionCode());

		if (appLaunchStatus.equals(AppLaunchStatus.VERSION_UPGRADE) && updateManager != null) {
			updateManager.update(fromVersionCode);
		}
	}

	@NonNull
	protected AnalyticsSender<? extends AnalyticsTracker> createAnalyticsSender() {
		return new AnalyticsSender<>();
	}
	
	@SuppressWarnings("unchecked")
	@NonNull
	public <T extends AnalyticsTracker> AnalyticsSender<T> getAnalyticsSender() {
		return (AnalyticsSender<T>)analyticsSender;
	}
	
	public Boolean isStrictModeEnabled() {
		return true;
	}
	
	private void initStrictMode() {
		if (!appContext.isProductionEnvironment() && isStrictModeEnabled()) {
			StrictMode.enableDefaults();
		}
	}
	
	protected void initDeviceYearClass() {
		deviceYearClass = SharedPreferencesHelper.get().loadPreferenceAsInteger(DEVICE_YEAR_CLASS, YearClass.CLASS_UNKNOWN);
		//Try again if device was previously unknown.
		if (deviceYearClass == YearClass.CLASS_UNKNOWN) {
			deviceYearClass = YearClass.get(getApplicationContext());
			SharedPreferencesHelper.get().savePreference(DEVICE_YEAR_CLASS, deviceYearClass);
		}
	}

	public Integer getDeviceYearClass() {
		return deviceYearClass;
	}

	public void initExceptionHandlers() {
		UncaughtExceptionHandler currentExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		if ((currentExceptionHandler == null) || !currentExceptionHandler.getClass().equals(getExceptionHandlerClass())) {
			
			getAnalyticsSender().onInitExceptionHandler(getExceptionHandlerMetadata());
			
			ExceptionHandler exceptionHandler = ReflectionUtils.newInstance(getExceptionHandlerClass());
			exceptionHandler.setDefaultExceptionHandler(currentExceptionHandler);
			Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
			LOGGER.info("Custom exception handler initialized");
		}
	}
	
	protected Map<String, String> getExceptionHandlerMetadata() {
		return null;
	}
	
	public ExceptionHandler getExceptionHandler() {
		return (ExceptionHandler)Thread.getDefaultUncaughtExceptionHandler();
	}
	
	public Class<? extends ExceptionHandler> getExceptionHandlerClass() {
		return DefaultExceptionHandler.class;
	}
	
	public void saveInstallationSource() {
		String installationSource = SharedPreferencesHelper.get().loadPreference(INSTALLATION_SOURCE);
		if (StringUtils.isBlank(installationSource)) {
			installationSource = appContext.getInstallationSource();
			SharedPreferencesHelper.get().savePreference(INSTALLATION_SOURCE, installationSource);
			LOGGER.debug("Saved installation source: " + installationSource);
		}
	}
	
	public abstract Class<? extends Activity> getHomeActivityClass();

	public Class<?> getBuildConfigClass() {
		return ReflectionUtils.getClass(AndroidUtils.getApplicationId() + ".BuildConfig");
	}

	@SuppressWarnings("unchecked")
	public <T> T getBuildConfigValue(String property) {
		return (T)ReflectionUtils.getStaticFieldValue(AbstractApplication.get().getBuildConfigClass(), property);
	}

	@SuppressWarnings("unchecked")
	public <T> T getBuildConfigValue(String property, Object defaultValue) {
		return (T)ReflectionUtils.getStaticFieldValue(AbstractApplication.get().getBuildConfigClass(), property, defaultValue);
	}

	@NonNull
	protected abstract AppContext createAppContext();

	@NonNull
	public AppContext getAppContext() {
		return appContext;
	}

	@Nullable
	protected UpdateManager createUpdateManager() {
		return null;
	}

	@Nullable
	protected CacheManager createCacheManager() {
		return new CacheManager();
	}

	@Nullable
	public CacheManager getCacheManager() {
		synchronized (AbstractApplication.class) {
			if (cacheManager == null) {
				cacheManager = createCacheManager();
			}
		}
		return cacheManager;
	}

	@NonNull
	protected UriMapper createUriMapper() {
		return new UriMapper();
	}

	@NonNull
	public UriMapper getUriMapper() {
		return uriMapper;
	}

	@NonNull
	protected GitContext createGitContext() {
		return new AndroidGitContext();
	}

	@NonNull
	public GitContext getGitContext() {
		synchronized (AbstractApplication.class) {
			if (gitContext == null) {
				gitContext = createGitContext();
			}
		}
		return gitContext;
	}

	public AboutContext getAboutContext() {
		synchronized (AbstractApplication.class) {
			if (aboutContext == null) {
				aboutContext = createAboutContext();
			}
		}
		return aboutContext;
	}

	protected AboutContext createAboutContext() {
		return new AboutContext();
	}

	protected DebugContext createDebugContext() {
		return new DebugContext();
	}

	public DebugContext getDebugContext() {
		synchronized (AbstractApplication.class) {
			if (debugContext == null) {
				debugContext = createDebugContext();
			}
		}
		return debugContext;
	}

	public ActivityHelper createActivityHelper(AbstractFragmentActivity activity) {
		return new ActivityHelper(activity);
	}

	public FragmentHelper createFragmentHelper(Fragment fragment) {
		return new FragmentHelper(fragment);
	}
	
	public void setCurrentActivity(Activity activity) {
		currentActivity = activity;
	}
	
	public Activity getCurrentActivity() {
		return currentActivity;
	}
	
	/**
	 * @return the inBackground
	 */
	public Boolean isInBackground() {
		return inBackground;
	}
	
	/**
	 * @param inBackground the inBackground to set
	 */
	public void setInBackground(Boolean inBackground) {
		this.inBackground = inBackground;
	}
	
	public Boolean isLoadingCancelable() {
		return false;
	}
	
	public String getInstallationId() {
		return installationId;
	}
	
	private void loadInstallationId() {
		if (SharedPreferencesHelper.get().hasPreference(INSTALLATION_ID)) {
			installationId = SharedPreferencesHelper.get().loadPreference(INSTALLATION_ID);
		} else {
			installationId = UUID.randomUUID().toString();
			SharedPreferencesHelper.get().savePreference(INSTALLATION_ID, installationId);
		}
		LOGGER.debug("Installation id: " + installationId);
	}
	
	public String getAppName() {
		return getString(R.string.appName);
	}
	
	public UserRepository getUserRepository() {
		return null;
	}
	
	private void initRepositories() {
		repositories = new HashMap<>();
		
		initRepositories(repositories);
		
		if (isDatabaseEnabled()) {
			SQLiteHelper dbHelper = new SQLiteHelper(this);
			getDebugContext().initDebugRepositories(repositories, dbHelper);
			initDatabaseRepositories(repositories, dbHelper);
			dbHelper.addUpgradeSteps(getSQLiteUpgradeSteps());
		}
	}
	
	protected void initRepositories(Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories) {
		// Do nothing
	}
	
	protected void initDatabaseRepositories(
			Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories, SQLiteHelper dbHelper) {
		// Do nothing
	}
	
	public Boolean isDatabaseEnabled() {
		return false;
	}
	
	protected List<SQLiteUpgradeStep> getSQLiteUpgradeSteps() {
		return Lists.newArrayList();
	}
	
	public Boolean isDebugLogRepositoryEnabled() {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public <M extends Identifiable> Repository<M> getRepositoryInstance(Class<M> persistentClass) {
		return (Repository<M>)repositories.get(persistentClass);
	}

	public InAppBillingContext getInAppBillingContext() {
		synchronized (AbstractApplication.class) {
			if (inAppBillingContext == null) {
				inAppBillingContext = createInAppBillingContext();
			}
		}
		return inAppBillingContext;
	}

	protected InAppBillingContext createInAppBillingContext() {
		return new InAppBillingContext();
	}
	
	public AppLaunchStatus getAppLaunchStatus() {
		return appLaunchStatus;
	}

	public List<AppModule> getAppModules() {
		return appModules;
	}
}
