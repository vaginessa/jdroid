package com.jdroid.android.images;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.domain.UriFileContent;

public class CustomImageView extends ImageView implements ImageHolder {
	
	private static final String INSTANCE_STATE_EXTRA = "instanceState";
	private static final String IMAGE_URI_EXTRA = "imageUri";
	private static final String MEMORY_CACHE_ENABLE_EXTRA = "memoryCacheEnabled";
	private static final String FILESYSTEM_CACHE_ENABLED_EXTRA = "fileSystemCacheEnable";
	
	private int stubId;
	private Integer maxWidth;
	private Integer maxHeight;
	private Boolean memoryCacheEnabled;
	private Boolean fileSystemCacheEnable;
	private Uri imageUri;
	private ImageLoadingListener imageLoadingListener;
	private Boolean saveState = false;
	
	public CustomImageView(Context context) {
		super(context);
	}
	
	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setSaveState(Boolean saveState) {
		this.saveState = saveState;
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(java.lang.String, int)
	 */
	@Override
	public void setImageContent(String url, int stubId) {
		setImageContent(url, stubId, null, null);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(java.lang.String, int, java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	public void setImageContent(String url, int stubId, Integer maxWidth, Integer maxHeight) {
		setImageContent(url, stubId, null, null, true, true);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(java.lang.String, int, java.lang.Integer,
	 *      java.lang.Integer, java.lang.Boolean, java.lang.Boolean)
	 */
	@Override
	public void setImageContent(String url, int stubId, Integer maxWidth, Integer maxHeight,
			Boolean memoryCacheEnabled, Boolean fileSystemCacheEnabled) {
		setImageContent(new UriFileContent(url), stubId, maxWidth, maxHeight, memoryCacheEnabled,
			fileSystemCacheEnabled);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(com.jdroid.android.domain.FileContent, int)
	 */
	@Override
	public void setImageContent(FileContent fileContent, int stubId) {
		setImageContent(fileContent, stubId, null, null);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(com.jdroid.android.domain.FileContent, int,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void setImageContent(FileContent fileContent, int stubId, Integer maxWidth, Integer maxHeight) {
		setImageContent(fileContent, stubId, maxWidth, maxHeight, true, true);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(com.jdroid.android.domain.FileContent, int,
	 *      java.lang.Integer, java.lang.Integer, java.lang.Boolean, java.lang.Boolean)
	 */
	@Override
	public void setImageContent(FileContent fileContent, int stubId, Integer maxWidth, Integer maxHeight,
			Boolean memoryCacheEnabled, Boolean fileSystemCacheEnabled) {
		setImageContent(fileContent != null ? fileContent.getUri() : null, stubId, maxWidth, maxHeight,
			memoryCacheEnabled, fileSystemCacheEnabled);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(android.net.Uri, int)
	 */
	@Override
	public void setImageContent(Uri imageUri, int stubId) {
		setImageContent(imageUri, stubId, null, null);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(android.net.Uri, int, java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	public void setImageContent(Uri imageUri, int stubId, Integer maxWidth, Integer maxHeight) {
		setImageContent(imageUri, stubId, maxWidth, maxHeight, true, true);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(android.net.Uri, int, java.lang.Integer,
	 *      java.lang.Integer, java.lang.Boolean, java.lang.Boolean)
	 */
	@Override
	public void setImageContent(Uri imageUri, int stubId, Integer maxWidth, Integer maxHeight,
			Boolean memoryCacheEnabled, Boolean fileSystemCacheEnabled) {
		this.stubId = stubId;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		if (imageUri != null) {
			this.imageUri = imageUri;
			ImageLoader.get().displayImage(imageUri, this, memoryCacheEnabled, fileSystemCacheEnabled);
		} else {
			showStubImage();
			if (imageLoadingListener != null) {
				imageLoadingListener.onStubImageLoaded();
			}
		}
	}
	
	/**
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	protected Parcelable onSaveInstanceState() {
		if (saveState && (imageUri != null)) {
			Bundle bundle = new Bundle();
			bundle.putParcelable(INSTANCE_STATE_EXTRA, super.onSaveInstanceState());
			bundle.putParcelable(IMAGE_URI_EXTRA, imageUri);
			bundle.putBoolean(MEMORY_CACHE_ENABLE_EXTRA, memoryCacheEnabled);
			bundle.putBoolean(FILESYSTEM_CACHE_ENABLED_EXTRA, fileSystemCacheEnable);
			return bundle;
		} else {
			return super.onSaveInstanceState();
		}
	}
	
	/**
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle)state;
			imageUri = bundle.getParcelable(IMAGE_URI_EXTRA);
			memoryCacheEnabled = bundle.getBoolean(MEMORY_CACHE_ENABLE_EXTRA);
			fileSystemCacheEnable = bundle.getBoolean(FILESYSTEM_CACHE_ENABLED_EXTRA);
			if (imageUri != null) {
				ImageLoader.get().displayImage(imageUri, this, memoryCacheEnabled, fileSystemCacheEnable);
			}
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE_EXTRA));
		} else {
			super.onRestoreInstanceState(state);
		}
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#showStubImage()
	 */
	@Override
	public void showStubImage() {
		this.setImageResource(stubId);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#getMaximumWidth()
	 */
	@Override
	public Integer getMaximumWidth() {
		return maxWidth;
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#getMaximumHeight()
	 */
	@Override
	public Integer getMaximumHeight() {
		return maxHeight;
	}
	
	/**
	 * @return the imageUri
	 */
	@Override
	public Uri getImageUri() {
		return imageUri;
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#getImageLoadingListener()
	 */
	@Override
	public ImageLoadingListener getImageLoadingListener() {
		return imageLoadingListener;
	}
	
	/**
	 * @param imageLoadingListener the imageLoadingListener to set
	 */
	public void setImageLoadingListener(ImageLoadingListener imageLoadingListener) {
		this.imageLoadingListener = imageLoadingListener;
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#runOnUiThread(java.lang.Runnable)
	 */
	@Override
	public void runOnUiThread(Runnable runnable) {
		AbstractApplication.get().getCurrentActivity().runOnUiThread(runnable);
	}
	
	public static interface ImageLoadingListener {
		
		public void onImageLoaded();
		
		public void onStubImageLoaded();
	}
	
}
