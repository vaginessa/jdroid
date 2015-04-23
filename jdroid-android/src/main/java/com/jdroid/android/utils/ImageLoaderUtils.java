package com.jdroid.android.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.jdroid.java.concurrent.ExecutorUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ImageLoaderUtils {
	
	private static SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.get("imageLoader");
	@SuppressWarnings("unchecked")
	private static Map<String, Long> imagesExpirationMap = new ConcurrentHashMap<>(
			(Map<String, Long>)sharedPreferencesHelper.loadAllPreferences());
	
	public static void displayImage(String url, ImageView imageView, Integer defaultImage,
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
	
	public static void displayImage(String url, ImageView imageView, Integer defaultImage) {
		displayImage(url, imageView, defaultImage, null, null);
	}
	
	public static Bitmap loadBitmap(String url, ImageScaleType imageScaleType, int width, int height, Long timeToLive) {
		
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
					synchronized (ImageLoaderUtils.class) {
						if (!imagesExpirationMap.containsKey(url)) {
							Long timestamp = System.currentTimeMillis() + timeToLive;
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
				synchronized (ImageLoaderUtils.class) {
					for (Entry<String, Long> entry : imagesExpirationMap.entrySet()) {
						if (System.currentTimeMillis() > entry.getValue()) {
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
