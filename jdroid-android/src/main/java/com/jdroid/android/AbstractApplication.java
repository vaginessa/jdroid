package com.jdroid.android;

import java.io.File;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.json.JSONObject;
import org.slf4j.Logger;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import com.crittercism.app.Crittercism;
import com.crittercism.app.CrittercismConfig;
import com.flurry.sdk.eq;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.billing.BillingContext;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.exception.ExceptionHandler;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.gcm.GcmMessageResolver;
import com.jdroid.android.images.BitmapLruCache;
import com.jdroid.android.repository.UserRepository;
import com.jdroid.android.utils.AndroidEncryptionUtils;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.SharedPreferencesUtils;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.context.GitContext;
import com.jdroid.java.http.cache.Cache;
import com.jdroid.java.http.cache.CachedWebService;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;
import com.jdroid.java.utils.StringUtils;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractApplication extends Application {
	
	/**
	 * The LOGGER variable is initialized in the "OnCreate" method, after that "LoggerUtils" has been properly
	 * configured by the superclass.
	 */
	protected static Logger LOGGER;
	
	private static final String INSTALLATION_ID = "installationId";
	public static final String INSTALLATION_SOURCE = "installationSource";
	private static final String VERSION_CODE_KEY = "versionCodeKey";
	
	/** Maximum size (in MB) of the images cache */
	private static final int IMAGES_CACHE_SIZE = 5;
	
	private static final String IMAGES_DIRECTORY = "images";
	private static final String HTTP_CACHE_DIRECTORY_PREFFIX = "http_";
	
	protected static AbstractApplication INSTANCE;
	
	private DefaultApplicationContext applicationContext;
	
	/** Current activity in the top stack. */
	private Activity currentActivity;
	
	private File cacheDirectory;
	private File imagesCacheDirectory;
	private BitmapLruCache bitmapLruCache;
	
	private String installationId;
	private boolean inBackground = false;
	
	private AppLaunchStatus appLaunchStatus;
	
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
		
		LoggerUtils.setEnabled(isDebuggable());
		LOGGER = LoggerUtils.getLogger(AbstractApplication.class);
		
		applicationContext = createApplicationContext();
		
		if (isDebuggable()) {
			GitContext.init();
		}
		
		if (applicationContext.displayDebugSettings()) {
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
				initHttpCache();
				initEncryptionUtils();
				initCacheDirectory();
				initImagesCacheDirectory();
				initBitmapLruCache();
			}
		});
		
		initAnalytics();
		initInAppBilling();
	}
	
	/**
	 * @see android.app.Application#onTrimMemory(int)
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		
		if (level >= TRIM_MEMORY_MODERATE) {
			bitmapLruCache.evictAll();
		}
	}
	
	protected void verifyAppLaunchStatus() {
		Integer fromVersionCode = SharedPreferencesUtils.loadPreferenceAsInteger(VERSION_CODE_KEY);
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
		SharedPreferencesUtils.savePreference(VERSION_CODE_KEY, AndroidUtils.getVersionCode());
	}
	
	protected void initAnalytics() {
		if (hasAnalyticsSender()) {
			getAnalyticsSender().init();
		}
	}
	
	public Boolean hasAnalyticsSender() {
		return getAnalyticsSender() != null;
	}
	
	public <T extends AnalyticsTracker> AnalyticsSender<T> getAnalyticsSender() {
		return null;
	}
	
	public Boolean isStrictModeEnabled() {
		return true;
	}
	
	private void initStrictMode() {
		if (!applicationContext.isProductionEnvironment() && isStrictModeEnabled()) {
			StrictMode.enableDefaults();
		}
	}
	
	protected void initCacheDirectory() {
		// Configure the cache dir for the whole application
		cacheDirectory = getExternalCacheDir();
		if (cacheDirectory == null) {
			// TODO We could listen the Intent.ACTION_DEVICE_STORAGE_LOW and clear the cache
			cacheDirectory = getCacheDir();
		}
		LOGGER.debug("Cache directory: " + cacheDirectory.getPath());
	}
	
	protected void initImagesCacheDirectory() {
		imagesCacheDirectory = new File(getCacheDirectory(), IMAGES_DIRECTORY);
		LOGGER.debug("Images cache directory: " + imagesCacheDirectory.getPath());
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				File dir = getImagesCacheDirectory();
				if ((dir != null) && ((FileUtils.getDirectorySize(dir) / FileUtils.BYTES_TO_MB) > IMAGES_CACHE_SIZE)) {
					FileUtils.forceDelete(imagesCacheDirectory);
				}
			}
		});
	}
	
	protected void initBitmapLruCache() {
		// Get memory class of this device, exceeding this amount will throw an OutOfMemory exception.
		int memClass = ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = (1024 * 1024 * memClass) / 8;
		bitmapLruCache = new BitmapLruCache(cacheSize);
	}
	
	protected void initHttpCache() {
		try {
			
			// Sort the caches by priority
			List<Cache> httpCaches = getHttpCaches();
			Collections.sort(httpCaches, new Comparator<Cache>() {
				
				@Override
				public int compare(Cache cache1, Cache cache2) {
					return cache2.getPriority().compareTo(cache1.getPriority());
				}
			});
			
			for (Cache cache : httpCaches) {
				populateHttpCache(cache);
				reduceHttpCache(cache);
			}
		} catch (Exception e) {
			getExceptionHandler().logHandledException(e);
		}
	}
	
	@SuppressWarnings("resource")
	protected void populateHttpCache(Cache cache) {
		
		if (!appLaunchStatus.equals(AppLaunchStatus.NORMAL)) {
			
			Map<String, String> defaultContent = cache.getDefaultContent();
			
			if ((defaultContent != null) && !defaultContent.isEmpty()) {
				for (Entry<String, String> entry : defaultContent.entrySet()) {
					InputStream source = AbstractApplication.class.getClassLoader().getResourceAsStream(
						"cache/" + entry.getKey());
					if (source != null) {
						File cacheFile = new File(AbstractApplication.get().getHttpCacheDirectory(cache),
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
	
	protected void reduceHttpCache(Cache cache) {
		if (cache.getMaximumSize() != null) {
			File dir = getHttpCacheDirectory(cache);
			
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
	
	protected List<Cache> getHttpCaches() {
		return Lists.newArrayList();
	}
	
	public void cleanHttpCache(Cache cache) {
		FileUtils.forceDelete(getHttpCacheDirectory(cache));
	}
	
	public File getHttpCacheDirectory(Cache cache) {
		return getApplicationContext().getDir(HTTP_CACHE_DIRECTORY_PREFFIX + cache.getName(), Context.MODE_PRIVATE);
	}
	
	private void initInAppBilling() {
		if (isInAppBillingEnabled()) {
			BillingContext.get().initialize();
		}
	}
	
	public void initExceptionHandlers() {
		UncaughtExceptionHandler currentExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		if ((currentExceptionHandler == null) || !currentExceptionHandler.getClass().equals(getExceptionHandlerClass())) {
			
			// If Flurry is enabled, we initialize its exception handler as the first custom exception handler
			if (applicationContext.isFlurryEnabled()) {
				eq.a();
			}
			
			initCrittercism(getExceptionHandlerMetadata());
			
			ExceptionHandler exceptionHandler = ReflectionUtils.newInstance(getExceptionHandlerClass());
			exceptionHandler.setDefaultExceptionHandler(currentExceptionHandler);
			Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
			LOGGER.info("Custom exception handler initialized");
		}
	}
	
	protected JSONObject getExceptionHandlerMetadata() {
		return null;
	}
	
	private void initCrittercism(JSONObject metadata) {
		
		if (applicationContext.isCrittercismEnabled()) {
			try {
				// send logcat data for devices with API Level 16 and higher
				CrittercismConfig crittercismConfig = new CrittercismConfig();
				crittercismConfig.setLogcatReportingEnabled(true);
				
				Crittercism.initialize(getApplicationContext(), applicationContext.getCrittercismAppId(),
					crittercismConfig);
				
				if (installationId != null) {
					Crittercism.setUsername(installationId);
				}
				if (metadata != null) {
					Crittercism.setMetadata(metadata);
				}
				LOGGER.debug("Crittercism initialized");
			} catch (Exception e) {
				LOGGER.error("Error when initializing Crittercism");
			}
		}
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
		String installationSource = SharedPreferencesUtils.loadPreference(INSTALLATION_SOURCE);
		if (StringUtils.isBlank(installationSource)) {
			installationSource = applicationContext.getInstallationSource();
			SharedPreferencesUtils.savePreference(INSTALLATION_SOURCE, installationSource);
			LOGGER.debug("Saved installation source: " + installationSource);
		}
	}
	
	/**
	 * @return the bitmapLruCache
	 */
	public BitmapLruCache getBitmapLruCache() {
		return bitmapLruCache;
	}
	
	public abstract Class<? extends Activity> getHomeActivityClass();
	
	protected DefaultApplicationContext createApplicationContext() {
		return new DefaultApplicationContext();
	}
	
	public DefaultApplicationContext getAndroidApplicationContext() {
		return applicationContext;
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
	 * @return the cacheDirectory
	 */
	public File getCacheDirectory() {
		if (!cacheDirectory.exists()) {
			cacheDirectory.mkdirs();
		}
		return cacheDirectory;
	}
	
	public File getImagesCacheDirectory() {
		if (!imagesCacheDirectory.exists()) {
			imagesCacheDirectory.mkdirs();
		}
		return imagesCacheDirectory.exists() && imagesCacheDirectory.isDirectory() ? imagesCacheDirectory : null;
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
		if (SharedPreferencesUtils.hasPreference(INSTALLATION_ID)) {
			installationId = SharedPreferencesUtils.loadPreference(INSTALLATION_ID);
		} else {
			installationId = UUID.randomUUID().toString();
			SharedPreferencesUtils.savePreference(INSTALLATION_ID, installationId);
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
}
