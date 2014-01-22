package com.jdroid.android.images;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import android.graphics.Bitmap;
import android.net.Uri;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.concurrent.SafeRunnable;
import com.jdroid.android.contacts.ContactImageResolver;
import com.jdroid.android.utils.BitmapUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.LowPriorityThreadFactory;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;

public class ImageLoader {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(ImageLoader.class);
	
	private static final ImageLoader INSTANCE = new ImageLoader();
	
	private List<ImageResolver> imageResolvers = Lists.newArrayList();
	
	private Executor firstLevelExecutor = Executors.newFixedThreadPool(5, new LowPriorityThreadFactory());
	private Executor secondLevelExecutor = Executors.newFixedThreadPool(5, new LowPriorityThreadFactory());
	
	public static ImageLoader get() {
		return INSTANCE;
	}
	
	private ImageLoader() {
		imageResolvers.add(ReflectedRemoteImageResolver.get());
		imageResolvers.add(FileSystemImageResolver.get());
		imageResolvers.add(RemoteImageResolver.get());
		imageResolvers.add(MediaImageResolver.get());
		imageResolvers.add(ContactImageResolver.get());
	}
	
	public void displayImage(final Uri uri, final ImageHolder imageHolder, final Boolean memoryCacheEnabled,
			final Boolean fileSystemCacheEnabled) {
		
		// Memory cache
		Bitmap bitmap = memoryCacheEnabled ? loadFromMemoryCache(uri) : null;
		if (bitmap != null) {
			// Display cached image
			setImageBitmap(bitmap, imageHolder);
		} else {
			
			setStubImage(imageHolder);
			// Make the background threads low priority. This way it will not affect the UI performance.
			firstLevelExecutor.execute(new ImageLoaderRunnable(uri, imageHolder, memoryCacheEnabled,
					fileSystemCacheEnabled, true));
		}
	}
	
	protected Bitmap loadFromMemoryCache(Uri uri) {
		Bitmap bitmap = AbstractApplication.get().getBitmapLruCache().get(uri.toString());
		if ((bitmap != null) && LoggerUtils.isEnabled()) {
			LOGGER.debug("Loaded image [" + uri.toString() + "] from memory");
		}
		return bitmap;
	}
	
	protected void saveToMemoryCache(Bitmap bitmap, Uri uri) {
		AbstractApplication.get().getBitmapLruCache().put(uri.toString(), bitmap);
		if (LoggerUtils.isEnabled()) {
			LOGGER.debug("Saved image [" + uri.toString() + "] on memory cache");
		}
	}
	
	protected Bitmap loadFromFileSystemCache(Uri uri, Integer maxWidth, Integer maxHeight) {
		File directory = AbstractApplication.get().getImagesCacheDirectory();
		File file = new File(directory, String.valueOf(uri.toString().hashCode()));
		Bitmap bitmap = null;
		if (file.exists()) {
			bitmap = BitmapUtils.toBitmap(Uri.fromFile(file), maxWidth, maxHeight);
			if (LoggerUtils.isEnabled()) {
				LOGGER.debug("Loaded image [" + uri.toString() + "] from [" + file.getAbsolutePath() + "].");
			}
		}
		return bitmap;
	}
	
	protected void saveToFileSystemCache(Bitmap bitmap, Uri uri) {
		@SuppressWarnings("resource")
		ByteArrayInputStream byteArrayInputStream = BitmapUtils.toPNGInputStream(bitmap);
		File directory = AbstractApplication.get().getImagesCacheDirectory();
		File file = new File(directory, String.valueOf(uri.toString().hashCode()));
		FileUtils.copyStream(byteArrayInputStream, file);
		
		if (LoggerUtils.isEnabled()) {
			LOGGER.debug("Saved image [" + uri.toString() + "] on [" + file.getAbsolutePath() + "].");
		}
	}
	
	private class ImageLoaderRunnable extends SafeRunnable {
		
		private Uri uri;
		private SoftReference<ImageHolder> imageHolderReference;
		private Boolean memoryCacheEnabled;
		private Boolean fileSystemCacheEnabled;
		
		private Boolean resolveFromFileSystem;
		
		public ImageLoaderRunnable(Uri uri, ImageHolder imageHolder, Boolean memoryCacheEnabled,
				Boolean fileSystemCacheEnabled, Boolean resolveFromFileSystem) {
			this.uri = uri;
			this.memoryCacheEnabled = memoryCacheEnabled;
			this.fileSystemCacheEnabled = fileSystemCacheEnabled;
			imageHolderReference = new SoftReference<ImageHolder>(imageHolder);
			this.resolveFromFileSystem = fileSystemCacheEnabled && resolveFromFileSystem;
		}
		
		private Bitmap resolveBitmap() {
			Bitmap bitmap = null;
			ImageHolder imageHolder = getImageHolder();
			if ((imageHolder != null) && !isExpired()) {
				
				if (resolveFromFileSystem) {
					// File system cache
					bitmap = fileSystemCacheEnabled ? loadFromFileSystemCache(uri, imageHolder.getMaximumWidth(),
						imageHolder.getMaximumHeight()) : null;
				} else {
					ImageResolver imageResolver = null;
					for (ImageResolver each : imageResolvers) {
						if (each.canResolve(uri)) {
							imageResolver = each;
							break;
						}
					}
					
					if (imageResolver != null) {
						bitmap = imageResolver.resolve(uri, imageHolder.getMaximumWidth(),
							imageHolder.getMaximumHeight());
					} else {
						LOGGER.error("The Image loader couldn't resolve the uri: " + uri.toString());
					}
				}
			}
			return bitmap;
		}
		
		/**
		 * @see com.jdroid.android.concurrent.SafeRunnable#doRun()
		 */
		@Override
		public void doRun() {
			
			final Bitmap bitmap = resolveBitmap();
			
			final ImageHolder imageHolder = getImageHolder();
			if ((imageHolder != null) && !isExpired()) {
				imageHolder.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if (!isExpired()) {
							if (bitmap != null) {
								setImageBitmap(bitmap, imageHolder);
							} else {
								setStubImage(imageHolder);
							}
						}
					}
				});
			}
			
			if (bitmap != null) {
				if (memoryCacheEnabled) {
					saveToMemoryCache(bitmap, uri);
				}
				
				if (fileSystemCacheEnabled) {
					saveToFileSystemCache(bitmap, uri);
				}
			} else {
				if (resolveFromFileSystem) {
					secondLevelExecutor.execute(new ImageLoaderRunnable(uri, imageHolder, memoryCacheEnabled,
							fileSystemCacheEnabled, false));
				}
			}
		}
		
		private ImageHolder getImageHolder() {
			return imageHolderReference.get();
		}
		
		private Boolean isExpired() {
			ImageHolder imageHolder = getImageHolder();
			return (imageHolder == null) || !imageHolder.getImageUri().equals(uri);
		}
	}
	
	private void setImageBitmap(final Bitmap bitmap, final ImageHolder imageHolder) {
		imageHolder.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				imageHolder.setImageBitmap(bitmap);
				if (imageHolder.getImageLoadingListener() != null) {
					imageHolder.getImageLoadingListener().onImageLoaded();
				}
			}
		});
	}
	
	private void setStubImage(final ImageHolder imageHolder) {
		imageHolder.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				imageHolder.showStubImage();
				if (imageHolder.getImageLoadingListener() != null) {
					imageHolder.getImageLoadingListener().onStubImageLoaded();
				}
			}
		});
	}
}
