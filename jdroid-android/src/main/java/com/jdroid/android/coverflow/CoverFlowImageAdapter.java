package com.jdroid.android.coverflow;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.jdroid.android.adapter.BaseArrayAdapter;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.images.ReflectedBitmapDisplayer;
import com.jdroid.android.images.ReflectedRemoteImageResolver;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * This class is an adapter that provides base, abstract class for images adapter.
 * 
 * @param <T>
 * 
 */
public abstract class CoverFlowImageAdapter<T> extends BaseArrayAdapter<T> {
	
	private int width;
	private int height;
	
	public CoverFlowImageAdapter(Context context, List<T> objects, int width, int height) {
		super(context, objects);
		this.width = width;
		this.height = (int)(height * (1 + ReflectedRemoteImageResolver.get().getImageReflectionRatio()));
	}
	
	/**
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(parent.getContext());
			imageView.setLayoutParams(new CoverFlow.LayoutParams(width, height));
		} else {
			imageView = (ImageView)convertView;
		}
		imageView.setTag(position);
		FileContent fileContent = getFileContent(getItem(position));
		
		DisplayImageOptions.Builder optionsBuilder = new DisplayImageOptions.Builder();
		optionsBuilder.cacheInMemory(true);
		optionsBuilder.cacheOnDisc(true);
		optionsBuilder.showImageOnLoading(getDefaultDrawableId());
		optionsBuilder.showImageForEmptyUri(getDefaultDrawableId());
		optionsBuilder.showImageOnFail(getDefaultDrawableId());
		optionsBuilder.displayer(new ReflectedBitmapDisplayer());
		optionsBuilder.build();
		
		ImageLoader.getInstance().displayImage(fileContent.getUriAsString(), imageView, optionsBuilder.build());
		
		return imageView;
	}
	
	protected abstract FileContent getFileContent(T item);
	
	protected abstract int getDefaultDrawableId();
	
}