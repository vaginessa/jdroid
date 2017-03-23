package com.jdroid.android.uil;

import android.graphics.Bitmap;

import com.jdroid.android.images.loader.BitmapLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class UilBitmapLoader implements BitmapLoader {
	
	private String url;
	
	public UilBitmapLoader(String url) {
		this.url = url;
	}
	
	@Override
	public Bitmap load(int height, int width) {
		return UilImageLoaderHelper.loadBitmap(url, ImageScaleType.EXACTLY, width, height, null);
	}
}
