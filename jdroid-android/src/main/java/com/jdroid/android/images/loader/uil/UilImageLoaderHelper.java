package com.jdroid.android.images.loader.uil;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.images.loader.ImageLoaderHelper;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.date.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UilImageLoaderHelper implements ImageLoaderHelper {

	private SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.get("imageLoader");
	private Map<String, Long> imagesExpirationMap;

	@Override
	@SuppressWarnings("unchecked")
	public void init() {

		// Create global configuration and initialize ImageLoader with this configuration

		DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
		displayImageOptionsBuilder.cacheInMemory(true);
		displayImageOptionsBuilder.cacheOnDisk(true);

		ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(
				AbstractApplication.get().getApplicationContext());
		configBuilder.tasksProcessingOrder(QueueProcessingType.LIFO);
		configBuilder.defaultDisplayImageOptions(displayImageOptionsBuilder.build());
		configBuilder.diskCacheSize(10 * 1024 * 1024);
		// configBuilder.writeDebugLogs();

		ImageLoader.getInstance().init(configBuilder.build());

		imagesExpirationMap = new ConcurrentHashMap<>(
				(Map<String, Long>)sharedPreferencesHelper.loadAllPreferences());
		clearExpiredDiskCaches();
	}

	@Override
	public void displayImage(String url, ImageView imageView, Integer defaultImage,
									ImageLoadingListener imageLoadingListener, Long timeToLive) {

		DisplayImageOptions.Builder optionsBuilder = new DisplayImageOptions.Builder();
		optionsBuilder.cacheInMemory(true);
		optionsBuilder.cacheOnDisk(true);
		if (defaultImage != null) {
			optionsBuilder.showImageOnLoading(defaultImage);
			optionsBuilder.showImageForEmptyUri(defaultImage);
			optionsBuilder.showImageOnFail(defaultImage);
		}

		ImageLoader.getInstance().displayImage(url, imageView, optionsBuilder.build(), imageLoadingListener);
		registerDownload(url, timeToLive);
	}

	@Override
	public void displayImage(String url, ImageView imageView, Integer defaultImage) {
		displayImage(url, imageView, defaultImage, null, null);
	}

	@Override
	public Bitmap loadBitmap(String url, ImageScaleType imageScaleType, int width, int height, Long timeToLive) {

		DisplayImageOptions.Builder optionsBuilder = new DisplayImageOptions.Builder();
		optionsBuilder.cacheInMemory(true);
		optionsBuilder.cacheOnDisk(true);
		optionsBuilder.imageScaleType(imageScaleType);

		ImageSize imageSize = new ImageSize(width, height);
		registerDownload(url, timeToLive);

		return ImageLoader.getInstance().loadImageSync(url, imageSize, optionsBuilder.build());
	}

	private void registerDownload(final String url, final Long timeToLive) {
		if ((url != null) && (timeToLive != null)) {
			ExecutorUtils.execute(new Runnable() {

				@Override
				public void run() {
					synchronized (UilImageLoaderHelper.class) {
						if (!imagesExpirationMap.containsKey(url)) {
							Long timestamp = DateUtils.nowMillis() + timeToLive;
							sharedPreferencesHelper.savePreference(url, timestamp);
							imagesExpirationMap.put(url, timestamp);
						}
					}
				}
			});
		}
	}

	@Override
	public void clearExpiredDiskCaches() {
		ExecutorUtils.execute(new Runnable() {

			@Override
			public void run() {
				synchronized (UilImageLoaderHelper.class) {
					for (Map.Entry<String, Long> entry : imagesExpirationMap.entrySet()) {
						if (DateUtils.nowMillis() > entry.getValue()) {
							DiskCacheUtils.removeFromCache(entry.getKey(), ImageLoader.getInstance().getDiskCache());
							sharedPreferencesHelper.removePreferences(entry.getKey());
							imagesExpirationMap.remove(entry.getKey());
						}
					}
				}
			}
		});
	}

	@Override
	public void clearDiskCache() {
		ImageLoader.getInstance().clearDiskCache();
	}
	@Override
	public void clearMemoryCache() {
		ImageLoader.getInstance().clearMemoryCache();
	}

	@Override
	public Integer getLibraryNameResId() {
		return R.string.universalImageLoaderTitle;
	}

	@Override
	public Integer getLibraryDescriptionResId() {
		return R.string.universalImageLoaderDescription;
	}

	@Override
	public String getLibraryUrl() {
		return "https://github.com/nostra13/Android-Universal-Image-Loader";
	}

	@Override
	public String getLibraryKey() {
		return "universalImageLoader";
	}
}
