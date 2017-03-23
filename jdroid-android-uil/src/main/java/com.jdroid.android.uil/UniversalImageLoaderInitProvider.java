package com.jdroid.android.uil;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.provider.AbstractInitProvider;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class UniversalImageLoaderInitProvider extends AbstractInitProvider {
	
	@Override
	protected void init() {
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
		
	}
}
