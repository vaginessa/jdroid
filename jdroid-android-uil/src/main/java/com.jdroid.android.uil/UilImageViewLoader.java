package com.jdroid.android.uil;

import android.widget.ImageView;

import com.jdroid.android.images.loader.ImageViewLoader;

public class UilImageViewLoader implements ImageViewLoader {
	
	@Override
	public void displayImage(String url, ImageView imageView, Integer defaultImage, Long timeToLive) {
		UilImageLoaderHelper.displayImage(url, imageView, defaultImage, null, timeToLive);
	}
}
