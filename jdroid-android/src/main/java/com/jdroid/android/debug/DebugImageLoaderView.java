package com.jdroid.android.debug;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.jdroid.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DebugImageLoaderView extends LinearLayout {
	
	public DebugImageLoaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public DebugImageLoaderView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		
		LayoutInflater.from(context).inflate(R.layout.debug_image_loader_view, this, true);
		
		findViewById(R.id.clearImagesDiscCache).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageLoader.getInstance().clearDiskCache();
			}
		});
		findViewById(R.id.clearImagesMemoryCache).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageLoader.getInstance().clearMemoryCache();
			}
		});
		
	}
}
