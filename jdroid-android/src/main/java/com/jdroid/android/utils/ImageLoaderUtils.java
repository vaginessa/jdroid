package com.jdroid.android.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

/**
 * 
 * @author Maxi Rosson
 */
public class ImageLoaderUtils {
	
	public static void displayImage(String url, ImageView imageView, int defaultImage,
			ImageLoadingListener imageLoadingListener) {
		
		DisplayImageOptions.Builder optionsBuilder = new DisplayImageOptions.Builder();
		optionsBuilder.cacheInMemory(true);
		optionsBuilder.cacheOnDisc(true);
		optionsBuilder.showImageOnLoading(defaultImage);
		optionsBuilder.showImageForEmptyUri(defaultImage);
		optionsBuilder.showImageOnFail(defaultImage);
		
		ImageLoader.getInstance().displayImage(url, imageView, optionsBuilder.build(), imageLoadingListener);
	}
	
	public static void displayImage(String url, ImageView imageView, int defaultImage) {
		displayImage(url, imageView, defaultImage, null);
	}
	
	public static Bitmap loadBitmap(String url, ImageScaleType imageScaleType, int width, int height) {
		
		DisplayImageOptions.Builder optionsBuilder = new DisplayImageOptions.Builder();
		optionsBuilder.cacheInMemory(true);
		optionsBuilder.cacheOnDisc(true);
		optionsBuilder.imageScaleType(imageScaleType);
		
		ImageSize imageSize = new ImageSize(width, height);
		
		return ImageLoader.getInstance().loadImageSync(url, imageSize, optionsBuilder.build());
	}
	
}
