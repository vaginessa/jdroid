package com.jdroid.android.uil;

import android.graphics.Bitmap;
import android.support.annotation.WorkerThread;
import android.widget.ImageView;

import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.date.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UilImageLoaderHelper {

	private static SharedPreferencesHelper sharedPreferencesHelper;
	private static Map<String, Long> imagesExpirationMap;

	@WorkerThread
	@SuppressWarnings("unchecked")
	private static synchronized void init() {
		if (sharedPreferencesHelper == null) {

			sharedPreferencesHelper = SharedPreferencesHelper.get("imageLoader");

			imagesExpirationMap = new ConcurrentHashMap<>(
					(Map<String, Long>)sharedPreferencesHelper.loadAllPreferences());
			clearExpiredDiskCaches();
		}
	}

	public static void displayImage(String url, ImageView imageView, Integer defaultImage,
									ImageLoadingListener imageLoadingListener, Long timeToLive) {

		init();
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

	public static void displayImage(String url, ImageView imageView, Integer defaultImage) {
		displayImage(url, imageView, defaultImage, null, null);
	}

	public static Bitmap loadBitmap(String url, ImageScaleType imageScaleType, int width, int height, Long timeToLive) {

		init();
		DisplayImageOptions.Builder optionsBuilder = new DisplayImageOptions.Builder();
		optionsBuilder.cacheInMemory(true);
		optionsBuilder.cacheOnDisk(true);
		optionsBuilder.imageScaleType(imageScaleType);

		ImageSize imageSize = new ImageSize(width, height);
		registerDownload(url, timeToLive);

		return ImageLoader.getInstance().loadImageSync(url, imageSize, optionsBuilder.build());
	}

	private static void registerDownload(final String url, final Long timeToLive) {
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

	public static void clearExpiredDiskCaches() {
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

	public static void clearDiskCache() {
		ImageLoader.getInstance().clearDiskCache();
	}
	public static void clearMemoryCache() {
		ImageLoader.getInstance().clearMemoryCache();
	}
}
