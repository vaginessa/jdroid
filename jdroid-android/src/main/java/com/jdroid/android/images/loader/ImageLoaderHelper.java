package com.jdroid.android.images.loader;

import android.graphics.Bitmap;
import android.support.annotation.WorkerThread;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public interface ImageLoaderHelper {

	@WorkerThread
	public void init();

	public void displayImage(String url, ImageView imageView, Integer defaultImage,
							 ImageLoadingListener imageLoadingListener, Long timeToLive);

	public void displayImage(String url, ImageView imageView, Integer defaultImage);

	public Bitmap loadBitmap(String url, ImageScaleType imageScaleType, int width, int height, Long timeToLive);

	public void clearDiskCache();

	public void clearExpiredDiskCaches();

	public void clearMemoryCache();

	public Integer getLibraryNameResId();

	public Integer getLibraryDescriptionResId();

	public String getLibraryUrl();

	public String getLibraryKey();
}
