package com.jdroid.android;

import java.io.File;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.slf4j.Logger;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.debug.DebugLog;
import com.jdroid.android.debug.DebugLogsRepository;
import com.jdroid.android.debug.DebugSettingsFragment;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.exception.ExceptionHandler;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.gcm.GcmMessageResolver;
import com.jdroid.android.repository.UserRepository;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.android.utils.AndroidEncryptionUtils;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.ImageLoaderUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.http.cache.Cache;
import com.jdroid.java.http.cache.CachedWebService;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;
import com.jdroid.java.utils.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public abstract class AbstractApplication extends Application {
	
	/**
	 * The LOGGER variable is initialized in the "OnCreate" method, after that "LoggerUtils" has been properly
	 * configured by the superclass.
	 */
	protected static Logger LOGGER;
	
	private static final String INSTALLATION_ID = "installationId";
	public static final String INSTALLATION_SOURCE = "installationSource";
	private static final String VERSION_CODE_KEY = "versionCodeKey";
	
	private static final String CACHE_DIRECTORY_PREFFIX = "cache_";
	
	protected static AbstractApplication INSTANCE;
	
	private AppContext appContext;
	private AnalyticsSender<? extends AnalyticsTracker> analyticsSender;
	
	/** Current activity in the top stack. */
	private Activity currentActivity;
	
	private String installationId;
	private boolean inBackground = false;
	
	private AppLaunchStatus appLaunchStatus;
	
	private Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories;
	
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
		
		changeLocale();
		
		LoggerUtils.setEnabled(isDebuggable());
		LOGGER = LoggerUtils.getLogger(AbstractApplication.class);
		
		appContext = createAppContext();
		analyticsSender = createAnalyticsSender();
		
		initExceptionHandlers();
		
		if (appContext.displayDebugSettings()) {
			PreferenceManager.setDefaultValues(this, R.xml.debug_preferences, false);
		}
		initStrictMode();
		
		// This is required to initialize the statics fields of the utils classes.
		ToastUtils.init();
		DateUtils.init();
		
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				loadInstallationId();
				verifyAppLaunchStatus();
				initFileSystemCache();
				initEncryptionUtils();
			}
		});
		
		initRepositories();
		initImageLoader();
	}
	
	protected void initImageLoader() {
		// Create global configuration and initialize ImageLoader with this configuration
		
		DisplayImageOptions.Builder defaultOptiBuilder = new DisplayImageOptions.Builder();
		defaultOptiBuilder.cacheInMemory(true);
		defaultOptiBuilder.cacheOnDisk(true);
		
		ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(getApplicationContext());
		configBuilder.tasksProcessingOrder(QueueProcessingType.LIFO);
		configBuilder.defaultDisplayImageOptions(defaultOptiBuilder.build());
		configBuilder.diskCacheSize(10 * 1024 * 1024);
		// configBuilder.writeDebugLogs();
		
		ImageLoader.getInstance().init(configBuilder.build());
		
		ImageLoaderUtils.clearImagesCache();
	}
	
	protected void verifyAppLaunchStatus() {
		Integer fromVersionCode = SharedPreferencesHelper.getOldDefault().loadPreferenceAsInteger(VERSION_CODE_KEY);
		if (fromVersionCode == null) {
			appLaunchStatus = AppLaunchStatus.NEW_INSTALATTION;
			fromVersionCode = 0;
		} else {
			if (AndroidUtils.getVersionCode().equals(fromVersionCode)) {
				appLaunchStatus = AppLaunchStatus.NORMAL;
			} else {
				appLaunchStatus = AppLaunchStatus.VERSION_UPGRADE;
			}
		}
		LOGGER.debug("App launch status: " + appLaunchStatus);
		SharedPreferencesHelper.getOldDefault().savePreference(VERSION_CODE_KEY, AndroidUtils.getVersionCode());
	}
	
	protected AnalyticsSender<? extends AnalyticsTracker> createAnalyticsSender() {
		return new AnalyticsSender<AnalyticsTracker>();
	}
	
	@SuppressWarnings("unchecked")
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
	
	protected void initFileSystemCache() {
		try {
			
			// Sort the caches by priority
			List<Cache> caches = getFileSystemCaches();
			Collections.sort(caches, new Comparator<Cache>() {
				
				@Override
				public int compare(Cache cache1, Cache cache2) {
					return cache2.getPriority().compareTo(cache1.getPriority());
				}
			});
			
			for (Cache cache : caches) {
				populateFileSystemCache(cache);
				reduceFileSystemCache(cache);
			}
		} catch (Exception e) {
			getExceptionHandler().logHandledException(e);
		}
	}
	
	@SuppressWarnings("resource")
	protected void populateFileSystemCache(Cache cache) {
		
		if (!appLaunchStatus.equals(AppLaunchStatus.NORMAL)) {
			
			Map<String, String> defaultContent = cache.getDefaultContent();
			
			if ((defaultContent != null) && !defaultContent.isEmpty()) {
				for (Entry<String, String> entry : defaultContent.entrySet()) {
					InputStream source = AbstractApplication.class.getClassLoader().getResourceAsStream(
						"cache/" + entry.getKey());
					if (source != null) {
						File cacheFile = new File(AbstractApplication.get().getFileSystemCacheDirectory(cache),
								CachedWebService.generateCacheFileName(entry.getValue()));
						FileUtils.copyStream(source, cacheFile);
						LOGGER.debug("Populated " + entry.toString() + " to " + cacheFile.getAbsolutePath());
						FileUtils.safeClose(source);
					}
				}
				LOGGER.debug(cache.getName() + " cache populated");
			}
		}
	}
	
	protected void reduceFileSystemCache(Cache cache) {
		if (cache.getMaximumSize() != null) {
			File dir = getFileSystemCacheDirectory(cache);
			
			// Verify if the cache should be clean
			if (dir != null) {
				float dirSize = FileUtils.getDirectorySizeInMB(dir);
				LOGGER.info("Cache " + cache.getName() + " size: " + dirSize + " MB");
				if (dirSize > cache.getMaximumSize()) {
					// Sort the files by modification date, so we remove the not used files first
					List<File> files = Lists.newArrayList(dir.listFiles());
					Collections.sort(files, new Comparator<File>() {
						
						@Override
						public int compare(File file1, File file2) {
							return Long.valueOf(file1.lastModified()).compareTo(file2.lastModified());
						}
					});
					
					// Remove the file until the minumum size is achieved
					for (File file : files) {
						if (dirSize > cache.getMinimumSize()) {
							dirSize -= FileUtils.getFileSizeInMB(file);
							FileUtils.forceDelete(file);
						} else {
							break;
						}
					}
				}
			}
		}
	}
	
	protected List<Cache> getFileSystemCaches() {
		return Lists.newArrayList();
	}
	
	public void cleanFileSystemCache() {
		for (Cache each : getFileSystemCaches()) {
			cleanFileSystemCache(each);
		}
	}
	
	public void cleanFileSystemCache(Cache cache) {
		FileUtils.forceDelete(getFileSystemCacheDirectory(cache));
	}
	
	public File getFileSystemCacheDirectory(Cache cache) {
		return getApplicationContext().getDir(CACHE_DIRECTORY_PREFFIX + cache.getName(), Context.MODE_PRIVATE);
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
	
	protected void initEncryptionUtils() {
		AndroidEncryptionUtils.init();
	}
	
	public ExceptionHandler getExceptionHandler() {
		return (ExceptionHandler)Thread.getDefaultUncaughtExceptionHandler();
	}
	
	public Class<? extends ExceptionHandler> getExceptionHandlerClass() {
		return DefaultExceptionHandler.class;
	}
	
	public void saveInstallationSource() {
		String installationSource = SharedPreferencesHelper.getOldDefault().loadPreference(INSTALLATION_SOURCE);
		if (StringUtils.isBlank(installationSource)) {
			installationSource = appContext.getInstallationSource();
			SharedPreferencesHelper.getOldDefault().savePreference(INSTALLATION_SOURCE, installationSource);
			LOGGER.debug("Saved installation source: " + installationSource);
		}
	}
	
	public abstract Class<? extends Activity> getHomeActivityClass();
	
	protected AppContext createAppContext() {
		return new AppContext();
	}
	
	public AppContext getAppContext() {
		return appContext;
	}
	
	public ActivityHelper createActivityHelper(Activity activity) {
		return new ActivityHelper(activity);
	}
	
	public FragmentHelper createFragmentHelper(Fragment fragment) {
		return new FragmentHelper(fragment);
	}
	
	public Boolean isInAppBillingEnabled() {
		return false;
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
	public boolean isInBackground() {
		return inBackground;
	}
	
	/**
	 * @param inBackground the inBackground to set
	 */
	public void setInBackground(boolean inBackground) {
		this.inBackground = inBackground;
	}
	
	public Boolean isLoadingCancelable() {
		return false;
	}
	
	public String getInstallationId() {
		return installationId;
	}
	
	private void loadInstallationId() {
		if (SharedPreferencesHelper.getOldDefault().hasPreference(INSTALLATION_ID)) {
			installationId = SharedPreferencesHelper.getOldDefault().loadPreference(INSTALLATION_ID);
		} else {
			installationId = UUID.randomUUID().toString();
			SharedPreferencesHelper.getOldDefault().savePreference(INSTALLATION_ID, installationId);
		}
		LOGGER.debug("Installation id: " + installationId);
	}
	
	public String getAppName() {
		return getString(R.string.appName);
	}
	
	private boolean isDebuggable() {
		int flags = this.getApplicationInfo().flags;
		return (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
	}
	
	public GcmMessageResolver getGcmResolver() {
		return null;
	}
	
	public UserRepository getUserRepository() {
		return null;
	}
	
	private void initRepositories() {
		repositories = new HashMap<Class<? extends Identifiable>, Repository<? extends Identifiable>>();
		
		initRepositories(repositories);
		
		if (isDatabaseEnabled()) {
			SQLiteHelper dbHelper = new SQLiteHelper(this);
			if (isDebugLogRepositoryEnabled() && !appContext.isProductionEnvironment()) {
				repositories.put(DebugLog.class, new DebugLogsRepository(dbHelper));
			}
			initDatabaseRepositories(repositories, dbHelper);
		}
	}
	
	protected void initRepositories(Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories) {
	}
	
	protected void initDatabaseRepositories(
			Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories, SQLiteHelper dbHelper) {
	}
	
	protected Boolean isDatabaseEnabled() {
		return false;
	}
	
	protected Boolean isDebugLogRepositoryEnabled() {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public <M extends Identifiable> Repository<M> getRepositoryInstance(Class<M> persistentClass) {
		return (Repository<M>)repositories.get(persistentClass);
	}
	
	public Class<? extends AbstractPreferenceFragment> getDebugSettingsFragmentClass() {
		return DebugSettingsFragment.class;
	}
	
	protected String getFixedLocaleCountryCode() {
		return null;
	}
	
	public void changeLocale() {
		
		String countryCode = getFixedLocaleCountryCode();
		
		if (countryCode != null) {
			Locale locale = new Locale(countryCode);
			Locale.setDefault(locale);
			
			Context baseContext = getBaseContext();
			if (baseContext != null) {
				Resources resources = baseContext.getResources();
				if (resources != null) {
					Configuration config = resources.getConfiguration();
					if (config != null) {
						config.locale = locale;
						resources.updateConfiguration(config, getResources().getDisplayMetrics());
					}
				}
			}
		}
		
	}
}
