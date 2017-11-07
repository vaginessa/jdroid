package com.jdroid.android.sample.ui.imageloader;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.uil.UilImageLoaderHelper;

public class ImageLoaderFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.imageloader_fragment;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		ImageView imageView = findView(R.id.image);
		UilImageLoaderHelper.displayImage("http://jdroidtools.com/images/mainImage.png", imageView,
				R.drawable.hero);
	}
}
